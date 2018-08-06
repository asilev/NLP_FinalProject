package il.ac.openu.nlp.finalproject.models;

import java.util.HashMap;
import java.util.Map;

public class FeatureVector<K> extends HashMap<K, Double> {
	private static final long serialVersionUID = 1L;
	private Map<K, Double> keyValueMap = new HashMap<>();
	
	@Override
	public Double get(Object key) {
		Double returnValue = super.get(key);
		if (returnValue==null) {
			returnValue=0.0;
		}
		return returnValue;
	}
}
