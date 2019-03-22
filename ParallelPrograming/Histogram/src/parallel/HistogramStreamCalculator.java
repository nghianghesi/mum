package parallel;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


public class HistogramStreamCalculator extends common.HistogramCalculator{
	private int max;
	public HistogramStreamCalculator(int max) {
		this.max = max;
	}
	@Override
	public String getName() {
		return "Parallel Stream";
	}

	@Override
	public int[] calculate(Integer[][] image) {
		Map<Integer,Long> mapRes = Arrays.stream(image).parallel()
		.map(r->Arrays.stream(r)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet())
		.flatMap(Set::stream)
		.collect(Collectors.groupingBy(Entry::getKey, Collectors.summingLong(Entry::getValue)));
		int[] res = new int[this.max];
		for(Entry<Integer,Long> e : mapRes.entrySet()) {
			res[e.getKey()] = e.getValue().intValue();
		}
		return res;
	}

}
