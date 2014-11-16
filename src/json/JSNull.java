package json;

public class JSNull extends JSNode {
	public JSNull() {
		super(JSType.Null);
	}
	
	public String toString() {
		return "null";
	}
}
