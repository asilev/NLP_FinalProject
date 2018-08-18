package il.ac.openu.nlp.finalproject.models.svm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.AttributeList;
import il.ac.openu.nlp.finalproject.models.MorphemeRecord;
import il.ac.openu.nlp.finalproject.models.StructuredDataReader;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;
import il.ac.openu.nlp.finalproject.models.bagofwords.BagOfWordsModel;

public class SupportVectorMachine {
	public static void prepareSvmInputsFromBagOfWords(String structuredDataPath, String encoding, String ZERO_INDEX, String outputFilename, String wordsMapper) throws IOException {
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		Map<String, List<List<MorphemeRecord>>> structuredData = dataReader.readStructuredData();
		List<TaggedFeatureVector<String>> trainingData = BagOfWordsModel.buildAuthorBagOfWords(structuredData, ZERO_INDEX);
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilename));
		AttributeList<String> attributes = new AttributeList<>();
		for (TaggedFeatureVector<String> taggedFeatureVector: trainingData) {
			bw.write(((Integer)taggedFeatureVector.getTag().hashCode()).toString());
			
			for (AttributeList<String>.Attribute a : attributes.getSortedList(taggedFeatureVector.getFeatureVector())) {
				bw.write(" "+a.getKey()+":"+a.getValue());
			}
			bw.write("\n");
		}
		bw.close();
		
		bw = new BufferedWriter(new FileWriter(wordsMapper));
		Map<String, Integer> keyMapper = attributes.getKeyMapper().getMapper();
		for (Map.Entry<String, Integer> e : keyMapper.entrySet()) {
			bw.write(e.getKey()+"\t"+e.getValue()+"\n");
		}
	}
	
	public static void main(String[] args) throws IOException {
		prepareSvmInputsFromBagOfWords("C:\\Users\\asil\\Workspaces\\FinalProject\\TwitterReader\\OutputFromYap\\", "UTF8", "ZERO_INDEX", "outputFilename", "wordsMapper");
	}
}
