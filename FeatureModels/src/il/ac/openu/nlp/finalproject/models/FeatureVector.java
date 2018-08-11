package il.ac.openu.nlp.finalproject.models;

import java.util.HashMap;

public class FeatureVector<K> extends HashMap<K, Double> {
	private static final long serialVersionUID = 1L;

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
}
