package il.ac.openu.nlp.finalproject.models.lr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.FeatureVector;
import il.ac.openu.nlp.finalproject.models.MorphemeRecord;
import il.ac.openu.nlp.finalproject.models.StructuredDataReader;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;
import il.ac.openu.nlp.finalproject.models.bagofwords.BagOfWordsModel;

public class LogisticRegressionModel {
	private static List<TaggedFeatureVector<String>> trainingData;
	private static double ALPHA = 1;
	private static int numOfIterations = 50;
	private static String ZERO_INDEX; 
	
	public LogisticRegressionModel(List<TaggedFeatureVector<String>> data, String ZERO_INDEX) {
		LogisticRegressionModel.trainingData = data;
		LogisticRegressionModel.ZERO_INDEX = ZERO_INDEX;
	}

	// Hypothesis Function
	public static double h(Map<String, Double> parameters, FeatureVector<String> featureVector) {
		double parametersXfeatures = parameters.get(ZERO_INDEX); // Initial
		for (String featureKey : featureVector.keySet()) {
			parametersXfeatures += parameters.get(featureKey)*featureVector.get(featureKey);
		}
		return g(parametersXfeatures);
	}

	// Sigmoid Function
	private static double g(double z) {
		return 1.0/(1+Math.pow(Math.E, -1*z));
	}
	
	// Loss Function
	private static double j(Map<String, Double> parameters, String targetClass) throws Exception {
		double cost = 0;
		for (TaggedFeatureVector<String> trainingExample : trainingData) {
			cost += cost(h(parameters,trainingExample.getFeatureVector()), trainingExample.getTag().equals(targetClass));
		}
		return cost/trainingData.size();
	}
	
	// Cost Function
	private static double cost(double hThetaX, boolean y) throws Exception {
		if (!y) {
			return -1*Math.log(1-hThetaX);
		}
		else if (y) {
			return -1*Math.log(hThetaX);
		}
		else {
			throw new Exception("All y's must be either 1 or 0");
		}
	}
	
	// Gradient Descent Function
	private static Map<String, Double> minJ(Map<String, Double> parameters, String targetClass) {
		Map<String, Double> returnParameters = new HashMap<>();
		int m = trainingData.size();
		 
		for (String featureKey : parameters.keySet()) {
			double returnParameterValue = parameters.get(featureKey) - (ALPHA/m)*sumDerivations(parameters, featureKey, targetClass);
			returnParameters.put(featureKey, returnParameterValue);
		}
		return returnParameters;
	}
	
	// Sum of derivations Function
	private static double sumDerivations(Map<String, Double>parameters, String featureKey, String targetClass) {
		double returnValue = 0;
		for (TaggedFeatureVector<String> trainingExample : trainingData) {
			double xij = trainingExample.getFeatureVector().get(featureKey);
			if (xij!=0) {
				returnValue += (h(parameters, trainingExample.getFeatureVector())-(trainingExample.getTag().equals(targetClass)?1.0:0.0))*xij;
			}
		}
		return returnValue;
	}
	
	// Training theta parameters
	public static Map<String, Double> trainParameters(Map<String, Double>initialparameters, String targetClass) throws Exception {
		for (int i=0;i<numOfIterations;++i) {
			System.out.println("cost "+i+": "+j(initialparameters, targetClass));
//			System.out.println("probability: "+h(initialparameters,trainingData.get(0).getFeatureVector()));
			initialparameters = minJ(initialparameters, targetClass);
//			saveToFile("output/"+targetClass+"Parameters.pars", initialparameters);
		}
		return initialparameters;
	}
	
	public static void main(String[] args) throws Exception {
		StructuredDataReader dataReader = new StructuredDataReader(args[0], args[1]);
		System.out.println("Building bag of words");
		Map<String, List<List<MorphemeRecord>>> data = dataReader.readStructuredData();
		BagOfWordsModel bow = new BagOfWordsModel();
		trainingData = bow.buildAuthorBagOfWords(data, ZERO_INDEX);
		
		System.out.println("Building initial thetas");
		System.out.println("Total number of unique words: "+dataReader.getListOfUniqueWords().size());
		Map<String, Double> thetas = new HashMap<>();
		thetas.put(ZERO_INDEX, 1.0);
		// TODO: getListOfUniqueWords must be called after training. Should be fixed 
		for (String word : dataReader.getListOfUniqueWords()) {
			thetas.put(word, 1.0);
		}
		System.out.println("Training Thetas for bencaspit");
		String label = "bencaspit";
		thetas = trainParameters(thetas, label);
		
		saveToFile("outputBenCaspitParameters.pars", thetas);
		
		FeatureVector<String> f = new FeatureVector<>(ZERO_INDEX);
		f.put("a", 1.0);
		f.put("b", 1.0);
		f.put("e", 1.0);
		
		double p1 = h(thetas,trainingData.get(0).getFeatureVector());
		System.out.println("p1="+p1);
		
		double p2 = h(thetas,trainingData.get(1).getFeatureVector());
		System.out.println("p2="+p2);
		
		double p3 = h(thetas,trainingData.get(2).getFeatureVector());
		System.out.println("p3="+p3);
		
		double pf = h(thetas,f);
		System.out.println("pf="+pf);
	}

	private static void saveToFile(String thetasFilename, Map<String, Double> thetas) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(thetasFilename));
		for (Map.Entry<String, Double> mapEntry: thetas.entrySet()) {
			if (mapEntry.getKey()==null) {
				bw.write("");				
			}
			else {
				bw.write(mapEntry.getKey());
			}
			bw.write("\t"+mapEntry.getValue().toString()+"\n");
		}
		bw.close();
	}
}
