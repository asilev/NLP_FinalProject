package il.ac.openu.nlp.finalproject.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.FeatureModel.FeatureType;
import il.ac.openu.nlp.finalproject.models.bagofwords.BagOfWordsModel;

public class FeatureSelector {
	public static List<TaggedFeatureVector<String>> buildFeaturesVector(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX, FeatureModel featureModel) {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		List<List<TaggedFeatureVector<String>>> featureVectors = new ArrayList<>();
//		FeatureVector<String> featureVector;
		for (FeatureType feature : featureModel.features) {
			switch (feature) {
				case BagOfWords:
					featureVectors.add(BagOfWordsModel.buildAuthorBagOfWords(userTweets, ZERO_INDEX));
					break;
				case AverageNumberOfPunctuationMarks:
					break;
				case AverageSentenceSize:
					break;
				case AverageWordSize:
					break;
				case Bigrams:
					break;
				case NumOfLongWords:
					break;
				case NumberOfCharacters:
					break;
				case NumberOfMorphemes:
					break;
				case NumberOfPucntuationMarks:
					break;
				case PosBagOfWords:
					break;
				case PosUsage:
					break;
				case StemmedBagOfWords:
					break;
				default:
					break;						
			}
		}

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
