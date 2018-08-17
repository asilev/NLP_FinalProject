package il.ac.openu.nlp.finalproject.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureVector<K> extends HashMap<K, Double> {
	private static final long serialVersionUID = 1L;
	private Map<K, Integer> stringMapper = new HashMap<>();
	private Integer lastKeyInMap = 0;
	public FeatureVector(K zeroIndex) {
		super();
		this.put(zeroIndex, 1.0);
	}
	
	@Override
	public Double get(Object key) {
		if (containsKey(key)) {
			return super.get(key);
		}
		else {
			return 0.0;
		}
	}
	public class Attribute implements Comparable<Attribute >{
		private int key;
		private double value;
		
		public int getKey() {
			return key;
		}
		public double getValue() {
			return value;
		}
		public Attribute(int key, double value) {
			this.key = key;
			this.value = value;
		}
		@Override
		public int compareTo(FeatureVector<K>.Attribute o) {
			return ((Integer)key).compareTo(o.getKey());
		}
	}
	
	public List<Attribute> getSortedList() {
		
		List<Attribute> ret = new ArrayList<>();
		for (Map.Entry<K, Double> e : this.entrySet()) {
			if (!stringMapper.containsKey(e.getKey())) {
				stringMapper.put(e.getKey(), ++lastKeyInMap);
			}
			ret.add(new Attribute(stringMapper.get(e.getKey()), e.getValue()));
		}
		Collections.sort(ret);
		return ret;
	}
}
