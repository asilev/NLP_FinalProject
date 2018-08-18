package il.ac.openu.nlp.finalproject.models;

public class TaggedFeatureVector<K> {
	private FeatureVector<K> featureVector;
	private String tag;
	public FeatureVector<K> getFeatureVector() {
		return featureVector;
	}
	public void setFeatureVector(FeatureVector<K> featureVector) {
		this.featureVector = featureVector;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public TaggedFeatureVector(FeatureVector<K> featureVector, String tag) {
		super();
		this.featureVector = featureVector;
		this.tag = tag;
	}
	public TaggedFeatureVector(String tag, K ZERO_INDEX) {
		super();
		this.featureVector = new FeatureVector<>(ZERO_INDEX);
		this.tag = tag;
	}
}