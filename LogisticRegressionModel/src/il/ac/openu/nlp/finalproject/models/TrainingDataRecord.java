package il.ac.openu.nlp.finalproject.models;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class TrainingDataRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	private double[] x;
	private int y;
	
	private final static int xSize = 8;
	private final static int ySize = 4;
	
	public TrainingDataRecord(double[] x, int y) {
		setTrainingDataRecord(x, y);
	}
	
	public void setTrainingDataRecord(double[] x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double[] getTrainingDataRecordFeaturesVector() {
		return x;
	}
	
	public int getTrainingDataRecordClass() {
		return y;
	}
	
	public void unSerialize(byte[] data) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		x = new double[(data.length-ySize)/xSize];
		
		for (int i=0;i<x.length;++i) {
			x[i]=byteBuffer.getDouble(i*xSize);
		}
		y = byteBuffer.getInt(x.length*xSize);
	}
	
	public byte[] serialize() {
		byte[] bytesBuffer = new byte[x.length*xSize+ySize];
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytesBuffer);
		for (double d : x) {
			byteBuffer.putDouble(d);
		}
		byteBuffer.putInt(y);
		return bytesBuffer;
	}
}
