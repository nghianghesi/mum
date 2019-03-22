package parallel;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MultipleAtomicHistogramCalculator extends common.HistogramCalculator{

	private int max;
	public MultipleAtomicHistogramCalculator(int max) {
		this.max = max;
	}
	
	@Override
	public String getName() {
		return "Multi Atomic Parallel";
	}

	@Override
	public int[] calculate(Integer[][] image) {
		int numberOfGroup = Runtime.getRuntime().availableProcessors();
		
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		int groupsize = (image.length + numberOfGroup - 1)/numberOfGroup;
		AtomicInteger[] finalhistograms = new AtomicInteger[this.max];
		for(int j=0;j<this.max;j++) {
			finalhistograms[j] = new AtomicInteger(0);
		}
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
					finalhistograms[i].addAndGet(histogram[i]);
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
			res[i] = finalhistograms[i].intValue();
		}
		return res;
	}

}
