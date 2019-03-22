package parallel;

public class ParallelMergingSorter implements common.IMergingSorter{

	class MergingTask implements Runnable{
		private int x[],y[],z[];
		private boolean includeEqual;
		public MergingTask(int[] x, int[] y, int[] z, boolean includeEqual) {
			this.x=x;
			this.y=y;
			this.z = z;
			this.includeEqual= includeEqual;
		}

		@Override
		public void run() {	
			if(!includeEqual) {
				int j=0;
				for(int i=0;i<x.length;i++) {
					for(;j<y.length && x[i]>y[j];j++) {}
					z[i+j] = x[i];
				}
			} else {
				int j = y.length-1;
				for(int i=x.length-1;i>=0;i--) {
					for(;j>=0 && x[i]<y[j];j--) {}
					z[i+j+1] = x[i];
				}
			}
		}

	}
	
	@Override
	public String getName() {
		return "Parallel (2 threads)";
	}

	@Override
	public void merge(int[] x, int[] y, int[] z) {
		// TODO Auto-generated method stub
		Thread threadxy = new Thread(new MergingTask(x,y,z,false) );
		threadxy.start();		
		
		Thread threadyx = new Thread(new MergingTask(y,x,z,true));
		threadyx.start();
		
		try {
			threadxy.join();		
			threadyx.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
