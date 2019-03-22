package common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class App {
	
	private static int n = 10000;
	private static int max = 100;
	
	private static Integer[][] generateData(){
		Integer[][] image = new Integer[n][n];
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				image[i][j] =  ThreadLocalRandom.current().nextInt(max);
			}
		}
		return image;
	}
	public static void main(String args[]) {
		Integer[][] image = generateData();
		

		List<common.HistogramCalculator> calculators = new ArrayList<>();
		calculators.add(new serial.HistogramCalculator(max));
		calculators.add(new parallel.IncorrectParallelHistogram(max));
		calculators.add(new parallel.OneGlobalHistogramCalculator(max));
		calculators.add(new parallel.GlobalAtomicHistogramCalculator(max));
		calculators.add(new parallel.MultipleLocalHistogramCalculator(max));
		calculators.add(new parallel.MultipleAtomicHistogramCalculator(max));
		calculators.add(new parallel.NoLockHistogramCalculator(max));
		calculators.add(new parallel.HistogramStreamCalculator(max));
		
		System.out.println("n:"+n+" max:"+max);
		long serialTime=-1; 
		for(HistogramCalculator cal : calculators) {
			System.out.println(cal.getName());
			Date t = new Date();
			int[] histogram = cal.calculate(image);
			int limit = 20;
			for(int i=0;i<limit&&i<histogram.length;i++) {
				System.out.print(i+"/"+histogram[i]+" ");
			}
			System.out.println();
			long time= (new Date()).getTime() - t.getTime();
			if(serialTime<0) {
				serialTime=time;
				System.out.println("Elapsed Time: " + time);
			}else {
				System.out.println("Elapsed Time: " + time + " speed up "+ (1.0*serialTime/time));
			}
		}		
	}
}
