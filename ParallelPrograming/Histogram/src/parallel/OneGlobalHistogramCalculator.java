package parallel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OneGlobalHistogramCalculator extends common.HistogramCalculator{

	private int max;
	public OneGlobalHistogramCalculator(int max) {
		this.max = max;
	}
	@Override
	public String getName() {
		return "Central & Synchronize Parallel";
	}

	@Override
	public int[] calculate(Integer[][] image) {
		int numberOfGroup = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(numberOfGroup);
		int groupsize = (image.length + numberOfGroup - 1)/numberOfGroup;
		common.HistogramStorage[] finalhistogram = new common.HistogramStorage[this.max];
		for(int i=0;i<this.max;i++) {finalhistogram[i] = new common.HistogramStorage(0);}
		for(int loopidx = 0;loopidx<numberOfGroup;loopidx++) {
			final int groupidx = loopidx;
			executor.execute(() -> {
				for(int i=groupidx*groupsize;i<((groupidx+1)*groupsize)&&i<image.length;i++) {
					for(int j=0;j<image[i].length;j++) {
						synchronized (finalhistogram[image[i][j]]) {
							finalhistogram[image[i][j]].increase();
						}					
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
