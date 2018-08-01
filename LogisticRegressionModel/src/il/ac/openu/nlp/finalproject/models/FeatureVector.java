package il.ac.openu.nlp.finalproject.models;

import java.util.HashMap;
import java.util.Map;

public class FeatureVector<K> {
	private Map<K, Double> keyValueMap = new HashMap<>();
	
	public double getFeature(K index) {
		return keyValueMap.get(index)==null?(0.0):(double) keyValueMap.get(index);
	}
	
	public void putFeature(K index, Double value) {
		keyValueMap.put(index, value);
	}
}
