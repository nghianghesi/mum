package common;

import java.util.ArrayList;
import java.util.List;

public class Sample {
	private String tag;
	private List<Double> data;
	
	public Sample(String tag, double[] data) {
		this.tag = tag;
		this.data = new ArrayList<>();
		for(double d:data) {
			this.data.add(d);
		}
	}
	
	public String getTag() {
		return tag;
	}

	public List<Double> getData() {
		return data;
	}

	public double distance(Sample v) {
		return Util.distance(this.data, v.data);
	}
}
