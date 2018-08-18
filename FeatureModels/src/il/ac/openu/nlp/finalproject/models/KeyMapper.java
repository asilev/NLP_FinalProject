package il.ac.openu.nlp.finalproject.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class KeyMapper<K> implements Serializable{
	private static final long serialVersionUID = 1L;
	private Map<K, Integer> mapper;
	private int lastIndexInUse = 0;
	
	public KeyMapper() {
		mapper = new HashMap<>();
	}
	
	public void put(K key) {
		if (!mapper.containsKey(key)) {
			mapper.put(key, ++lastIndexInUse);
		}
	}
	
	public Integer get(K key) {
		return mapper.get(key);
	}
	
	public Map<K, Integer> getMapper() {
		return mapper;
	}
	
	public void setMapper(Map<K, Integer> mapper) {
		this.mapper = mapper;
		this.lastIndexInUse = 0;
		for (Integer index : mapper.values()) {
			if (index>lastIndexInUse) {
				lastIndexInUse = index;
			}
		}
	}
}
