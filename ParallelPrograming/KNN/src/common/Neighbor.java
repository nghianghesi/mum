package common;

public class Neighbor implements Comparable<Neighbor>{
	private String tag;
	private double distance;
	public String getTag() {
		return tag;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public Neighbor(common.Sample train, common.Sample v) {
		this.tag = train.getTag();
		this.distance = train.distance(v);
	}
	
	@Override
	public int compareTo(Neighbor other) {
		// TODO Auto-generated method stub
		if (this.distance < other.getDistance()) {
			return -1;
		} else if (this.distance > other.getDistance()) {
			return 1;
		}
		return 0;
	}
}
