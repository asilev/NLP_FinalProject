package il.ac.openu.nlp.finalproject.models;

import java.io.IOException;

public class LogisticRegressionModel {
	private TrainingDataWriter trainingDataWriter;
	private TrainingDataReader trainingDataReader;
	private static double x[][];
	private static int y[];
	private static double ALPHA = 0.01;
	private static int numOfIterations = 5000;
	
	public LogisticRegressionModel(String trainingFileName) throws IOException {
		trainingDataWriter = new TrainingDataWriter(trainingFileName);
		trainingDataReader = new TrainingDataReader(trainingFileName);
	}
	public void addTrainingData(double[] x, int y) throws IOException {
		trainingDataWriter.write(new TrainingDataRecord(x, y));
	}
	
	private static double j(double[] theta) throws Exception {
		double cost = 0;
		int numOfTrainingDataSet = x.length;
		for (int i=0;i<numOfTrainingDataSet;++i) {
			cost += cost(h(theta, x[i]),y[i]);
		}
		return cost/numOfTrainingDataSet;
	}
	
	public static double h(double[] theta, double[] x) {
		double thetaX = theta[0];
		for (int i=0;i<x.length;++i) {
			thetaX += theta[i+1]*x[i];
		}
		return g(thetaX);
	}
	
	private static double g(double z) {
		return 1.0/(1+Math.pow(Math.E, -1*z));
	}
	
	private static double cost(double hThetaX, double y) throws Exception {
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
	
	private static double[] minJ(double[] theta) {
		double[] returnTheta = new double[theta.length];
		int m = x.length;
		for (int j=0;j<theta.length;++j) {
			returnTheta[j] = theta[j] - (ALPHA/m)*sumDerivations(theta, j);
		}
		return returnTheta;
	}
	
	private static double sumDerivations(double[] theta, int j) {
		int m=x.length;
		double returnValue = 0;
		for (int i=0;i<m;++i) {
			double xij = (j==0)?1:x[i][j-1];
			returnValue += (h(theta, x[i])-y[i])*xij;
		}
		return returnValue;
	}
	
	public static double[] findThetas(double[] initialThetas) throws Exception {
		for (int i=0;i<numOfIterations;++i) {
			System.out.println("cost "+i+": "+j(initialThetas));
			System.out.println("probability: "+h(initialThetas,x[0]));
			initialThetas = minJ(initialThetas);
		}
		return initialThetas;
	}
	
	public static void main(String[] args) throws Exception {
		x = new double[4][5];
//		x = new ArrayList<>(4);
		y = new int[4];
//		x.add(new ArrayList<>(5));
//		List<Double> x0 = x.get(0);
//		x0.add(7.0);
//		x0.add(8.0);
//		x0.add(5.0);
//		x0.add(9.0);
//		x0.add(1.0);
		x[0][0] = 7;
		x[0][1] = 8;
		x[0][2] = 5;
		x[0][3] = 9;
		x[0][4] = 1;
		
//		x.add(new ArrayList<>(5));
//		List<Double> x1 = x.get(1);
//		x1.add(0.1);
//		x1.add(1.0);
//		x1.add(2.0);
//		x1.add(1.0);
//		x1.add(3.0);
		x[1][0] = 0.1;
		x[1][1] = 1;
		x[1][2] = 2;
		x[1][3] = 1;
		x[1][4] = 3;
//		x.add(new ArrayList<>(5));
//		List<Double> x2 = x.get(2);
//		x2.add(9.0);
//		x2.add(3.0);
//		x2.add(4.0);
//		x2.add(5.0);
//		x2.add(1.0);
		x[2][0] = 9;
		x[2][1] = 3;
		x[2][2] = 4;
		x[2][3] = 5;
		x[2][4] = 1;
//		x.add(new ArrayList<>(5));
//		List<Double> x3 = x.get(3);
//		x3.add(0.1);
//		x3.add(0.1);
//		x3.add(3.0);
//		x3.add(9.0);
//		x3.add(2.0);
		x[3][0] = 0.1;
		x[3][1] = 0.1;
		x[3][2] = 3;
		x[3][3] = 9;
		x[3][4] = 2;
		
		double[] initialThetas = new double[6];
		initialThetas[0]=1;
		initialThetas[1]=1;
		initialThetas[2]=1;
		initialThetas[3]=1;
		initialThetas[4]=1;
		initialThetas[5]=1;
		
		y[0] = 1;
		y[1] = 0;
		y[2] = 0;
		y[3] = 0;
		double[] thetas1 = findThetas(initialThetas);
		y[0] = 0;
		y[1] = 1;
		y[2] = 0;
		y[3] = 0;
		double[] thetas2 = findThetas(initialThetas);
		y[0] = 0;
		y[1] = 0;
		y[2] = 1;
		y[3] = 0;
		double[] thetas3 = findThetas(initialThetas);
		y[0] = 0;
		y[1] = 0;
		y[2] = 0;
		y[3] = 1;
		double[] thetas4 = findThetas(initialThetas);

		
		double[] evalX = new double[5];
//		List<Double> evalX = new ArrayList<>(5);
//		evalX.add(1.0);
//		evalX.add(1.0);
//		evalX.add(0.0);
//		evalX.add(1.0);
//		evalX.add(0.0);
		evalX[0]=1;
		evalX[1]=1;
		evalX[2]=0;
		evalX[3]=1;
		evalX[4]=0;
		
		double[] probs = new double[4];
		probs[0] = h(thetas1,evalX);
		probs[1] = h(thetas2,evalX);
		probs[2] = h(thetas3,evalX);
		probs[3] = h(thetas4,evalX);
		
		for (int i=0;i<4;++i) {
			System.out.println(probs[i]);
		}
	}
}
