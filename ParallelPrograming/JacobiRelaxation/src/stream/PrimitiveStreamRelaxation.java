package stream;

import java.util.stream.IntStream;

public class PrimitiveStreamRelaxation extends common.AbtractJacobiRelaxation {

	public PrimitiveStreamRelaxation(int n, float tolerance) {
		super(n, tolerance);
	}

	private float[][] B;
	private float[][] A;
	@Override
	public void run(float[][] input) {
		this.A = input;
		this.B = new float[n][n];
		
		// init B values
		IntStream.range(0,n)
		.parallel()
		.map(i->IntStream.range(0,n).map(j -> (int)(B[i][j] = A[i][j])).min().getAsInt())
		.min();
		
		boolean done = false;
		while (!done) {
			int res = IntStream.range(1,n-1)
					.parallel()
					.map(i->IntStream.range(1,n-1)
								.map(j -> {
									B[i][j] = (float)((A[i - 1][j] + A[i + 1][j] + A[i][j - 1] + A[i][j + 1]) / 4.0);
									return Math.abs(B[i][j] - A[i][j]) <= this.tolerance ? 1:0;
								}).min().getAsInt())
					.min().getAsInt();
			float[][] t = A;A=B;B=t;
			done = (res == 1);
		}
		
		// update final result
		if(input != A) {
			IntStream.range(0,n)
			.parallel()
			.map(i->IntStream.range(0,n).map(j -> (int)(input[i][j] = A[i][j])).min().getAsInt())
			.min();
		}
		this.A = this.B = null;
	}

	@Override
	public String getName() {
		return "Primitive stream reduction version";
	}
}
