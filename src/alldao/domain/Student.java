package alldao.domain;

public class Student {

	private int Sid;
	private String Sname;
	private int Sage;
	private int Ssex;
	

//	public Student(){
//		
//	}
//	public Student(int sid ,String sname , int sage , int ssex){
//		this.Sid = sid;
//		this.Sname = sname;
//		this.Sage = sage;
//		this.Ssex = ssex;
//	}
	
	public int getSid() {
		return Sid;
	}
	public void setSid(int sid) {
		Sid = sid;
	}
	public String getSname() {
		return Sname;
	}
	public void setSname(String sname) {
		Sname = sname;
	}
	public int getSage() {
		return Sage;
	}
	public void setSage(int sage) {
		Sage = sage;
	}
	public int getSsex() {
		return Ssex;
	}
	public void setSsex(int ssex) {
		Ssex = ssex;
	}
	@Override
	public String toString() {
		return "Student [Sid=" + Sid + ", Sname=" + Sname + ", Sage=" + Sage + ", Ssex=" + Ssex + "]";
	}
	
	
}
