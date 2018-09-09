package il.ac.openu.nlp.finalproject.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.FeatureModel.FeatureType;
import il.ac.openu.nlp.finalproject.models.bagofwords.BagOfWordsModel;
import il.ac.openu.nlp.finalproject.models.featuremarker.FeatureMarkerModel;

public class FeatureSelector {
	public static List<TaggedFeatureVector<String>> buildFeaturesVector(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX, FeatureModel featureModel) {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		List<List<TaggedFeatureVector<String>>> featureVectors = new ArrayList<>();
		if (featureModel.features.contains(FeatureType.BagOfWords)) {
			featureVectors.add(BagOfWordsModel.buildAuthorBagOfWords(userTweets, ZERO_INDEX));
		}
		if (featureModel.features.contains(FeatureType.Bigrams)) {
			featureVectors.add(BagOfWordsModel.buildAuthorBagOf2GramsWords(userTweets, ZERO_INDEX));
		}
		if (featureModel.features.contains(FeatureType.PosBagOfWords)) {
//			featureVectors.add(BagOfWordsModel.buildAuthorBagOfPosWords(userTweets, ZERO_INDES));
		}
		if (featureModel.features.contains(FeatureType.StemmedBagOfWords)) {
			featureVectors.add(BagOfWordsModel.buildAuthorBagOfStemmedWords(userTweets, ZERO_INDEX));
		}
		
		featureVectors.add(FeatureMarkerModel.buildFeatureMarker(userTweets, ZERO_INDEX, featureModel));

		for (List<TaggedFeatureVector<String>> listOfTaggedFeatureVector : featureVectors) {
			int i=0;
			for (TaggedFeatureVector<String> taggedFeatureVector : listOfTaggedFeatureVector) {
				if (usersTweetsVector.size() <= i) {
					usersTweetsVector.add(taggedFeatureVector);
				}
				else {
					usersTweetsVector.get(i).getFeatureVector().putAll(taggedFeatureVector.getFeatureVector());
				}
				++i;
			}
		}
		return usersTweetsVector;
	}
}
