package common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class app {

	private static List<StudentGrade> generateData(){
		return IntStream.range(0, 1000000).mapToObj(StudentGrade::randomGrade).collect(Collectors.toList());
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<StudentGrade> data = generateData();
		List<StudentGradeCalculator> calculators = new ArrayList<>();
		calculators.add(new serial.StudentGradeCalculator(data));
		calculators.add(new parallel.StudentGradeCalculator(data));
		long serialTime=-1; 
		for(StudentGradeCalculator cal : calculators) {
			System.out.println(cal.getName());
			Date t = new Date();
			System.out.println("Student GPA:"+cal.studentGPA());
			System.out.println("Dept Avg GPA:"+cal.averageGPAbyDepartment());
			System.out.println("Dept Highest GPA:"+cal.highestGPAbyDepartment());
			System.out.println("Dept Lowest GPA:"+cal.lowestGPAbyDepartment());
			System.out.println("Course Avg Grade:"+cal.averageGradeByCourse());
			long time= (new Date()).getTime() - t.getTime();
			if(serialTime<0) {
				serialTime=time;
				System.out.println("Elapsed Time: " + time);
			}else {
				System.out.println("Elapsed Time: " + time + " speed up "+ (1.0*serialTime/time));
			}
		}
	}

}
