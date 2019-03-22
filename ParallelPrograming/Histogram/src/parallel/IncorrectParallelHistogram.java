package parallel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IncorrectParallelHistogram extends common.HistogramCalculator{

	private int max;
	public IncorrectParallelHistogram(int max) {
		this.max = max;
	}
	@Override
	public String getName() {
		return "Incorrect Parallel (No locking)";
	}

	@Override
	public int[] calculate(Integer[][] image) {
		int numberOfGroup = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		int groupsize = (image.length + numberOfGroup - 1)/numberOfGroup;
		int[] finalhistogram = new int[max];
		for(int loopidx = 0;loopidx<numberOfGroup;loopidx++) {
			final int groupidx = loopidx;
			executor.execute(() -> {
				for(int i=groupidx*groupsize;i<((groupidx+1)*groupsize)&&i<image.length;i++) {
					for(int j=0;j<image[i].length;j++) {
						finalhistogram[image[i][j]]++;
					}
				}
			});
		}
		executor.shutdown();
		try {
			executor.awaitTermination(100, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalhistogram;
	}

}