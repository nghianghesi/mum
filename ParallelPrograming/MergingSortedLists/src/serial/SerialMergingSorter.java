package serial;

import common.IMergingSorter;

public class SerialMergingSorter implements IMergingSorter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Serial (optimized)";
	}

	@Override
	public void merge(int[] x, int[] y, int[] z) {
		int i=0;
		int j=0;
		int k=0;
		while(i<x.length || j<y.length) {
			if(i==x.length || (j<y.length && x[i]>y[j])) {
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
