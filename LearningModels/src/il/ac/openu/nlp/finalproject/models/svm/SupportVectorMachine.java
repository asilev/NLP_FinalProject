package il.ac.openu.nlp.finalproject.models.svm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.AttributeList;
import il.ac.openu.nlp.finalproject.models.FeatureModel;
import il.ac.openu.nlp.finalproject.models.FeatureSelector;
import il.ac.openu.nlp.finalproject.models.KeyMapper;
import il.ac.openu.nlp.finalproject.models.MorphemeRecord;
import il.ac.openu.nlp.finalproject.models.StructuredDataReader;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;
import il.ac.openu.nlp.finalproject.models.FeatureModel.FeatureType;
import il.ac.openu.nlp.finalproject.models.all_features.AllFeaturesModel;
import il.ac.openu.nlp.finalproject.models.bagofwords.BagOfWordsModel;
import il.ac.openu.nlp.finalproject.models.featuremarker.FeatureMarkerModel;

public class SupportVectorMachine {
	public static void prepareSvmInputsFromBagOfWords(String structuredDataPath, String encoding, String ZERO_INDEX, String svmInputFilename, String wordsMapperFilename, int numOfAuthors) throws IOException {
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		Map<String, List<List<MorphemeRecord>>> structuredData = dataReader.readStructuredData("gold", numOfAuthors);
		List<TaggedFeatureVector<String>> trainingData = BagOfWordsModel.buildAuthorBagOfWords(structuredData, ZERO_INDEX);
		BufferedWriter bw = new BufferedWriter(new FileWriter(svmInputFilename));
		AttributeList<String> attributes = new AttributeList<>();
		for (TaggedFeatureVector<String> taggedFeatureVector: trainingData) {
			bw.write(taggedFeatureVector.getTag());
			
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
	
	public static void prepareSvmEvaluationsFromBagOfWords(String structuredDataPath, String encoding, String ZERO_INDEX, String svmInputFilename, String wordsMapperFilename, int numOfAuthors) throws IOException {
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		Map<String, List<List<MorphemeRecord>>> structuredData = dataReader.readStructuredData("test", numOfAuthors);
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
			bw.write(taggedFeatureVector.getTag());
			for (AttributeList<String>.Attribute a : attributes.getSortedList(taggedFeatureVector.getFeatureVector())) {
				bw.write(" "+a.getKey()+":"+a.getValue());
			}
			bw.write("\n");
			bw.flush();
		}
		bw.close();
	}
	
	public static void prepareSvmInputsFromFeatureMarker(String structuredDataPath, String encoding, String ZERO_INDEX, String svmInputFilename, String wordsMapperFilename, int numOfAuthors) throws IOException {
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		Map<String, List<List<MorphemeRecord>>> structuredData = dataReader.readStructuredData("gold", numOfAuthors);
		FeatureModel model = new FeatureModel();
		model.features.addAll(Arrays.asList(FeatureType.values()));
		List<TaggedFeatureVector<String>> trainingData = FeatureMarkerModel.buildFeatureMarker(structuredData, null, model);
		BufferedWriter bw = new BufferedWriter(new FileWriter(svmInputFilename));
		AttributeList<String> attributes = new AttributeList<>();
		for (TaggedFeatureVector<String> taggedFeatureVector: trainingData) {
			bw.write(taggedFeatureVector.getTag());
			
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

	public static void prepareSvmInputsFromAllFeatures(String structuredDataPath, String encoding, String ZERO_INDEX, String svmInputFilename, String wordsMapperFilename, int numOfAuthors) throws Exception {
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		Map<String, List<List<MorphemeRecord>>> structuredData = dataReader.readStructuredData("gold", numOfAuthors);
		List<TaggedFeatureVector<String>> trainingData = AllFeaturesModel.buildAllFeatures(structuredData, null);
		BufferedWriter bw = new BufferedWriter(new FileWriter(svmInputFilename));
		AttributeList<String> attributes = new AttributeList<>();
		for (TaggedFeatureVector<String> taggedFeatureVector: trainingData) {
			bw.write(taggedFeatureVector.getTag());
			
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
	
	public static void prepareSvmEvaluationsFromFeatureMarker(String structuredDataPath, String encoding, String ZERO_INDEX, String svmInputFilename, String wordsMapperFilename, int numOfAuthors) throws IOException {
		FeatureModel model = new FeatureModel();
		model.features.addAll(Arrays.asList(FeatureType.values()));
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		Map<String, List<List<MorphemeRecord>>> structuredData = dataReader.readStructuredData("test", numOfAuthors);
		List<TaggedFeatureVector<String>> evaluationData = FeatureMarkerModel.buildFeatureMarker(structuredData, null, model);
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
			bw.write(taggedFeatureVector.getTag());
			for (AttributeList<String>.Attribute a : attributes.getSortedList(taggedFeatureVector.getFeatureVector())) {
				bw.write(" "+a.getKey()+":"+a.getValue());
			}
			bw.write("\n");
			bw.flush();
		}
		bw.close();
		
	}
	
	public static void prepareSvmEvaluationsFromAllFeatures(String structuredDataPath, String encoding, String ZERO_INDEX, String svmInputFilename, String wordsMapperFilename, int numOfAuthors) throws Exception {
		
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		Map<String, List<List<MorphemeRecord>>> structuredData = dataReader.readStructuredData("test", numOfAuthors);
		List<TaggedFeatureVector<String>> evaluationData = AllFeaturesModel.buildAllFeatures(structuredData, null);
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
			bw.write(taggedFeatureVector.getTag());
			for (AttributeList<String>.Attribute a : attributes.getSortedList(taggedFeatureVector.getFeatureVector())) {
				bw.write(" "+a.getKey()+":"+a.getValue());
			}
			bw.write("\n");
			bw.flush();
		}
		bw.close();
		
	}

	public static void prepareSvmInputsFromFeaturesSelector(String structuredDataPath, String encoding, String ZERO_INDEX, String svmInputFilename, String wordsMapperFilename, FeatureModel model, int numOfAuthors) throws Exception {
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		Map<String, List<List<MorphemeRecord>>> structuredData = dataReader.readStructuredData("gold", numOfAuthors);
		List<TaggedFeatureVector<String>> trainingData = FeatureSelector.buildFeaturesVector(structuredData, null, model);
		BufferedWriter bw = new BufferedWriter(new FileWriter(svmInputFilename));
		AttributeList<String> attributes = new AttributeList<>();
		for (TaggedFeatureVector<String> taggedFeatureVector: trainingData) {
			bw.write(taggedFeatureVector.getTag());
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

	public static void prepareSvmEvaluationsFromFeaturesSelector(String structuredDataPath, String encoding, String ZERO_INDEX, String svmInputFilename, String wordsMapperFilename, FeatureModel model, int numOfAuthors) throws Exception {
		
		StructuredDataReader dataReader = new StructuredDataReader(structuredDataPath, encoding);
		Map<String, List<List<MorphemeRecord>>> structuredData = dataReader.readStructuredData("test", numOfAuthors);
		List<TaggedFeatureVector<String>> evaluationData = FeatureSelector.buildFeaturesVector(structuredData, null, model);
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
			bw.write(taggedFeatureVector.getTag());
			for (AttributeList<String>.Attribute a : attributes.getSortedList(taggedFeatureVector.getFeatureVector())) {
				bw.write(" "+a.getKey()+":"+a.getValue());
			}
			bw.write("\n");
			bw.flush();
		}
		bw.close();
		
	}
	
	public static void main(String[] args) throws Exception {
		/*
		 * Usage: SupportVectorMachine 
		 * [-p input_path] 
		 * [-e encoding=UTF8] 
		 * [-t training_filename=training] 
		 * [-v evalutation_filename=evaluation] 
		 * [-f feature_mapper_filename=featureMapper] 
		 * [-m model_filename] 
		 * [-n number_of_authors=10]
		 */
		String path = "C:\\Users\\asil\\Workspaces\\FinalProject\\TwitterReader\\OutputFromYap_10K_10A\\";
		String encoding = "UTF8";
		String trainingFilename = "training";
		String evalutationFilename = "evaluation";
		String featuresMapperFilename = "featuresMapper";
		int numOfAuthors = 10;
		FeatureModel featureModel = new FeatureModel();
		featureModel.features.add(FeatureType.BagOfWords);
		for (int i = 0; i < args.length;i+=2) {
			if (args[i].equals("-p")) {
				path = args[i+1];
			}
			else if (args[i].equals("-e")) {
				encoding = args[i+1];
			}
			else if (args[i].equals("-t")) {
				trainingFilename = args[i+1];
			}
			else if (args[i].equals("-v")) {
				evalutationFilename = args[i+1];
			}
			else if (args[i].equals("-f")) {
				featuresMapperFilename = args[i+1];
			}
			else if (args[i].equals("-n")) {
				numOfAuthors = Integer.parseInt(args[i+1]);
			}
			else if (args[i].equals("-m")) {
				featureModel = createModel(args[i+1], encoding);
			}
		}
		prepareSvmInputsFromFeaturesSelector(path, encoding, null, trainingFilename, featuresMapperFilename, featureModel, numOfAuthors);
		prepareSvmEvaluationsFromFeaturesSelector(path, encoding, null, evalutationFilename, featuresMapperFilename, featureModel, numOfAuthors);
		
	}

	private static FeatureModel createModel(String featuresFilename, String encoding) throws IOException {
		FeatureModel featureModel = new FeatureModel();
        BufferedReader in;
		in = new BufferedReader(new InputStreamReader(new FileInputStream(featuresFilename), encoding));
		String str;
        while ((str = in.readLine()) != null) {
        	if (str.equals("") == false)
        	{
        		if (!str.startsWith("%")) {
        			featureModel.features.add(FeatureType.valueOf(str));
        		}
        	}
        }
        in.close();
        return featureModel;
    }
}
