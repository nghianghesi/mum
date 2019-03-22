package common;

import java.util.Date;

public class Application {
	static int n = 10000;
	static float tolerance = (float) 0.1;
	public static void main(String[] args) {
		AbtractJacobiRelaxation[] processors = new AbtractJacobiRelaxation[] {
			new serial.JacobiRelaxation(n, tolerance),			
			new parallel.JacobiRelaxation(n, tolerance),
			new stream.JacobiRelaxation(n, tolerance),
			new stream.PrimitiveStreamRelaxation(n, tolerance),
			new stream.ForeachJacobiRelaxation(n, tolerance),
			new stream.ForeachArrayDoneJacobiRelaxation(n, tolerance)
		};

		//Random random = new Random();
		float[][] A = new float[n][n];
		/*for(int i=0;i<n;i++) {
			for(int j=0;i<n;i++) {
				A[i][j] = random.nextFloat();
			}
		}*/
		for (int i = 0; i < n; i++) {
			A[i][0] = 10; A[i][n-1] = 10;
			A[0][i] = 10; A[n-1][i] = 10;
		}
		
		
		
		float[][] paramA = new float[n][n];
		System.out.println("n: "+n);
		for (common.AbtractJacobiRelaxation p : processors) {
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
					paramA[i][j] = A[i][j];
				}
			}
			System.out.println(p.getName());
			Date t = new Date();
			p.run(paramA);
			Date s = new Date();
			for(int i=0;i<10;i++) {
				for(int j=0;j<10;j++) {
					System.out.print(" " + paramA[i][j]);
				}
				System.out.println();
			}	
			System.out.print(" elapsed Time: ");
			System.out.println(s.getTime() - t.getTime());
		}		
	}

}
