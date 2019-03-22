package serial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.CountTag;
import common.Neighbor;
import common.Sample;
import common.Util;

public class SerialKNN <T extends Sample> extends common.KNN<T> {
	private ArrayList<Neighbor> buildNeighbors(List<T> traingingSet, T v, int k) {
		ArrayList<Neighbor> neighbors = initNeighbor(traingingSet.size());
		
		int idx = 0;
		for (Sample train : traingingSet) {
			neighbors.set(idx++, new Neighbor(train, v));
		}
		return neighbors;
	}

	@Override
	public String getName() {
		return "Serial parallel";
	}

	@Override
	public String classify(ArrayList<T> traingingSet, T v, int k){
		ArrayList<Neighbor> neighbors = buildNeighbors(traingingSet, v, k);
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
