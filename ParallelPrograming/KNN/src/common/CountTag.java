package common;

public class CountTag implements Comparable<CountTag>{
	int value = 0;
	private String tag;
	public int getValue() {
		return value;
	}
	public String getTag() {
		return tag;
	}
	public CountTag(String tag) {
		this.value = 1;
		this.tag= tag;
	}
	public void Increase() {
		this.value+=1;		
	}
	@Override
	public int compareTo(CountTag other) {
		// TODO Auto-generated method stub
		if (this.value < other.getValue()) {
			return -1;
		} else if (this.value > other.getValue()) {
			return 1;
		}
		return 0;
	}
}
