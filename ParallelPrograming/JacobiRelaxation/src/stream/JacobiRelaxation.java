package stream;

import java.util.stream.IntStream;

public class JacobiRelaxation extends common.AbtractJacobiRelaxation {

	public JacobiRelaxation(int n, float tolerance) {
		super(n, tolerance);
	}

	float[][] B;
	float[][] A;
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
			done = IntStream.range(1,n-1)
					.parallel()
					.mapToObj(i->IntStream.range(1,n-1)
								.mapToObj(j -> {
									B[i][j] = (float)((A[i - 1][j] + A[i + 1][j] + A[i][j - 1] + A[i][j + 1]) / 4.0);
									return Math.abs(B[i][j] - A[i][j]) <= this.tolerance;
								}).reduce(true, (x,y) -> x&&y))
					.reduce(true, (x,y) -> x&&y);
			float[][] t = A;A=B;B=t;
		}
		
		// update final result
		if(input != A) {
			IntStream.range(0,n)
			.parallel()
			.map(i->IntStream.range(0,n)
						.map(j -> (int)(input[i][j] = A[i][j])).min().getAsInt())
			.min();
		}
		this.A = this.B = null;
	}

	@Override
	public String getName() {
		return "Stream reduction version";
	}
}
