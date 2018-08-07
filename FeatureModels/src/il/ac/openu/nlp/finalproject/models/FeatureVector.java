package il.ac.openu.nlp.finalproject.models;

import java.util.HashMap;

public class FeatureVector<K> extends HashMap<K, Double> {
	private static final long serialVersionUID = 1L;

//	public FeatureVector(K zeroIndex) {
//		super();
//		this.put(zeroIndex, 0.0);
//	}
	
	@Override
	public Double get(Object key) {
		Double returnValue = super.get(key);
		if (returnValue==null) {
			returnValue=0.0;
		}
		return returnValue;
	}
}
