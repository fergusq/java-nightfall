package json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class JSArray extends JSNode {
	public JSArray() {
		super(JSType.Array);
		mValue = new ArrayList<JSNode>();
	}
	public JSArray(JSNode... values) {
		super(JSType.Array);
		mValue = new ArrayList<JSNode>(Arrays.asList(values));
	}
	
	public JSNode get(int index) {
		assert index < mValue.size(): "JSArray index out of bounds";
		return mValue.get(index);
	}
	
	public void set(int index, JSNode value) {
		assert index < mValue.size(): "JSArray index out of bounds";
		mValue.set(index, value);
	}
	
	public void add(JSNode value) {
		mValue.add(value);
	}
	
	public int size() {
		return mValue.size();
	}
	
	public Collection<JSNode> getChildren() {
		return Collections.unmodifiableCollection(mValue);
	}
	
	public String toString() {
		String s = "[";
		for (JSNode nd : mValue) {
			s += (nd.toString() + ", "); 
		}
		s += "]";
		return s;
	}
	
	private ArrayList<JSNode> mValue;
}
