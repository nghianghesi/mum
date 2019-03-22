package common;

import java.util.Date;
import java.util.Random;

import javax.activity.InvalidActivityException;

public class Application {
	public static final int n = 200000000;

	private static void verify(int a[]) {
		for(int i=0;i<n*2-1;i++) {
			if(a[i]>a[i+1]) {
				System.out.print("Wrong!!!");
				return;
			}
		}
	}
	
	public static void main(String[] args) {
		int i;
		int[] X = new int[n];
		int[] Y = new int[n];
		int[] Z = new int[2 * n];
		Random random = new Random();
		int v = 0;
		/* input sorted lists X and Y */
		for (i = 0; i < n; i++) {
			X[i] = v = v + random.nextInt(10);
		}

		v = 0;
		for (i = 0; i < n; i++) {
			Y[i] = v = v + random.nextInt(10);
		}

		common.IMergingSorter sorters[] = new common.IMergingSorter[] {
			new serial.SerialMergingSorter(),
			new serial.Serial2LoopsMergingSorter(),
			new parallel.ParallelMergingSorter(),
			new parallel.SplitParallelMergingSorter(),
			new parallel.BinaryParallelMergingSorter()
		};


		System.out.printf("Number of items: %d\n", n);
		for (common.IMergingSorter sorter : sorters) {
			for (i = 0; i < n*2; i++) {
				Z[i] = 0;
			}
			System.out.print(sorter.getName());
			Date t = new Date();
			sorter.merge(X, Y, Z);
			Date s = new Date();
			verify(Z);
			System.out.print(" Elapsed Time: ");
			System.out.println(s.getTime() - t.getTime());
		}
	}
}
