package rowbasedparallel;

import java.util.ArrayList;
import java.util.List;

public class ParallelRowMultiplier implements common.IMatrixMultiplier {	
	
	@Override
		public String getName() {
		return "Row based multiplier";
	}	
	
	@Override
	public void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
		List<Thread> threads = new ArrayList<>();
		int rows1 = matrix1.length;
		for (int i = 0; i < rows1; i++) {
			RowMultiplierTask task = new RowMultiplierTask(result, matrix1, matrix2, i);
			Thread thread = new Thread(task);
			thread.start();
			threads.add(thread);
			if (threads.size() % 10 == 0) {
				try {
					threads.get(0).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				threads.remove(0);
			}
		}
		waitForThreads(threads);
	}

	private static void waitForThreads(List<Thread> threads) {
		for (Thread thread : threads) {
			try {
				thread.join();
				threads.remove(0);
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		threads.clear();
	}
}
