package stream;

import java.util.stream.IntStream;

public class ForeachJacobiRelaxation extends common.AbtractJacobiRelaxation {

	public ForeachJacobiRelaxation(int n, float tolerance) {
		super(n, tolerance);
	}

	float[][] B;
	float[][] A;
	boolean done;
	@Override
	public void run(float[][] input) {
		this.A = input;
		this.B = new float[n][n];
		
		// init B values
		IntStream.range(0,n)
		.parallel()
		.forEach(i->IntStream.range(0,n).forEach(j -> B[i][j] = A[i][j]));

		this.done = false;
		while (!done) {
			done = true;
			IntStream.range(1,n-1)
					.parallel()
					.forEach(i->IntStream.range(1,n-1)
								.forEach(j -> {
									B[i][j] = (float)((A[i - 1][j] + A[i + 1][j] + A[i][j - 1] + A[i][j + 1]) / 4.0);
									if(Math.abs(B[i][j] - A[i][j]) > this.tolerance) {
										done=false;
									}
								}));
			float[][] t = A;A=B;B=t;
		}
		
		// update final result
		if(input != A) {
			IntStream.range(0,n)
			.parallel()
			.forEach(i->IntStream.range(0,n)
						.forEach(j -> input[i][j] = A[i][j]));
		}
		this.A = this.B = null;
	}

	@Override
	public String getName() {
		return "Stream foreach version";
	}
}
