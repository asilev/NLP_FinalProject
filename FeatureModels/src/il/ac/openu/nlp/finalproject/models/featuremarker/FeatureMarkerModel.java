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
	
	public static List<TaggedFeatureVector<String>> buildFeatureMarkerTweetSize(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) throws IOException {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		FeatureMarker fm = new FeatureMarker("tweet_size", 10);

		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			for (List<MorphemeRecord> tweet : user.getValue()) {
				FeatureVector<String> wordsPerTweet = new FeatureVector<String>(ZERO_INDEX);
				
				// Check if the tweet's length if equal or larger to the given length in the FeatureMarker
				if (tweet.size() >= (int)fm.getValue()) {
					// If yes, "re-build" the tweet itself since the variable 'tweet' contains discreet morphemes
					for (MorphemeRecord morpheme : tweet) {
						wordsPerTweet.put(morpheme.originalWord, 1.0);
					}
					usersTweetsVector.add(new TaggedFeatureVector<>(wordsPerTweet, user.getKey()));
				}
			}
		}
		
		// Return all the tweets from all the users that comply with the condition of FeatureMarker
		return usersTweetsVector;
	}
	
	public static void main (String[] args) throws Exception
	{
		// Simple test function to test "buildFeatureMarkerTweetSize"
		StructuredDataReader dataReader = new StructuredDataReader(args[0], args[1]);
		System.out.println("Building FeatureMarker");
		Map<String, List<List<MorphemeRecord>>> data = dataReader.readStructuredData("gold");
		trainingData = buildFeatureMarkerTweetSize(data, ZERO_INDEX);
	}
}
