package il.ac.openu.nlp.finalproject.models;

import java.io.Serializable;

public class ClassifiedFeatureRecord<K> implements Serializable { //TODO: should change from contains to inheriting the feature vector?
																	//TODO: This is duplicated with taggedFeatureVector
	private static final long serialVersionUID = 1L;
	private FeatureVector<K> x;
	private String y;
	public ClassifiedFeatureRecord(FeatureVector<K> x, String y) {
		setTrainingDataRecord(x, y);
	}
	
	public ClassifiedFeatureRecord(K zeroIndex, String y) {
		FeatureVector<K> newX = new FeatureVector<>();
		newX.put(zeroIndex, 1.0);
		setTrainingDataRecord(newX, y);
	}
	
	public void setTrainingDataRecord(FeatureVector<K>x, String y) {
		this.x = x;
		this.y = y;
	}
	
	public FeatureVector<K> getTrainingFeaturesVector() {
		return x;
	}
	
	public String getTrainingDataRecordClass() {
		return y;
	}
	
//	public void unSerialize(byte[] data) {
//		ByteBuffer byteBuffer = ByteBuffer.wrap(data);
//		x = new double[(data.length-ySize)/xSize];
//		
//		for (int i=0;i<x.length;++i) {
//			x[i]=byteBuffer.getDouble(i*xSize);
//		}
//		y = byteBuffer.getInt(x.length*xSize);
//	}
//	
//	public byte[] serialize() {
//		byte[] bytesBuffer = new byte[x.length*xSize+ySize];
//		ByteBuffer byteBuffer = ByteBuffer.wrap(bytesBuffer);
//		for (double d : x) {
//			byteBuffer.putDouble(d);
//		}
//		byteBuffer.putInt(y);
//		return bytesBuffer;
//	}
}
