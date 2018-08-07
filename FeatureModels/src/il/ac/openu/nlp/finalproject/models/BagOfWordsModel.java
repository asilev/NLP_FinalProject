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
//		List<ArrayList<Double>> authorWordsArray;
		authorWordsArray = new double[authorBagOfWords.size()][];
//		authorWordsArray = new ArrayList<>();
//		for (int i=0;i<authorBagOfWords.size();++i) {
//			authorWordsArray.add(new ArrayList<Double>());
//		}
		for (Map.Entry<String, BagOfWords> e : authorBagOfWords.entrySet()) {
			BagOfWords bagOfWords = e.getValue();
			if (!authorIndex.containsKey(e.getKey())) {
				authorIndex.put(e.getKey(), newAuthorIndex++);
			}
			System.out.println(newAuthorIndex);
			System.out.println("Allocating "+(newWordIndex+e.getValue().size()));
			authorWordsArray[authorIndex.get(e.getKey())] = new double[newWordIndex+e.getValue().size()];
//			ArrayList<Double> newDoubleList = new ArrayList<Double>(newWordIndex+e.getValue().size());
//			authorWordsArray.set(authorIndex.get(e.getKey()), new ArrayList<Double>());
//			for (int i=0; i<newWordIndex+e.getValue().size();++i) {
//				authorWordsArray.get(authorIndex.get(e.getKey())).add(new Double(0.0));
//			}
			for (String word : bagOfWords.getWords()) {
				if (!wordsIndex.containsKey(word)) {
					wordsIndex.put(word, newWordIndex++);
				}
				System.out.println("word "+wordsIndex.get(word));
				authorWordsArray[authorIndex.get(e.getKey())][wordsIndex.get(word)] = bagOfWords.getNumOfOccurances(word);
//				authorWordsArray.get(authorIndex.get(e.getKey())).set(wordsIndex.get(word), new Double(bagOfWords.getNumOfOccurances(word)));
			}
		}		
	}
}
