package il.ac.openu.nlp.finalproject.models.bagofwords;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BagOfWords {
	private Map<String, Integer> bagOfWords;
	
	public BagOfWords() {
		bagOfWords = new HashMap<>();
	}
	
	public Integer getNumOfOccurances(String word) {
		return bagOfWords.get(word);
	}
	
	public void addWord(String word) {
		if (bagOfWords.containsKey(word)) {
			bagOfWords.put(word, bagOfWords.get(word)+1);
		}
		else {
			bagOfWords.put(word, 1);
		}
	}
	
	public Set<String> getWords() {
		return bagOfWords.keySet();
	}
	
	public int size() {
		return bagOfWords.size();
	}
}
