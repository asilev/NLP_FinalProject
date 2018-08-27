package il.ac.openu.nlp.finalproject.models.svm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.AttributeList;
import il.ac.openu.nlp.finalproject.models.KeyMapper;
import il.ac.openu.nlp.finalproject.models.MorphemeRecord;
import il.ac.openu.nlp.finalproject.models.StructuredDataReader;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;
import il.ac.openu.nlp.finalproject.models.bagofwords.BagOfWordsModel;

public class SupportVectorMachine {
	public static void prepareSvmInputsFromBagOfWords(String structuredDataPath, String encoding, String ZERO_INDEX, String svmInputFilename, String wordsMapperFilename) throws IOException {
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		Map<String, List<List<MorphemeRecord>>> structuredData = dataReader.readStructuredData("gold");
		List<TaggedFeatureVector<String>> trainingData = BagOfWordsModel.buildAuthorBagOfWords(structuredData, ZERO_INDEX);
		BufferedWriter bw = new BufferedWriter(new FileWriter(svmInputFilename));
		AttributeList<String> attributes = new AttributeList<>();
		for (TaggedFeatureVector<String> taggedFeatureVector: trainingData) {
			bw.write(((Integer)taggedFeatureVector.getTag().hashCode()).toString());
			
			for (AttributeList<String>.Attribute a : attributes.getSortedList(taggedFeatureVector.getFeatureVector())) {
				bw.write(" "+a.getKey()+":"+a.getValue());
			}
			bw.write("\n");
		}
		bw.close();
		
		bw = new BufferedWriter(new FileWriter(wordsMapperFilename));
		Map<String, Integer> keyMapper = attributes.getKeyMapper().getMapper();
		for (Map.Entry<String, Integer> e : keyMapper.entrySet()) {
			bw.write(e.getKey()+"\t"+e.getValue()+"\n");
		}
		bw.close();
	}
	
	public static void prepareSvmEvaluationsFromBagOfWords(String structuredDataPath, String encoding, String ZERO_INDEX, String svmInputFilename, String wordsMapperFilename) throws IOException {
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		Map<String, List<List<MorphemeRecord>>> structuredData = dataReader.readStructuredData("test");
		List<TaggedFeatureVector<String>> evaluationData = BagOfWordsModel.buildAuthorBagOfWords(structuredData, ZERO_INDEX);
		Map<String, Integer> mapper = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(wordsMapperFilename));
		String line;
		while ((line = br.readLine())!=null) {
			String[] entry = line.split("\t");
				if (entry.length==2) {
					mapper.put(entry[0], Integer.parseInt(entry[1]));
			}
		}
		br.close();
		AttributeList<String> attributes = new AttributeList<>();
		KeyMapper<String> keyMapper = new KeyMapper<>();
		keyMapper.setMapper(mapper);
		attributes.setKeyMapper(keyMapper);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(svmInputFilename));
		for (TaggedFeatureVector<String> taggedFeatureVector: evaluationData) {
			bw.write(((Integer)taggedFeatureVector.getTag().hashCode()).toString());
			for (AttributeList<String>.Attribute a : attributes.getSortedList(taggedFeatureVector.getFeatureVector())) {
				bw.write(" "+a.getKey()+":"+a.getValue());
			}
			bw.write("\n");
			bw.flush();
		}
		bw.close();
	}
	
	public static void main(String[] args) throws IOException {
		prepareSvmInputsFromBagOfWords("C:\\Users\\asil\\Workspaces\\FinalProject\\TwitterReader\\OutputFromYap_10K\\", "UTF8", null, "training", "wordsMapper");
		prepareSvmEvaluationsFromBagOfWords("C:\\Users\\asil\\Workspaces\\FinalProject\\TwitterReader\\OutputFromYap_10K\\", "UTF8", null, "evaluation", "wordsMapper");
	}
}
