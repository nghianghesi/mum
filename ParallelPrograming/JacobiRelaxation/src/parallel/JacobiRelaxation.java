package parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class JacobiRelaxation extends common.AbtractJacobiRelaxation {
	class Scan_Array implements Runnable {
		int start, end;
		CyclicBarrier barrier;
		private float A[][];
		private float B[][];

		public Scan_Array(int start, int end, float A[][], float B[][], CyclicBarrier barrier) {
			this.start = Math.max(1, start);
			this.end = Math.min(n-1, end);
			this.A = A;
			this.B = B;
			this.barrier = barrier;
		}

		@Override
		public void run() {
			try {				
				while(!sharedDone) {
					for (int i = start; i < end; i++)
						for (int j = 1; j < this.A[i].length - 1; j++)
							/* Compute average of four neighbors */
							B[i][j] = (A[i - 1][j] + A[i + 1][j] + A[i][j - 1] + A[i][j + 1]) / 4;
					barrier.await();
					sharedDone = true;
					barrier.await();
					for (int i = start; i < end; i++)
						for (int j = 1; j < this.A[i].length-1; j++) {
							float diff = Math.abs(A[i][j]-B[i][j]);
							if(diff > tolerance) {
								sharedDone = false;
							}
							A[i][j] = B[i][j];
						}
					barrier.await();
				}
			} catch (BrokenBarrierException b) {
			} catch (InterruptedException e) {
			}
		}
	}
	
	private boolean sharedDone=false;

	public JacobiRelaxation(int n, float tolerance) {
		super(n,tolerance);
	}

	@Override
	public void run(float[][] A) {
		float[][] B = new float[n][n];		
		int numberofthread = Runtime.getRuntime().availableProcessors();
		List<Thread> threads = new ArrayList<Thread>();
		int step = (this.n + numberofthread - 2) / numberofthread;
		CyclicBarrier barrier = new CyclicBarrier (numberofthread);
		for(int start=1, end=step, idx = 0; idx < numberofthread; idx+=1, start+=step, end+=step) {
			Thread thr = new Thread(new Scan_Array(start, end, A, B, barrier));
			thr.start();	
			threads.add(thr);
		}		
		
		for(Thread thr : threads) {
			try {
				thr.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getName() {
		return "Parallel verion";
	}

}
