package alldao.dao;

import java.util.List;

import alldao.domain.Student;

public interface StudentDao extends BaseDao<Student>{
	//新增的业务逻辑
	List<Student> selectAll();

}
