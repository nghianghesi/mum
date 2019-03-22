package parallel;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalAtomicHistogramCalculator extends common.HistogramCalculator{

	private int max;
	public GlobalAtomicHistogramCalculator(int max) {
		this.max = max;
	}
	
	@Override
	public String getName() {
		return "Central Atomic Parallel";
	}

	@Override
	public int[] calculate(Integer[][] image) {
		int numberOfGroup = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(numberOfGroup);
		int groupsize = (image.length + numberOfGroup - 1)/numberOfGroup;
		AtomicInteger[] finalhistogram = new AtomicInteger[this.max];
		for(int i=0;i<max;i++) {
			finalhistogram[i] = new AtomicInteger();
		}
		for(int loopidx = 0;loopidx<numberOfGroup;loopidx++) {
			final int groupidx = loopidx;
			executor.execute(() -> {
				for(int i=groupidx*groupsize;i<((groupidx+1)*groupsize)&&i<image.length;i++) {
					for(int j=0;j<image[i].length;j++) {
						finalhistogram[image[i][j]].addAndGet(1);
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
		int[] res = new int[this.max];
		for(int i=0;i<max;i++) {
			res[i] = finalhistogram[i].intValue();
		}
		return res;
	}
}