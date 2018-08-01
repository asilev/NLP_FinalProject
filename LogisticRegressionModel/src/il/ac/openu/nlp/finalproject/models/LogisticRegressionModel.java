package il.ac.openu.nlp.finalproject.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;

public class LogisticRegressionModel {
	private TrainingDataWriter trainingDataWriter; //TODO
	private TrainingDataReader trainingDataReader; //TODO
	private static TrainingDataRecord<String>[] trainingData;
	private static double ALPHA = 0.01;
	private static int numOfIterations = 5000;
	private static final String ZERO_INDEX = "Zero"; 
	
	public LogisticRegressionModel(String trainingFileName) throws IOException {
		throw new NotImplementedException("File Constructor");
//		trainingDataWriter = new TrainingDataWriter(trainingFileName);
//		trainingDataReader = new TrainingDataReader(trainingFileName);
	}
	
	public LogisticRegressionModel(TrainingDataRecord<String>[] data) {
		LogisticRegressionModel.trainingData = data;
	}

	public static double h(Map<String, Double> parameters, FeatureVector<String> featureVector) {
		double parametersXfeatures = parameters.get(ZERO_INDEX); // Initial
		for (String featureKey : parameters.keySet()) {
			parametersXfeatures += parameters.get(featureKey)*featureVector.getFeature(featureKey);
		}
		return g(parametersXfeatures);
	}

	private static double g(double z) { // Sigmoid Function
		return 1.0/(1+Math.pow(Math.E, -1*z));
	}
	
	private static double j(Map<String, Double> parameters, String targetClass) throws Exception {
		double cost = 0;
		int numOfTrainingDataSet = trainingData.length;
		for (int i=0;i<numOfTrainingDataSet;++i) {
			cost += cost(h(parameters, trainingData[i].getTrainingFeaturesVector()),trainingData[i].getTrainingDataRecordClass().equals(targetClass)?1:0);
		}
		return cost/numOfTrainingDataSet;
	}
	
	private static double cost(double hThetaX, int y) throws Exception {
		if (y==0) {
			return -1*Math.log(1-hThetaX);
		}
		else if (y==1) {
			return -1*Math.log(hThetaX);
		}
		else {
			throw new Exception("All y's must be either 1 or 0");
		}
	}
	
	private static Map<String, Double> minJ(Map<String, Double> parameters, String targetClass) {
		Map<String, Double> returnParameters = new HashMap<>();
		int m = trainingData.length;
		for (String featureKey : parameters.keySet()) {
			double returnParameterValue = parameters.get(featureKey) - (ALPHA/m)*sumDerivations(parameters, featureKey, targetClass);
			returnParameters.put(featureKey, returnParameterValue);
		}
		return returnParameters;
	}
	
	private static double sumDerivations(Map<String, Double>parameters, String featureKey, String targetClass) {
		int m = trainingData.length;
		double returnValue = 0;
		for (int i=0;i<m;++i) {
			double xij = trainingData[i].getTrainingFeaturesVector().getFeature(featureKey);
			returnValue += (h(parameters, trainingData[i].getTrainingFeaturesVector())-(trainingData[i].getTrainingDataRecordClass().equals(targetClass)?1.0:0.0))*xij;
		}
		return returnValue;
	}
	
	public static Map<String, Double> trainParameters(Map<String, Double>initialparameters, String targetClass) throws Exception {
		for (int i=0;i<numOfIterations;++i) {
			System.out.println("cost "+i+": "+j(initialparameters, targetClass));
			System.out.println("probability: "+h(initialparameters,trainingData[0].getTrainingFeaturesVector()));
			initialparameters = minJ(initialparameters, targetClass);
		}
		return initialparameters;
	}
	
	public static void main(String[] args) throws Exception {
		trainingData = new TrainingDataRecord[3];
		trainingData[0] = new TrainingDataRecord<String>("Zero", "A");
		trainingData[0].getTrainingFeaturesVector().putFeature("a", 1.0);
		trainingData[0].getTrainingFeaturesVector().putFeature("b", 1.0);
		trainingData[0].getTrainingFeaturesVector().putFeature("c", 1.0);
		
		trainingData[1] = new TrainingDataRecord<String>("Zero", "B");
		trainingData[1].getTrainingFeaturesVector().putFeature("a", 1.0);
		trainingData[1].getTrainingFeaturesVector().putFeature("b", 1.0);
		trainingData[1].getTrainingFeaturesVector().putFeature("d", 1.0);
		
		trainingData[2] = new TrainingDataRecord<String>("Zero", "C");
		trainingData[2].getTrainingFeaturesVector().putFeature("b", 1.0);
		trainingData[2].getTrainingFeaturesVector().putFeature("c", 1.0);
		trainingData[2].getTrainingFeaturesVector().putFeature("d", 1.0);
		
		Map<String, Double> thetas = new HashMap<>();
		thetas.put("Zero", 1.0);
		thetas.put("a", 1.0);
		thetas.put("b", 1.0);
		thetas.put("c", 1.0);
		thetas.put("d", 1.0);
		thetas.put("e", 1.0);
		
		thetas = trainParameters(thetas, "C");
		
		FeatureVector<String> f = new FeatureVector<>();
		f.putFeature("a", 1.0);
		f.putFeature("b", 1.0);
		f.putFeature("e", 1.0);
		
		double p1 = h(thetas,trainingData[0].getTrainingFeaturesVector());
		System.out.println("p1="+p1);
		
		double p2 = h(thetas,trainingData[1].getTrainingFeaturesVector());
		System.out.println("p2="+p2);
		
		double p3 = h(thetas,trainingData[2].getTrainingFeaturesVector());
		System.out.println("p3="+p3);
		
		double pf = h(thetas,f);
		System.out.println("pf="+pf);
	}
}
