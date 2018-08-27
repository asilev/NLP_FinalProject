package il.ac.openu.nlp.finalproject.models.featuremarker;

public class FeatureMarker {
	private String sFeatureName;
	private Object value;
	
	public FeatureMarker()
	{
	}
	
	public FeatureMarker(String name)
	{
		this.sFeatureName = name;
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
	
	public Object applyFeatureCond(Object inObj)
	{
		// Default function that does nothing, to be overridden 
		Object outObj = null;
		return outObj;
	}
}

