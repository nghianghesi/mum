package parallel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import common.StudentGrade;

public class StudentGradeCalculator extends common.StudentGradeCalculator{

	public StudentGradeCalculator(List<StudentGrade> grades) {
		super(grades);
	}

	@Override
	protected <T> Stream<T> getStream(Collection<T> list) {
		return list.parallelStream();
	}

	@Override
	public String getName() {
		return "Parallel";
	}

}
