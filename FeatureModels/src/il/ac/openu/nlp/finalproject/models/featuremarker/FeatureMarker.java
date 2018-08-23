package il.ac.openu.nlp.finalproject.models.featuremarker;

public class FeatureMarker {
	private String sFeatureName;
	private Object value;
	
	public FeatureMarker()
	{
	}
	
	public FeatureMarker(Object val)
	{
		this.value = val;
	}
	
	public FeatureMarker(String name, Object val)
	{
		this.sFeatureName = name;
		this.value = val;
	}
	
	public Object getValue()
	{
		return this.value;
	}
	
	public void setValue(Object val)
	{
		this.value = val;
	}
	
	public String getName()
	{
		return this.sFeatureName;
	}
	
	public void setName(String name)
	{
		this.sFeatureName = name;
	}
}

