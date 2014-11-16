package json;

public class JSBoolean extends JSNode {
	public JSBoolean(boolean val) {
		super(JSType.Boolean);
		mValue = val;
	}
	
	public boolean getValue() {
		return mValue;
	}
	
	public void setValue(boolean val) {
		mValue = val;
	}
	
	public String toString() {
		return mValue ? "true" : "false";
	}
	
	private boolean mValue;
}
