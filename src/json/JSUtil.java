package json;

import java.awt.Color;

import util.*;

public class JSUtil {
	private JSUtil() {}
	
	//format: {"x" : <Number>, "y" : <Number>} | [ <Number>, <Number> ]
	public static FVec toFVec(JSNode nd) {
		if (nd.getType() == JSType.Object) {
			JSObject obj = (JSObject)nd;
			JSNode x = obj.get("x");
			JSNode y = obj.get("y");
			if (x != null && y != null && x.getType() == JSType.Number && y.getType() == JSType.Number) {
				return new FVec((float)((JSNumber)x).getValue(), (float)((JSNumber)y).getValue());
			} else {
				return null;
			}
		} else if (nd.getType() == JSType.Array) {
			JSArray arr = (JSArray)nd;
			if (arr.size() == 2) {
				JSNode x = arr.get(0);
				JSNode y = arr.get(1);
				if (x.getType() == JSType.Number && y.getType() == JSType.Number) {
					return new FVec((float)((JSNumber)x).getValue(), (float)((JSNumber)y).getValue());
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	//format: {"r" : <Number>, "g" : <Number>, "b" : <Number> } | "nn nn nn nn"
	public static Color toColor(JSNode nd) {
		if (nd.getType() == JSType.String) {
			String s = ((JSString)nd).getValue();
			if (s.length() == 6) {
				s = s + "ff";
			}

			if (s.length() == 8) {
				int r = Integer.parseInt(s.substring(0, 2), 16);
				int g = Integer.parseInt(s.substring(2, 4), 16);
				int b = Integer.parseInt(s.substring(4, 6), 16);
				int a = Integer.parseInt(s.substring(6, 8), 16);
				return new Color(r, g, b, a);
			}
			
			return null;
		} else {
			return null;
		}
	}
}
