package il.ac.openu.nlp.finalproject.models.featuremarker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.FeatureVector;
import il.ac.openu.nlp.finalproject.models.MorphemeRecord;
import il.ac.openu.nlp.finalproject.models.StructuredDataReader;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;
import il.ac.openu.nlp.finalproject.models.bagofwords.BagOfWordsModel;

public class FeatureMarkerModel {
	private static List<TaggedFeatureVector<String>> trainingData;
	private static String ZERO_INDEX;
	
	public static List<TaggedFeatureVector<String>> buildFeatureMarker(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) throws IOException {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			for (List<MorphemeRecord> tweet : user.getValue()) {
				
				FeatureVector<String> numOfMorphemes = getTweetsSize(ZERO_INDEX, tweet);
				usersTweetsVector.add(new TaggedFeatureVector<>(numOfMorphemes, user.getKey()));
				
				FeatureVector<String> averageWordSize = getAverageWordSize(ZERO_INDEX, tweet);
				usersTweetsVector.add(new TaggedFeatureVector<>(averageWordSize, user.getKey()));
				
				FeatureVector<String> numOfPunctMarks = getNumOfPunctuationMarks(ZERO_INDEX, tweet);
				usersTweetsVector.add(new TaggedFeatureVector<>(numOfPunctMarks, user.getKey()));
			}
		}
		return usersTweetsVector;
	}

	private static FeatureVector<String> getAverageWordSize(String ZERO_INDEX, List<MorphemeRecord> tweet) {
		// TODO Auto-generated method stub
		// Run over the list of morphemes, sum the size of each morpheme, and divide by the number of words in the tweet (number of morphemes)
		
		double accSize = 0;
		double numOfWords = 0;
		FeatureVector<String> numOfMorphemes = new FeatureVector<String>(ZERO_INDEX);
		
		for (MorphemeRecord morpheme : tweet) {
			accSize += morpheme.originalWord.length();
			numOfWords++;
		}
		numOfMorphemes.put("avgWordSize", accSize/numOfWords);
		return numOfMorphemes;
	}

	private static FeatureVector<String> getNumOfPunctuationMarks(String ZERO_INDEX, List<MorphemeRecord> tweet) {
		// TODO Auto-generated method stub
		// Run over the list of morphemes, sum the size of each morpheme, and divide by the number of words in the tweet (number of morphemes)
		
		double numOfPunctMarks = 0;
		FeatureVector<String> numOfMorphemes = new FeatureVector<String>(ZERO_INDEX);
		
		for (MorphemeRecord morpheme : tweet) {
			if (morpheme.isPunctuationMark == true)
				numOfPunctMarks++;
		}
		numOfMorphemes.put("numOfPunctuationMarks", numOfPunctMarks);
		return numOfMorphemes;
	}
	
	private static FeatureVector<String> getTweetsSize(String ZERO_INDEX, List<MorphemeRecord> tweet) {
		// For now - just one feature of "num_of_morphemes"
		FeatureVector<String> numOfMorphemes = new FeatureVector<String>(ZERO_INDEX);
		numOfMorphemes.put("numOfMorphemes", (double)tweet.size());
		return numOfMorphemes;
	}
	
//	public static void buildFeatureMarker(String[] args) throws Exception
//	{
//		// Simple test function to test "buildFeatureMarkerTweetSize"
//		
//		StructuredDataReader dataReader = new StructuredDataReader(args[0], args[1]);
//		System.out.println("Building FeatureMarker");
//		Map<String, List<List<MorphemeRecord>>> data = dataReader.readStructuredData("gold");
//		trainingData = buildFeatureMarker(data, ZERO_INDEX);
//	}
}
