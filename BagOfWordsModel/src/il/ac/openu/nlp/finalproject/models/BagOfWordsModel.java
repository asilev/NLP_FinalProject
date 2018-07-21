package il.ac.openu.nlp.finalproject.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BagOfWordsModel {
	private static DataReader dataReader;
	private static Map<String, Integer> wordsIndex = new HashMap<>();
	private static Map<String, Integer> authorIndex = new HashMap<>();
	private static int newAuthorIndex = 0;
	private static int newWordIndex = 0;
	
	public static void main(String[] args) throws IOException {
		dataReader = new DataReader(args[0], "UTF8");
		Map<String, BagOfWords> authorBagOfWords = dataReader.setAuthorBagOfWords();
		double[][] authorWordsArray;
		authorWordsArray = new double[authorBagOfWords.size()][];
		for (Map.Entry<String, BagOfWords> e : authorBagOfWords.entrySet()) {
			BagOfWords bagOfWords = e.getValue();
			if (!authorIndex.containsKey(e.getKey())) {
				authorIndex.put(e.getKey(), newAuthorIndex++);
			}
			System.out.println(newAuthorIndex);
			authorWordsArray[authorIndex.get(e.getKey())] = new double[newWordIndex+e.getValue().size()];
			
			for (String word : bagOfWords.getWords()) {
				if (!wordsIndex.containsKey(word)) {
					wordsIndex.put(word, newWordIndex++);
				}
				System.out.println("word "+newWordIndex);
				authorWordsArray[authorIndex.get(e.getKey())][wordsIndex.get(word)] = bagOfWords.getNumOfOccurances(word);
			}
		}		
	}
}
