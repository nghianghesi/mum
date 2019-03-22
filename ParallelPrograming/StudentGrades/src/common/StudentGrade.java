package common;

import java.util.concurrent.ThreadLocalRandom;

public class StudentGrade {
	private int studentId;
	private int departmentId;
	private String courseNumber;	
	private java.util.Date CourseDate;	
	private int credits;	
	private double grades;
	
	public static StudentGrade randomGrade(int i) {
		StudentGrade g = new StudentGrade();
		g.studentId = ThreadLocalRandom.current().nextInt(100);
		g.departmentId = g.studentId%10;
		g.courseNumber = ""+ThreadLocalRandom.current().nextInt(20);
		g.grades = Math.min(4, ThreadLocalRandom.current().nextDouble(1+i%2) + ThreadLocalRandom.current().nextDouble(1+i%2) + ThreadLocalRandom.current().nextDouble(1+i%2));
		g.credits = ThreadLocalRandom.current().nextInt(20) > 10 ? 4:2;
		return g;
	}
	
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public String getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}
	public java.util.Date getCourseDate() {
		return CourseDate;
	}
	public void setCourseDate(java.util.Date courseDate) {
		CourseDate = courseDate;
	}
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public double getGrades() {
		return grades;
	}
	public void setGrades(double grades) {
		this.grades = grades;
	}
	@Override
	public String toString() {
		return this.studentId + "/" + this.departmentId + "/" + this.credits +"/"+this.grades+" ";
	}
}

