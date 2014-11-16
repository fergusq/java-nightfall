package json;

import java.util.HashMap;
import java.util.Map.Entry;

public class JSObject extends JSNode {
	public JSObject() {
		super(JSType.Object);
	}
	
	public void put(String key, JSNode value) {
		mValue.put(key, value);
	}
	
	public JSNode get(String key) {
		return mValue.get(key);
	}
	
	public void remove(String key) {
		mValue.remove(key);
	}
	
	public String toString() {
		String s = "{";
		for (Entry<String, JSNode> entry : mValue.entrySet()) {
			s += (entry.getKey() + " = " + entry.getValue().toString() + ", ");
		}
		s += "}";
		return s;
	}
	
	private HashMap<String,JSNode> mValue = new HashMap<String,JSNode>();
}
