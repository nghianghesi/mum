package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import bank.BankMarketing;

public class Util {
	public static double distance(Iterable<? extends Double> U, Iterable<? extends Double> V) {
		double d = 0;
		Iterator<? extends Double> v = V.iterator();
		Iterator<? extends Double> u = U.iterator();
		while (u.hasNext() || v.hasNext()) {
			double uvalue= (u.hasNext() ? u.next() : 0);
			double vvalue= (v.hasNext() ? v.next() : 0);
			d += Math.pow(uvalue - vvalue, 2);
		}
		return Math.sqrt(d);
	}
	public static <T extends Comparable<T>> List<T> selectKLeast(ArrayList<T> neighbors, int k) {
		return selectKLeast(neighbors, k, 0, neighbors.size());
	}
	public static <T extends Comparable<T>> List<T> selectKLeast(ArrayList<T> neighbors, int k, int start, int end) {		
		List<T> res = new LinkedList<>();
		while(start<end) {
			if (end - start <= k) {
				res.addAll(neighbors.subList(start, end));
				return res;
			}	
			T pivot = neighbors.get((end + start) / 2);
			int i, j;
			for (i = start, j = end - 1; i < j;) {
				for (; i < j && neighbors.get(i).compareTo(pivot) < 0; i++);
				for (; i < j && neighbors.get(j).compareTo(pivot) > 0; j--);
				for (; i < j && neighbors.get(i).compareTo(pivot) == 0 && neighbors.get(j).compareTo(pivot) == 0; i++,j--);
				if (i < j) { // swap
					T t = neighbors.get(i);
					neighbors.set(i,neighbors.get(j));
					neighbors.set(j, t);
					i++;
					j--;
				}
			}
			
			if (i - start >= k) {
				end = i;
			} else {
				res.addAll(neighbors.subList(start, i));
				k=k-(i-start);
				start=i;
			}			
		}
		return res;
	}
	
	public static ArrayList<Sample> readBankData(String dataPath){
		Path file = Paths.get(dataPath);
		ArrayList<Sample> dataSet = new ArrayList<>();
		try (InputStream in = Files.newInputStream(file);
			    BufferedReader reader =
			      new BufferedReader(new InputStreamReader(in))) {
			    String line = null;
			    while ((line = reader.readLine()) != null) {
			    	String data[]=line.split(";");
			    	BankMarketing dataObject=new BankMarketing();
			    	dataObject.setData(data);
			    	dataSet.add(new Sample(dataObject.getTag(), dataObject.getExample()));
			    }
			} catch (IOException x) {
			  x.printStackTrace();
			} catch (Exception e) {
			  e.printStackTrace();
			}
		return dataSet;
	}
}
