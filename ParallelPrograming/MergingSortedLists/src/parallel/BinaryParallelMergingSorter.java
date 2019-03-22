package parallel;

import java.util.ArrayList;
import java.util.List;

public class BinaryParallelMergingSorter implements common.IMergingSorter{
	class MergingTask implements Runnable{		
		private int x[],y[],z[];
		private int startx, endx, starty, endy;		MergingTask(int[] x, int startx, int endx, int[] y, int starty, int endy, int[] z){
			this.x=x;
			this.y=y;
			this.z=z;
			this.startx= startx;
			this.endx = Math.min(endx,x.length);
			this.starty = starty;
			this.endy = Math.min(endy, y.length);
		}

		@Override
		public void run() {
			int i=startx;
			int j=starty;
			int k=startx+starty;
			while((i<endx) || j<endy) {
				if(i==endx || (j<endy && x[i]>y[j])) {
					z[k] = y[j];
					j++;
				}else {
					z[k] = x[i];
					i++;
				}
				k++;
			}	
		}
	}

	@Override
	public String getName() {		
		return "Binary parallel";
	}
	

	
	private int binarySearchStart(int[] a, int start, int value) {
		int end = a.length - 1;
		int mid = (start+end)/2;
		while(start<end) {
			if(value > a[mid]) {
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
		int numberofstep = x.length/numberofthread;
		int startx = 0, starty = 0, endx = 0, endy = 0;
		for(startx=0, endx = numberofstep;startx<x.length;startx+=numberofstep, endx+=numberofstep) {
			starty = endy;
			if(endx < x.length) {
				endy = binarySearchStart(y, starty, x[endx]);
			}else {
				endy = y.length;
			}
			
			Thread thread = new Thread(new MergingTask(x, startx, endx, y, starty, endy, z) );
			thread.start();
			threads.add(thread);
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
