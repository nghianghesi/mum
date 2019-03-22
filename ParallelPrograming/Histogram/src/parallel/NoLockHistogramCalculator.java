package parallel;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NoLockHistogramCalculator extends common.HistogramCalculator{
	private int max;
	public NoLockHistogramCalculator(int max) {
		this.max = max;
	}
	@Override
	public String getName() {
		return "No Lock Parallel";
	}

	@Override
	public int[] calculate(Integer[][] image) {
		int numberOfGroup = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		int groupsize = (image.length + numberOfGroup - 1)/numberOfGroup;
		int[][] finalhistogram = new int[numberOfGroup][this.max + 100];
		for(int loopidx = 0;loopidx<numberOfGroup;loopidx++) {
			final int groupidx = loopidx;
			final int[] histogram = finalhistogram[groupidx];
			executor.execute(() -> {
				for(int i=groupidx*groupsize;i<((groupidx+1)*groupsize)&&i<image.length;i++) {
					for(int j=0;j<image[i].length;j++) {
						histogram[image[i][j]]++;
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
		int[] res = new int[max];
		for(int groupidx =0 ;groupidx<numberOfGroup; groupidx ++) {
			for(int i=0;i<this.max;i++) {
				res[i]+=finalhistogram[groupidx][i];
			}
		}
		return res;
	}

}
