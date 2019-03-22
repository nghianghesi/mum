package parallel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultipleLocalHistogramCalculator extends common.HistogramCalculator{

	private int max;
	public MultipleLocalHistogramCalculator(int max) {
		this.max = max;
	}
	@Override
	public String getName() {
		return "Multi Local & Synchronize Parallel";
	}

	@Override
	public int[] calculate(Integer[][] image) {
		int numberOfGroup = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		int groupsize = (image.length + numberOfGroup - 1)/numberOfGroup;
		common.HistogramStorage[] finalhistogram = new common.HistogramStorage[this.max];
		for(int i=0;i<this.max;i++) {finalhistogram[i] = new common.HistogramStorage(0);}
		for(int loopidx = 0;loopidx<numberOfGroup;loopidx++) {
			final int groupidx = loopidx;
			final int[] histogram = new int[this.max + 100];			
			executor.execute(() -> {
				for(int i=groupidx*groupsize;i<((groupidx+1)*groupsize)&&i<image.length;i++) {
					for(int j=0;j<image[i].length;j++) {
						histogram[image[i][j]]++;
					}
				}
				for(int i=0;i<this.max;i++) {
					synchronized (finalhistogram[i]) {
						finalhistogram[i].increase(histogram[i]);
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
		for(int i=0;i<this.max;i++) {
			res[i] = (int)finalhistogram[i].getValue();
		}
		return res;
	}

}
