package elementbasedparallel;

import java.util.ArrayList;
import java.util.List;


public class ParallelIndividualMultiplier implements common.IMatrixMultiplier{
	@Override
	public String getName() {
		return "Individual based multiplier";
	}	
	
	@Override
	public void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
		List<Thread> threads = new ArrayList<>();
		int rows1 = matrix1.length;
		int column2 = matrix1[0].length;
		for (int i = 0; i < rows1; i++) {
			for (int j = 0; j < column2; j++) {
				IndividualMultiplierTask task = new IndividualMultiplierTask(result, matrix1, matrix2, i, j);
				Thread thread = new Thread(task);
				thread.start();
				threads.add(thread);
				if (threads.size() % 10 == 0) {
					try {
						threads.get(0).join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					threads.remove(0);
				}
			}
		}					
		waitForThreads(threads);

	}

	private static void waitForThreads(List<Thread> threads) {
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		threads.clear();
	}
}
