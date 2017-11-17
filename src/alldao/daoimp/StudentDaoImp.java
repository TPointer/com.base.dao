package alldao.daoimp;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import alldao.dao.StudentDao;
import alldao.domain.Student;
import alldao.util.ConnectionManager;

public class StudentDaoImp extends BaseDaoImp<Student> implements StudentDao {

	private Class<?> EntityClass;
	private String sql;
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	private List<Student> list;

	public StudentDaoImp() {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		EntityClass = (Class<?>) type.getActualTypeArguments()[0];
	}

	@Override
	public List<Student> selectAll() {
		StringBuffer b = new StringBuffer();
		list = new ArrayList<Student>();
		sql = b.append("select * from " + EntityClass.getSimpleName()).toString();
		try {
			conn = ConnectionManager.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Student student = new Student();
				student.setSid(rs.getInt("Sid"));
				student.setSname(rs.getString("Sname"));
				student.setSage(rs.getInt("Sage"));
				student.setSsex(rs.getInt("Ssex"));
				list.add(student);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
