package common;

import java.util.Date;

import elementbasedparallel.ParallelIndividualMultiplier;
import groupbasedparallel.ParallelGroupMultiplier;
import rowbasedparallel.ParallelRowMultiplier;
import serial.SerialMultiplier;

public class Main {

	public static void main(String[] args) {
		IMatrixMultiplier multiplers[] = new IMatrixMultiplier[4];
		multiplers[0] = new SerialMultiplier();
		multiplers[1] = new ParallelIndividualMultiplier();
		multiplers[2] = new ParallelRowMultiplier();
		multiplers[3] = new ParallelGroupMultiplier();

		int dimensions[] = new int[] {500,1000,2000};
		for(int n:dimensions) {
			System.out.println(""+n);
			double matrix1[][] = MatrixGenerator.generate(n, n);
			double matrix2[][] = MatrixGenerator.generate(n, n);
			double resultSerial[][] = new double[matrix1.length][matrix2[0].length];
			for (IMatrixMultiplier multipler : multiplers) {
				System.out.printf(multipler.getName());
				Date start = new Date();
				multipler.multiply(matrix1, matrix2, resultSerial);
				Date end = new Date();
				System.out.printf(": %d \n", end.getTime() - start.getTime());
			}
		}
	}
}
