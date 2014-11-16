package json;

public class JSString extends JSNode {
	public JSString(String val) {
		super(JSType.String);
		mValue = val;
	}
	
	public String getValue() {
		return mValue;
	}
	
	public void setValue(String val) {
		mValue = val;
	}
	
	public String toString() {
		return mValue;
	}
	
	private String mValue;
}
