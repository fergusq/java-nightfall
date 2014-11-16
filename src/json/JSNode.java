package json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public abstract class JSNode {
	public JSNode(JSType t) {
		mType = t;
	}
	
	public JSType getType() {
		return mType;
	}
	
	public String getStringValue() {
		assert mType == JSType.String: "Called getStringValue on non-String";
		return ((JSString)this).getValue();
	}
	
	public double getNumberValue() {
		assert mType == JSType.Number: "Called getNumberValue on non-Number";
		return ((JSNumber)this).getValue();
	}
	
	public int getIntValue() {
		assert mType == JSType.Number: "Called getIntValue on non-Number";
		JSNumber n = ((JSNumber)this);
		int i = (int)n.getValue();
		assert (double)i == n.getNumberValue(): "Called getIntValue on non-Integral Number `" + n.getValue() + "`";
		return i;
	}
	
	public boolean getBooleanValue() {
		assert mType == JSType.Boolean: "Called getBooleanValue on non-Boolean";
		return ((JSBoolean)this).getValue();
	}
	
	public static JSNode parse(InputStream in) throws IOException {
		return parse(new BufferedReader(new InputStreamReader(in)));
	}
	
	public static String readDigits(Reader in) throws IOException {
		String num = "";
		while (true) {
			in.mark(2);
			char c = (char)in.read();
			if (Character.isDigit(c)) {
				num += c;
			} else {
				in.reset();
				break;
			}
		}
		return num;
	}
	
	public static void whitespace(Reader in) throws IOException {
		while (true) {
			in.mark(2);
			char c = (char)in.read();
			if (!Character.isWhitespace(c)) {
				in.reset();
				return;
			}
		}
	}
	
	public static JSNode parse(Reader in) throws IOException {
		whitespace(in);
		char c = (char)in.read();
		if (c == '{') {
			JSObject obj = new JSObject();
			whitespace(in);
			in.mark(2);
			c = (char)in.read();
			if (c != ']') {
				in.reset();
				while (true) {
					JSNode key = parse(in);
					if (key.getType() != JSType.String)
						throw new IOException("Expected string for key in Object");
					
					whitespace(in);
					c = (char)in.read();
					if (c != ':')
						throw new IOException("Expected `:` between key and value");
					
					JSNode val = parse(in);
					obj.put(((JSString)key).getValue(), val);
					
					whitespace(in);
					in.mark(2);
					c = (char)in.read();
					if (c == '}') {
						break;
					} else if (c != ',') {
						throw new IOException("Missing `,` in Object");
					}
				}	
			}
			return obj;	
		} else if (c == '[') {
			JSArray arr = new JSArray();
			whitespace(in);
			in.mark(2);
			c = (char)in.read();
			if (c != ']') {
				in.reset();
				while (true) {
					JSNode val = parse(in);
					arr.add(val);
					
					whitespace(in);
					in.mark(2);
					c = (char)in.read();
					if (c == ']') {
						break;
					} else if (c != ',') {
						throw new IOException("Missing `,` in Array");
					}
				}
			}
			return arr;
		} else if (c == '"') {
			String str = "";
			while (true) {
				c = (char)in.read();
				if (c == '\\') {
					c = (char)in.read();
					switch (c) {
					case 'n': str += '\n'; break;
					case 'r': str += '\r'; break;
					case 't': str += '\t'; break;
					case '\\': str += '\\'; break;
					case '"': str += '"'; break;
					case (char)-1: throw new IOException("Unfinished escape sequence near <EOF>");
					default: throw new IOException("Unknown escape sequence `\\" + c + "`");
					}
				} else if (c == '"') {
					break;
				} else if (c == -1) {
					throw new IOException("Unfinished string near <EOF>");
				} else {
					str += c;
				}
			}
			return new JSString(str);
		} else if (Character.isDigit(c) || c == '-') {
			String num = "" + c;
			num += readDigits(in);
			in.mark(2);
			if ((char)in.read() == '.') {
				num += readDigits(in);
			} else {
				in.reset();
			}
			double n = Double.parseDouble(num);
			return new JSNumber(n);
		} else if (Character.isLetter(c)) {
			String ident = "" + c;
			while (true) {
				in.mark(2);
				c = (char)in.read();
				if (Character.isLetter(c) || Character.isDigit(c)) {
					ident += c;
				} else {
					in.reset();
					break;
				}
			}
			if (ident.equals("null")) {
				return new JSNull();
			} else if (ident.equals("false")) {
				return new JSBoolean(false);
			} else if (ident.equals("true")) {
				return new JSBoolean(true);
			} else {
				throw new IOException("Bad identifier `" + ident + "`");
			}
		} else if (c == -1) {
			throw new IOException("Unexpected <EOF>");
		} else {
			throw new IOException("Bad input character `" + c + "`");
		}
	}
	
	private final JSType mType;
}
