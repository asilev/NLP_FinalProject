package il.ac.openu.nlp.finalproject.models;

import java.util.HashSet;
import java.util.Set;

public class FeatureModel {
	public static enum FeatureType {
		BagOfWords,
		StemmedBagOfWords,
		Bigrams,
		PosBagOfWords,
		NumberOfMorphemes,
		AverageWordSize,
		NumberOfCharacters,
		NumberOfPucntuationMarks,
		AverageNumberOfPunctuationMarks,
		PosUsage,
		AverageSentenceSize,
		NumOfLongWords	
	}
	public Set<FeatureType> features = new HashSet<>();
}
