package common;

public class HistogramStorage {
	private long value;
	public HistogramStorage() {
		this.value= 1;
	}	
	public HistogramStorage(long v) {
		this.value= v;
	}
	public void increase(long v) {
		this.value+=v;
	}
	public void increase() {
		this.value+=1;
	}
	public long getValue() {
		return this.value;
	}
}
