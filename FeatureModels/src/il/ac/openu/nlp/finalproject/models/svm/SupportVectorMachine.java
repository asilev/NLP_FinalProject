package il.ac.openu.nlp.finalproject.models.svm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.FeatureVector;
import il.ac.openu.nlp.finalproject.models.FeatureVector.Attribute;
import il.ac.openu.nlp.finalproject.models.StructuredDataReader;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;

public class SupportVectorMachine {
	public static void prepareSvmInputsFromBagOfWords(String structuredDataPath, String encoding, String ZERO_INDEX, String outputFilename) throws IOException {
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		List<TaggedFeatureVector<String>> trainingData = dataReader.buildAuthorBagOfWords(ZERO_INDEX);
		HashMap<String, Integer> stringMapper = new HashMap<>();
		Integer lastIndex = 0;
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilename));
		for (TaggedFeatureVector<String> taggedFeatureVector: trainingData) {
			bw.write(((Integer)taggedFeatureVector.getTag().hashCode()).toString());
			
			List<Attribute> attributesList = new ArrayList<>();
			for (Map.Entry<String, Double> fv : taggedFeatureVector.getFeatureVector().entrySet()) {
				if (!stringMapper.containsKey(fv.getKey())) {
					stringMapper.put(fv.getKey(), ++lastIndex);
				}
				attributesList.add(taggedFeatureVector.getFeatureVector().new Attribute(stringMapper.get(fv.getKey()),fv.getValue()));
			}
			Collections.sort(attributesList);
			for (Attribute a : attributesList) {
				bw.write(" "+a.getKey()+":"+a.getValue());
			}
			bw.write("\n");
		}
		bw.close();
	}
	
	public static void main(String[] args) throws IOException {
		prepareSvmInputsFromBagOfWords("C:\\Users\\asil\\Workspaces\\FinalProject\\TwitterReader\\OutputFromYap\\", "UTF8", "ZERO_INDEX", "outputFilename");
	}
}
