package serial;

public class JacobiRelaxation extends common.AbtractJacobiRelaxation {

	public JacobiRelaxation(int n, float tolerance) {
		super(n, tolerance);
	}

	@Override
	public void run(float A[][]) {
		float B[][] = new float[n][n];
		boolean done = false;
		while (!done) {
			done = true;
			for (int i = 1; i < n - 1; i++)
				for (int j = 1; j < n - 1; j++)
					B[i][j] = (float) ((A[i - 1][j] + A[i + 1][j] + A[i][j - 1] + A[i][j + 1]) / 4.0);

			for (int i = 1; i < n - 1; i++) {
				for (int j = 1; j < n - 1; j++) {
					float change = Math.abs(B[i][j] - A[i][j]);
					if (change > tolerance)
						done = false;
					A[i][j] = B[i][j];
				}
			}
		}
	}

	@Override
	public String getName() {
		return "Serial version";
	}
}
