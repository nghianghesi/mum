package common;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class StudentGradeCalculator {
	class StudentGradeSummary {
		private int totalCredits;
		private double creditGrades;
		private int departerid;
		private int studentid;
		public StudentGradeSummary() {
			
		}
		public StudentGradeSummary(StudentGrade s) {
			this.studentid = s.getStudentId();
			this.totalCredits = s.getCredits();
			this.creditGrades = s.getCredits() * s.getGrades();
			this.departerid = s.getDepartmentId();
		}
		public StudentGradeSummary addSummary(StudentGradeSummary s) {		
			this.studentid = s.studentid;
			this.totalCredits += s.totalCredits;
			this.creditGrades += s.creditGrades;
			this.departerid = s.departerid;
			return this;
		}
		public double getGPA() {
			return this.totalCredits!=0?this.creditGrades/this.totalCredits:0;
		}
		@Override
		public String toString() {
			return this.studentid + "/" + this.departerid + "/" + this.creditGrades +"/"+this.totalCredits+" ";
		}
	}
	protected List<StudentGrade> grades;
	public StudentGradeCalculator(List<StudentGrade> grades) {
		this.grades = grades;
	}
	
	protected abstract <T> Stream<T> getStream(Collection<T> list);
	public abstract String getName();
	
	public Map<Integer,Double> studentGPA() {
		return this.getStream(this.grades)
				.collect(Collectors.groupingBy(StudentGrade::getStudentId, 
							Collectors.mapping(StudentGradeSummary::new, 
												Collectors.reducing((gs,s)->gs.addSummary(s)))))
				.entrySet().stream()
				.collect(Collectors.toMap(Entry::getKey, 
							e->e.getValue().get().getGPA()));
	}
	
	public  Map<Integer,Double> highestGPAbyDepartment()	{
		return this.getStream(this.grades)
				.collect(Collectors.groupingBy(StudentGrade::getDepartmentId, 
					Collectors.groupingBy(StudentGrade::getStudentId, 
							Collectors.mapping(StudentGradeSummary::new, Collectors.reducing((gs,s)->gs.addSummary(s))))))
				.entrySet().stream()
				.collect(Collectors.toMap(Entry::getKey, 
							e->e.getValue().values().stream().map((s) -> s.get())
								.max(Comparator.comparing(StudentGradeSummary::getGPA)).get().getGPA()));
	}
	
	public Map<Integer, Double> lowestGPAbyDepartment(){
		return this.getStream(this.grades)
				.collect(Collectors.groupingBy(StudentGrade::getDepartmentId, 
							Collectors.groupingBy(StudentGrade::getStudentId, 
						Collectors.mapping(StudentGradeSummary::new, Collectors.reducing((gs,s)->gs.addSummary(s))))))
				.entrySet().stream().collect(Collectors.toMap(Entry::getKey, 
								e->e.getValue().values().stream().map((s) -> s.get())
				.min(Comparator.comparing(StudentGradeSummary::getGPA)).get().getGPA()));
	}
	
	public Map<Integer,Double> averageGPAbyDepartment(){		
		return this.getStream(this.grades)
				.collect(Collectors.groupingBy(StudentGrade::getDepartmentId, 
				Collectors.groupingBy(StudentGrade::getStudentId, 
						Collectors.mapping(StudentGradeSummary::new, 
								Collectors.reducing((gs,s)->gs.addSummary(s))))))
					.entrySet().stream().collect(Collectors.toMap(Entry::getKey, 
								e->e.getValue().values().stream().map((s) -> s.get())
					.collect(Collectors.averagingDouble(StudentGradeSummary::getGPA))));
	}
	
	public Map<String,Double> averageGradeByCourse(){
		return this.getStream(this.grades)
				.collect(Collectors.groupingBy(StudentGrade::getCourseNumber,
							Collectors.mapping(StudentGrade::getGrades, 
									Collectors.averagingDouble(x->x))));
	}
}
