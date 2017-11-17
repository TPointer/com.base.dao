package alldao.test;

import java.util.List;

import alldao.daoimp.StudentDaoImp;
import alldao.domain.Student;

public class Test {

	public static void main(String args[]) {
		List<Student> list = null;
		StudentDaoImp imp = new StudentDaoImp();

		Student student = new Student();
		student.setSid(6);
		student.setSname("StudentX");
		student.setSage(22);
		student.setSsex(0);
		
		//imp.add(student);
		//imp.del(student);
		//imp.update(student);
		Student s = imp.selectById(student);
		System.out.println(s.toString());
		
//		list = imp.selectAll();
//		for (Student students : list) {
//			System.out.println(students.getSid() + " " + students.getSname() + " " + students.getSage() + " "
//					+ students.getSsex());
//		}
	}
}
