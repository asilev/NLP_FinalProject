package il.ac.openu.nlp.finalproject.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AttributeList<K> {
	KeyMapper<K> keyMapper = new KeyMapper<>();
	public class Attribute implements Comparable<Attribute > {
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
		public int compareTo(Attribute o) {
			return ((Integer)key).compareTo(o.getKey());
		}
	}

	public List<Attribute> getSortedList(Map<K, Double> map) {
		List<Attribute> ret = new ArrayList<>();
		for (Map.Entry<K, Double> e : map.entrySet()) {
			keyMapper.put(e.getKey());
			ret.add(new Attribute(keyMapper.get(e.getKey()), e.getValue()));
		}
		Collections.sort(ret);
		return ret;
	}
	public void setKeyMapper(KeyMapper<K> keyMapper) {
		this.keyMapper = keyMapper;
	}
	
	public KeyMapper<K> getKeyMapper() {
		return keyMapper;
	}
}