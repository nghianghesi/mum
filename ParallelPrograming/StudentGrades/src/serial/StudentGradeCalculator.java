package serial;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import common.StudentGrade;

public class StudentGradeCalculator extends common.StudentGradeCalculator {

	public StudentGradeCalculator(List<StudentGrade> grades) {
		super(grades);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected <T> Stream<T> getStream(Collection<T> list) {
		return list.stream();
	}

	@Override
	public String getName() {
		return "Serial";
	}

}
