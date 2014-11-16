package json;

public class JSNumber extends JSNode {
	public JSNumber(double val) {
		super(JSType.Number);
		mValue = val;
	}
	
	public double getValue() {
		return mValue;
	}
	
	public void setValue(double val) {
		mValue = val;
	}
	
	public String toString() {
		return String.valueOf(mValue);
	}
	
	private double mValue;
}
