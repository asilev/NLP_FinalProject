package il.ac.openu.nlp.finalproject.models.lr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.FeatureModel;
import il.ac.openu.nlp.finalproject.models.FeatureModel.FeatureType;
import il.ac.openu.nlp.finalproject.models.FeatureSelector;
import il.ac.openu.nlp.finalproject.models.FeatureVector;
import il.ac.openu.nlp.finalproject.models.MorphemeRecord;
import il.ac.openu.nlp.finalproject.models.StructuredDataReader;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;

public class LogisticRegressionModel {
	private static List<TaggedFeatureVector<String>> trainingData;
	private static List<TaggedFeatureVector<String>> testData;
	private static double ALPHA = 0.01;
	private static int numOfIterations = 2000;
	private static double diffThreshold = 0.00005;
	private static String ZERO_INDEX = "ZERO";
	private static FeatureModel model = new FeatureModel();
	
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
		Double lastCost = Double.POSITIVE_INFINITY;
		Double nextCost = Double.POSITIVE_INFINITY;
		Double cost = Double.POSITIVE_INFINITY;
//		for (int i=0;i<numOfIterations;++i) {
		while (lastCost == Double.POSITIVE_INFINITY || lastCost - cost > diffThreshold) {
			lastCost = nextCost;
			cost = j(initialparameters, targetClass);
			if (cost > lastCost) {
				throw new Exception("Cost is getting higher. Use lower alpha value");
			}
			nextCost = cost;
			System.out.println("cost "/*+i*/+": "+cost);
			// Optional debug print, but takes a long time to calcualte
//			System.out.println("probability: "+h(initialparameters,trainingData.get(0).getFeatureVector()));
			initialparameters = minJ(initialparameters, targetClass);
		}
		return initialparameters;
	}
	
	public static void main(String[] args) throws Exception {
		// Usage: LogisticRegression <input folder> <encoding>
		StructuredDataReader dataReader = new StructuredDataReader(args[0], args[1]);
		Map<String, List<List<MorphemeRecord>>> data = dataReader.readStructuredData("gold", 10);
		model.features.add(FeatureType.NumberOfMorphemes);
		model.features.add(FeatureType.AverageNumberOfPunctuationMarks);
		model.features.add(FeatureType.AverageSentenceSize);
		model.features.add(FeatureType.AverageWordSize);
		model.features.add(FeatureType.NumberOfCharacters);
		model.features.add(FeatureType.NumberOfPucntuationMarks);
		model.features.add(FeatureType.NumOfLongWords);
		model.features.add(FeatureType.PosUsage);
		trainingData = FeatureSelector.buildFeaturesVector(data, ZERO_INDEX, model);
		data = dataReader.readStructuredData("test", 10);
		testData = FeatureSelector.buildFeaturesVector(data, ZERO_INDEX, model);
		
		HashSet<String> labelSet = new HashSet<>();
		for (TaggedFeatureVector<String> featureVector : trainingData) {
			labelSet.add(featureVector.getTag());
		}
		Map<String, Map<String, Double>> thetas = new HashMap<>();
		for (String label : labelSet) {
			System.out.println("Building initial thetas");
			thetas.put(label, new HashMap<>());
			thetas.get(label).put(ZERO_INDEX, 1.0);
			for (int i=0; i<trainingData.size();++i) {
				for (String feature: trainingData.get(i).getFeatureVector().keySet()) {
					thetas.get(label).put(feature, 1.0);
				}
			}
			System.out.println("Training Thetas for "+label);
			thetas.put(label, trainParameters(thetas.get(label), label));
		
			saveToFile("output"+label+"Parameters.pars", thetas.get(label));
		}
		Double success = 0.0;
		for (TaggedFeatureVector<String> featureVector : testData) {
			System.out.println("Actual Tweeter: "+featureVector.getTag());
			Double bestP = 0.0;
			String bestAuthor = null;
			for (String label : labelSet) {
				Double p = h(thetas.get(label),featureVector.getFeatureVector());
				System.out.println("Prediction for "+label+": "+p);
				if (p>bestP) {
					bestP = p;
					bestAuthor = label;
				}
			}
			if (bestAuthor.equals(featureVector.getTag())) {
				success++;
			}
		}
		success = success / testData.size();
		System.out.println("Success Ration: "+success);
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
