package il.ac.openu.nlp.finalproject.models.all_features;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.FeatureVector;
import il.ac.openu.nlp.finalproject.models.MorphemeRecord;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;
import il.ac.openu.nlp.finalproject.models.bagofwords.BagOfWordsModel;
import il.ac.openu.nlp.finalproject.models.featuremarker.FeatureMarkerModel;

public class AllFeaturesModel {
	public static List<TaggedFeatureVector<String>> buildAllFeatures(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		List<TaggedFeatureVector<String>> usersTweetsVectorForBagOfWords = BagOfWordsModel.buildAuthorBagOfWords(userTweets, ZERO_INDEX);
		List<TaggedFeatureVector<String>> usersTweetsVectorForFeatureMarker = FeatureMarkerModel.buildFeatureMarker(userTweets, ZERO_INDEX);
		for (int i=0;i<usersTweetsVectorForBagOfWords.size();++i) {
			String tag = usersTweetsVectorForBagOfWords.get(i).getTag();
			FeatureVector<String> featureVector = new FeatureVector<String>(ZERO_INDEX);
			featureVector.putAll(usersTweetsVectorForBagOfWords.get(i).getFeatureVector());
			featureVector.putAll(usersTweetsVectorForFeatureMarker.get(i).getFeatureVector());
			usersTweetsVector.add(new TaggedFeatureVector<>(featureVector, tag));
		}
		return usersTweetsVector;
	}
}
