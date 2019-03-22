package parallel;

import java.util.ArrayList;
import java.util.List;

public class SplitParallelMergingSorter implements common.IMergingSorter{
	class MergingTask implements Runnable{		
		private int x[],y[],z[];
		private int startx, endx, starty;
		private boolean includeEqual;
		MergingTask(int[] x, int startx,int endx, int[] y, int starty, int[] z, boolean includeEqual){
			this.x=x;
			this.y=y;
			this.z=z;
			this.startx= startx;
			this.endx = endx;
			this.starty = starty;
			this.includeEqual = includeEqual;
		}

		@Override
		public void run() {
			int j = starty;
			for(int i=startx; i<x.length && i<endx;i++) {
				for(;j<y.length && (x[i]>y[j] || (includeEqual && x[i]==y[j]));j++) {}
				z[i+j] = x[i];
			}
		}
	}

	@Override
	public String getName() {
		return "Spitting parallel";
	}
	
	private int binarySearchStart(int[] a, int start, int value, boolean includeEqual) {
		int end = a.length - 1;
		int mid = (start+end)/2;
		while(start<end) {
			if(value > a[mid] || (includeEqual && value == a[mid])) {
				start = mid + 1;
			}else {
				end = mid;
			}
			mid = (start+end)/2;
		}
		return start;
	}

	@Override
	public void merge(int[] x, int[] y, int[] z) {
		int numberofthread = Runtime.getRuntime().availableProcessors();
		List<Thread> threads = new ArrayList<Thread>();

		int step = (x.length + numberofthread - 1)/numberofthread;
		int startx = 0, starty = 0, endx = 0, endy = 0;
		for(startx=0, endx = step;startx<x.length;startx+=step, endx+=step) {
			starty=binarySearchStart(y, starty, x[startx], false);
			Thread threadxy = new Thread(new MergingTask(x, startx, endx, y, starty, z, false) );
			threadxy.start();	
			threads.add(threadxy);
		}		
		
		startx = starty = endx = endy = 0;
		for(starty=0,endy = step;starty<y.length;starty+=step, endy += step) {
			startx=binarySearchStart(x, startx, y[starty], true);
			Thread threadyx = new Thread(new MergingTask(y,starty, endy,x, startx,z,true));
			threadyx.start();
			threads.add(threadyx);
		}
		
		for(Thread th : threads) {
			try {
				th.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
