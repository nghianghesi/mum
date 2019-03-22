package common;

public abstract class AbtractJacobiRelaxation {
	protected int n;
	protected float tolerance;
	
	public abstract void run(float A[][]);
	public abstract String getName();
	
	protected AbtractJacobiRelaxation(int n, float tolerance) {
		this.n = n;
		this.tolerance = tolerance;
	}
}
