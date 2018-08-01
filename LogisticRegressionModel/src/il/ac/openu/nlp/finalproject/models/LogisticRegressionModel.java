package il.ac.openu.nlp.finalproject.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;

public class LogisticRegressionModel {
	private TrainingDataWriter trainingDataWriter; //TODO
	private TrainingDataReader trainingDataReader; //TODO
	private static TrainingDataRecord<String>[] data;
	private static double ALPHA = 0.01;
	private static int numOfIterations = 5000;
	
	public LogisticRegressionModel(String trainingFileName) throws IOException {
		throw new NotImplementedException("File Constructor");
//		trainingDataWriter = new TrainingDataWriter(trainingFileName);
//		trainingDataReader = new TrainingDataReader(trainingFileName);
	}
	
	public LogisticRegressionModel(TrainingDataRecord<String>[] data) {
		LogisticRegressionModel.data = data;
	}

	public static double h(Map<String, Double> theta, FeatureVector<String> x) {
		double thetaX = theta.get("Zero");
		for (String key : theta.keySet()) {
			thetaX += theta.get(key)*x.getFeature(key);
		}
		return g(thetaX);
	}

	private static double g(double z) {
		return 1.0/(1+Math.pow(Math.E, -1*z));
	}
	
	private static double j(Map<String, Double> theta, String target) throws Exception {
		double cost = 0;
		int numOfTrainingDataSet = data.length;
		for (int i=0;i<numOfTrainingDataSet;++i) {
			cost += cost(h(theta, data[i].getTrainingFeaturesVector()),data[i].getTrainingDataRecordClass().equals(target)?1:0);
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
	
	private static Map<String, Double> minJ(Map<String, Double> theta, String klass) {
		Map<String, Double> returnTheta = new HashMap<>();
		int m = data.length;
		for (String key : theta.keySet()) {
			double value = theta.get(key) - (ALPHA/m)*sumDerivations(theta, key, klass);
			returnTheta.put(key, value);
		}
		return returnTheta;
	}
	
	private static double sumDerivations(Map<String, Double>theta, String j, String klass) {
		int m = data.length;
		double returnValue = 0;
		for (int i=0;i<m;++i) {
			double xij = data[i].getTrainingFeaturesVector().getFeature(j);
			returnValue += (h(theta, data[i].getTrainingFeaturesVector())-(data[i].getTrainingDataRecordClass().equals(klass)?1.0:0.0))*xij;
		}
		return returnValue;
	}
	
	public static Map<String, Double> findThetas(Map<String, Double>initialThetas, String klass) throws Exception {
		for (int i=0;i<numOfIterations;++i) {
			System.out.println("cost "+i+": "+j(initialThetas, klass));
			System.out.println("probability: "+h(initialThetas,data[0].getTrainingFeaturesVector()));
			initialThetas = minJ(initialThetas, klass);
		}
		return initialThetas;
	}
	
	public static void main(String[] args) throws Exception {
		data = new TrainingDataRecord[3];
		data[0] = new TrainingDataRecord<String>("Zero", "A");
		data[0].getTrainingFeaturesVector().putFeature("a", 1.0);
		data[0].getTrainingFeaturesVector().putFeature("b", 1.0);
		data[0].getTrainingFeaturesVector().putFeature("c", 1.0);
		
		data[1] = new TrainingDataRecord<String>("Zero", "B");
		data[1].getTrainingFeaturesVector().putFeature("a", 1.0);
		data[1].getTrainingFeaturesVector().putFeature("b", 1.0);
		data[1].getTrainingFeaturesVector().putFeature("d", 1.0);
		
		data[2] = new TrainingDataRecord<String>("Zero", "C");
		data[2].getTrainingFeaturesVector().putFeature("b", 1.0);
		data[2].getTrainingFeaturesVector().putFeature("c", 1.0);
		data[2].getTrainingFeaturesVector().putFeature("d", 1.0);
		
		Map<String, Double> thetas = new HashMap<>();
		thetas.put("Zero", 1.0);
		thetas.put("a", 1.0);
		thetas.put("b", 1.0);
		thetas.put("c", 1.0);
		thetas.put("d", 1.0);
		thetas.put("e", 1.0);
		
		thetas = findThetas(thetas, "C");
		
		FeatureVector<String> f = new FeatureVector<>();
		f.putFeature("a", 1.0);
		f.putFeature("b", 1.0);
		f.putFeature("e", 1.0);
		
		double p1 = h(thetas,data[0].getTrainingFeaturesVector());
		System.out.println("p1="+p1);
		
		double p2 = h(thetas,data[1].getTrainingFeaturesVector());
		System.out.println("p2="+p2);
		
		double p3 = h(thetas,data[2].getTrainingFeaturesVector());
		System.out.println("p3="+p3);
		
		double pf = h(thetas,f);
		System.out.println("pf="+pf);
		
		
	}
}
