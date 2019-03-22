package parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import common.CountTag;
import common.Neighbor;
import common.Sample;
import common.Util;

public class SingleParallelKNN <T extends Sample> extends common.KNN<T> {
		
	private ArrayList<Neighbor> buildNeighbors(ArrayList<T> traingSet, T v, int k) {
		ArrayList<Neighbor> neighbors = initNeighbor(traingSet.size());
		
		java.util.function.IntFunction<Runnable> buildRunnable = (idxInput) -> {
			int idx = idxInput;
			return ()->{
				T train = traingSet.get(idx);
				neighbors.set(idx, new Neighbor(train, v));
			};
		};
	
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for(int idx = 0;idx<traingSet.size();idx++) {
			executor.execute(buildRunnable.apply(idx));
		}
		executor.shutdown();
		
		try {
			executor.awaitTermination(1000, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return neighbors;
	}

	@Override
	public String getName() {
		return "Single parallel";
	}
	
	@Override
	public String classify(ArrayList<T> traingSet, T v, int k){
		ArrayList<Neighbor> neighbors = buildNeighbors(traingSet, v, k);
		List<Neighbor> knearest = Util.selectKLeast(neighbors, k);
		Map<String, CountTag> countTags = buildCountTags(knearest);
		CountTag maxCT = null;
		for (CountTag ct : countTags.values()) {
			if (maxCT == null || maxCT.compareTo(ct) < 0) {
				maxCT = ct;
			}
		}
		return maxCT != null ? maxCT.getTag() : null;
	}
}

