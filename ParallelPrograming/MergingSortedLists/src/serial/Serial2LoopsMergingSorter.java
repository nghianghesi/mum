package serial;

import common.IMergingSorter;

public class Serial2LoopsMergingSorter implements IMergingSorter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Serial 2 Loops";
	}

	@Override
	public void merge(int[] x, int[] y, int[] z) {
		int i=0;
		int j=0;
		for(i=0;i<x.length;i++) {
			for(;j<y.length && x[i]>y[j];j++) {}
			z[i+j] = x[i];
		}
		
		i=0;
		for(j=0;j<y.length;j++) {
			for(;i<x.length && y[j]>=x[i];i++) {}
			z[i+j] = y[j];
		}
	}

}
