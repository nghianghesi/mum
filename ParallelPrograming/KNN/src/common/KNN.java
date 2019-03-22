package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class KNN <T extends Sample> {

	protected ArrayList<Neighbor> initNeighbor(int size){
		ArrayList<Neighbor> neighbors = new ArrayList<>();
		neighbors.ensureCapacity(size);
		for(int i=0;i<size;i++) {
			neighbors.add(null);
		}
		return neighbors;
	}
	
	protected Map<String, CountTag> buildCountTags(List<Neighbor> knearest) {
		Map<String, CountTag> countTags = new HashMap<String, CountTag>();
		for (Neighbor nb : knearest) {
			if (!countTags.containsKey(nb.getTag())) {
				countTags.put(nb.getTag(), new CountTag(nb.getTag()));
			} else {
				countTags.get(nb.getTag()).Increase();
			}
		}
		return countTags;
	}
	public abstract String classify(ArrayList<T> traingSet, T v, int k);
	public abstract String getName() ;
}
