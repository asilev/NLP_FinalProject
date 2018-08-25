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
	private static ArrayList<FeatureMarker> fml;	// A list of FeatureMarkers
	
	public static List<TaggedFeatureVector<String>> buildFeatureMarker(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) throws IOException {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			for (List<MorphemeRecord> tweet : user.getValue()) {
				
				/*
				FeatureVector<String> bagOfWords = new FeatureVector<String>(ZERO_INDEX);
				for (MorphemeRecord morpheme : tweet) {
					bagOfWords.put(morpheme.originalWord, bagOfWords.get(morpheme)+1);
				}*/
				
				for (FeatureMarker fm : fml)
				{
					// Traverse the list of features, for each call the feature implementation/test function
					// For now - just one feature of "num_of_morphemes"
					FeatureVector<String> numOfMorphemes = new FeatureVector<String>(ZERO_INDEX);
					String sOriginalTweet = "";
					for (MorphemeRecord morpheme : tweet) {
						sOriginalTweet += morpheme.originalWord + " ";
					}
					numOfMorphemes.put(fm.getName() + ": [" + sOriginalTweet + "]", (double)tweet.size());
					usersTweetsVector.add(new TaggedFeatureVector<>(numOfMorphemes, user.getKey()));
				}
			}
		}
		return usersTweetsVector;
	}
	
	public static void main (String[] args) throws Exception
	{
		// Simple test function to test "buildFeatureMarkerTweetSize"
		
		StructuredDataReader dataReader = new StructuredDataReader(args[0], args[1]);
		System.out.println("Building FeatureMarker");
		Map<String, List<List<MorphemeRecord>>> data = dataReader.readStructuredData();
		fml = new ArrayList<FeatureMarker>();
		fml.add(new FeatureMarker("num_of_morphemes"));
		trainingData = buildFeatureMarker(data, ZERO_INDEX);
	}
}
