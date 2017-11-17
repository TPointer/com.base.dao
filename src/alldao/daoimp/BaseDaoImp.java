package alldao.daoimp;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import alldao.dao.BaseDao;
import alldao.util.ConnectionManager;

public class BaseDaoImp<T> implements BaseDao<T> {

	public static final String SQL_INSERT = "insert";
	public static final String SQL_DELETE = "delete";
	public static final String SQL_UPDATE = "update";
	public static final String SQL_SELECT = "select";

	private Class<T> EntityClass;
	private Connection conn;
	private PreparedStatement ps;
	private String sql;
	private Object argType[];
	private ResultSet rs;

	@SuppressWarnings("unchecked")
	public BaseDaoImp() {

		// getGenericSuperclass()方法可以获取当前对象的超类的type
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		// 从一个泛型类型中获得第一个泛型类型的实体类
		EntityClass = (Class<T>) type.getActualTypeArguments()[0];
	}

	// sql拼接函数 形如 : insert into Student(Sid,Sname,Sage,Ssex) values(?,?,?,?)
	private String getSql(String sqlType) {
		StringBuffer sql = new StringBuffer();
		// 通过反射获取实体类中的所有变量
		Field fields[] = EntityClass.getDeclaredFields();

		if (sqlType.equals(SQL_INSERT)) {
			sql.append("insert into " + EntityClass.getSimpleName());
			sql.append("(");
			for (int i = 0; fields != null && i < fields.length; i++) {
				fields[i].setAccessible(true);// 因为类中的属性是private的，所以这句话必须加
				String column = fields[i].getName();
				sql.append(column).append(",");
			}
			sql = sql.deleteCharAt(sql.length() - 1);// 删除最后一个字符
			sql.append(") values (");
			for (int i = 0; fields != null && i < fields.length; i++) {
				sql.append("?,");
			}
			sql = sql.deleteCharAt(sql.length() - 1);
			sql.append(")");// 注意结尾分号
		} else if (sqlType.equals(SQL_DELETE)) {
			sql.append("delete from " + EntityClass.getSimpleName() + " where Sid=?");
		} else if (sqlType.equals(SQL_UPDATE)) {
			sql.append("update " + EntityClass.getSimpleName() + " set ");
			for (int i = 0; fields != null && i < fields.length; i++) {
				fields[i].setAccessible(true);
				String column = fields[i].getName();
				if (column.equals("Sid")) {
					continue;
				}
				sql.append(column).append("=").append("?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where Sid=?");
		} else if (sqlType.equals(SQL_SELECT)) {
			sql.append("select * from " + EntityClass.getSimpleName() + " where Sid=?");
		}
		return sql.toString();
	}

	// 获取参数
	private Object[] setArgs(T entity, String sqlType) throws IllegalAccessException, IllegalArgumentException {
		Field fields[] = EntityClass.getDeclaredFields();

		if (sqlType.equals(SQL_INSERT)) {
			Object[] obj = new Object[fields.length];
			for (int i = 0; obj != null && i < fields.length; i++) {
				fields[i].setAccessible(true);
				obj[i] = fields[i].get(entity);// 拿到entity中的值
			}
			return obj;
		} else if (sqlType.equals(SQL_DELETE)) {
			Object[] obj = new Object[1];
			fields[0].setAccessible(true);
			obj[0] = fields[0].get(entity);
			return obj;
		} else if (sqlType.equals(SQL_UPDATE)) {
			Object[] tempobj = new Object[fields.length];
			for (int i = 0; tempobj != null && i < fields.length; i++) {
				fields[i].setAccessible(true);
				tempobj[i] = fields[i].get(entity);
			}
			Object[] obj = new Object[fields.length];
			System.arraycopy(tempobj, 1, obj, 0, tempobj.length - 1);
			obj[obj.length - 1] = tempobj[0];
			return obj;
		} else if (sqlType.equals(SQL_SELECT)) {
			Object obj[] = new Object[1];
			fields[0].setAccessible(true);
			obj[0] = fields[0].get(entity);
			return obj;
		}
		return null;
	}

	@Override
	public void add(T t) {
		sql = this.getSql(SQL_INSERT);// 获取sql
		// 赋值：
		try {
			argType = setArgs(t, SQL_INSERT);
			conn = ConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);
			ps = ConnectionManager.setPreparedStatementParam(ps, argType);
			ps.executeUpdate();
		} catch (IllegalAccessException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionManager.releaseDB(rs, ps, conn);
		}
	}

	@Override
	public void del(T t) {
		sql = this.getSql(SQL_DELETE);
		try {
			argType = this.setArgs(t, SQL_DELETE);
			conn = ConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);
			ps = ConnectionManager.setPreparedStatementParam(ps, argType);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionManager.releaseDB(rs, ps, conn);
		}

	}

	@Override
	public void update(T t) {
		sql = this.getSql(SQL_UPDATE);
		try {
			argType = this.setArgs(t, SQL_UPDATE);
			conn = ConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);
			ps = ConnectionManager.setPreparedStatementParam(ps, argType);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionManager.releaseDB(rs, ps, conn);
		}
	}

	@Override
	public T selectById(T t) {
		sql = this.getSql(SQL_SELECT);
		T obj = null;
		try {
			argType = setArgs(t, SQL_SELECT);
			conn = ConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);
			ps = ConnectionManager.setPreparedStatementParam(ps, argType);
			rs = ps.executeQuery();

			Field fields[] = EntityClass.getDeclaredFields();
			while (rs.next()) {
				obj = EntityClass.newInstance();
				for (int i = 0; i < fields.length; i++) {
					fields[i].setAccessible(true);
					if (i == 0) {
						fields[i].setInt(obj, rs.getInt(fields[i].getName()));
					} else {
						fields[i].set(obj, rs.getObject(fields[i].getName()));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

}
