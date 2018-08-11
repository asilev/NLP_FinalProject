package il.ac.openu.nlp.finalproject.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;

public class LogisticRegressionModel {
	private static List<TaggedFeatureVector<String>> trainingData;
	private static double ALPHA = 0.01;
	private static int numOfIterations = 5000;
	private static final String ZERO_INDEX = "Zero"; 
	
	public LogisticRegressionModel(String trainingFileName) throws IOException {
		throw new NotImplementedException("File Constructor");
	}
	
	public LogisticRegressionModel(List<TaggedFeatureVector<String>> data) {
		LogisticRegressionModel.trainingData = data;
	}

	public static double h(Map<String, Double> parameters, FeatureVector<String> featureVector) {
		double parametersXfeatures = parameters.get(ZERO_INDEX); // Initial
		for (String featureKey : featureVector.keySet()) {
			parametersXfeatures += parameters.get(featureKey)*featureVector.get(featureKey);
		}
		return g(parametersXfeatures);
	}

	private static double g(double z) { // Sigmoid Function
		return 1.0/(1+Math.pow(Math.E, -1*z));
	}
	
	private static double j(Map<String, Double> parameters, String targetClass) throws Exception {
		double cost = 0;
		for (TaggedFeatureVector<String> trainingExample : trainingData) {
			cost += cost(h(parameters,trainingExample.getFeatureVector()), trainingExample.getTag().equals(targetClass));
		}
//		for (int i=0;i<numOfTrainingDataSet;++i) {
//			cost += cost(h(parameters, trainingData[i].getTrainingFeaturesVector()),trainingData[i].getTrainingDataRecordClass().equals(targetClass)?1:0);
//		}
		return cost/trainingData.size();
	}
	
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
	
	private static Map<String, Double> minJ(Map<String, Double> parameters, String targetClass) {
		Map<String, Double> returnParameters = new HashMap<>();
		int m = trainingData.size();
//		int i=0;
		for (String featureKey : parameters.keySet()) {
			double returnParameterValue = parameters.get(featureKey) - (ALPHA/m)*sumDerivations(parameters, featureKey, targetClass);
//			System.out.println(i++);
			returnParameters.put(featureKey, returnParameterValue);
		}
		return returnParameters;
	}
	
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
	
	public static Map<String, Double> trainParameters(Map<String, Double>initialparameters, String targetClass) throws Exception {
		for (int i=0;i<numOfIterations;++i) {
			System.out.println("cost "+i+": "+j(initialparameters, targetClass));
//			System.out.println("probability: "+h(initialparameters,trainingData.get(0).getFeatureVector()));
			initialparameters = minJ(initialparameters, targetClass);
		}
		return initialparameters;
	}
	
	public static void main(String[] args) throws Exception {
//		trainingData = new ClassifiedFeatureRecord[3];
//		trainingData[0] = new ClassifiedFeatureRecord<String>(ZERO_INDEX, "A");
//		trainingData[0].getTrainingFeaturesVector().put("a", 1.0);
//		trainingData[0].getTrainingFeaturesVector().put("b", 1.0);
//		trainingData[0].getTrainingFeaturesVector().put("c", 1.0);
//		
//		trainingData[1] = new ClassifiedFeatureRecord<String>(ZERO_INDEX, "B");
//		trainingData[1].getTrainingFeaturesVector().put("a", 1.0);
//		trainingData[1].getTrainingFeaturesVector().put("b", 1.0);
//		trainingData[1].getTrainingFeaturesVector().put("d", 1.0);
//		
//		trainingData[2] = new ClassifiedFeatureRecord<String>(ZERO_INDEX, "C");
//		trainingData[2].getTrainingFeaturesVector().put("b", 1.0);
//		trainingData[2].getTrainingFeaturesVector().put("c", 1.0);
//		trainingData[2].getTrainingFeaturesVector().put("d", 1.0);
		StructuredDataReader dataReader = new StructuredDataReader(args[0], args[1]);
		System.out.println("Building bag of words");
		trainingData = dataReader.buildAuthorBagOfWords(); 
		
		System.out.println("Building initial thetas");
		System.out.println("Total number of unique words: "+dataReader.getListOfUniqueWords().size());
		Map<String, Double> thetas = new HashMap<>();
		thetas.put(ZERO_INDEX, 1.0);
		// TODO: getListOfUniqueWords must be called after training. Should be fixed 
		for (String word : dataReader.getListOfUniqueWords()) {
			thetas.put(word, 1.0);
		}
//		thetas.put("a", 1.0);
//		thetas.put("b", 1.0);
//		thetas.put("c", 1.0);
//		thetas.put("d", 1.0);
//		thetas.put("e", 1.0);
		System.out.println("Training Thetas for bencaspit");
		thetas = trainParameters(thetas, "bencaspit");
		
		FeatureVector<String> f = new FeatureVector<>();
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
}
