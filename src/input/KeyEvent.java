package input;

import java.util.ArrayList;
import java.util.HashMap;

public class KeyEvent extends Event {
	public static enum KeyEventType {
		Press	(1<<0),
		Release	(1<<1);
		
		public static final int ButtonMask = Press.getMask() | Release.getMask();
		
		private KeyEventType(int mask) {
			mMask = mask;
		}
		
		public int getMask() {
			return mMask;
		}
		
		private int mMask;
	}
	
	public static enum Key {
		Enter		(java.awt.event.KeyEvent.VK_ENTER, '\n'),
		BackSpace	(java.awt.event.KeyEvent.VK_BACK_SPACE),
		Tab			(java.awt.event.KeyEvent.VK_TAB, '\t'),
		Cancel		(java.awt.event.KeyEvent.VK_CANCEL),
		Clear		(java.awt.event.KeyEvent.VK_CLEAR),
		Shift		(java.awt.event.KeyEvent.VK_SHIFT),
		Control		(java.awt.event.KeyEvent.VK_CONTROL),
		Alt			(java.awt.event.KeyEvent.VK_ALT),
		Pause		(java.awt.event.KeyEvent.VK_PAUSE),
		CapsLock	(java.awt.event.KeyEvent.VK_CAPS_LOCK),
		Escape		(java.awt.event.KeyEvent.VK_ESCAPE),
		Space		(java.awt.event.KeyEvent.VK_SPACE, ' '),
		PageUp		(java.awt.event.KeyEvent.VK_PAGE_UP),
		PageDown	(java.awt.event.KeyEvent.VK_PAGE_DOWN),
		End			(java.awt.event.KeyEvent.VK_END),
		Home		(java.awt.event.KeyEvent.VK_HOME),
		Left		(java.awt.event.KeyEvent.VK_LEFT),
		Up			(java.awt.event.KeyEvent.VK_UP),
		Right		(java.awt.event.KeyEvent.VK_RIGHT),
		Down		(java.awt.event.KeyEvent.VK_DOWN),
		Comma		(java.awt.event.KeyEvent.VK_COMMA, ','),
		Minus		(java.awt.event.KeyEvent.VK_MINUS, '-'),
		Period		(java.awt.event.KeyEvent.VK_PERIOD, '.'),
		Slash		(java.awt.event.KeyEvent.VK_SLASH, '/'),
		Semicolon	(java.awt.event.KeyEvent.VK_SEMICOLON, ';'),
		Equals		(java.awt.event.KeyEvent.VK_EQUALS, '='),
		K0		(java.awt.event.KeyEvent.VK_0, '0'),
		K1		(java.awt.event.KeyEvent.VK_1, '1'),
		K2		(java.awt.event.KeyEvent.VK_2, '2'),
		K3		(java.awt.event.KeyEvent.VK_3, '3'),
		K4		(java.awt.event.KeyEvent.VK_4, '4'),
		K5		(java.awt.event.KeyEvent.VK_5, '5'),
		K6		(java.awt.event.KeyEvent.VK_6, '6'),
		K7		(java.awt.event.KeyEvent.VK_7, '7'),
		K8		(java.awt.event.KeyEvent.VK_8, '8'),
		K9		(java.awt.event.KeyEvent.VK_9, '9'),
		A		(java.awt.event.KeyEvent.VK_A, 'a'),
		B		(java.awt.event.KeyEvent.VK_B, 'b'),
		C		(java.awt.event.KeyEvent.VK_C, 'c'),
		D		(java.awt.event.KeyEvent.VK_D, 'd'),
		E		(java.awt.event.KeyEvent.VK_E, 'e'),
		F		(java.awt.event.KeyEvent.VK_F, 'f'),
		G		(java.awt.event.KeyEvent.VK_G, 'g'),
		H		(java.awt.event.KeyEvent.VK_H, 'h'),
		I		(java.awt.event.KeyEvent.VK_I, 'i'),
		J		(java.awt.event.KeyEvent.VK_J, 'j'),
		K		(java.awt.event.KeyEvent.VK_K, 'k'),
		L		(java.awt.event.KeyEvent.VK_L, 'l'),
		M		(java.awt.event.KeyEvent.VK_M, 'm'),
		N		(java.awt.event.KeyEvent.VK_N, 'n'),
		O		(java.awt.event.KeyEvent.VK_O, 'o'),
		P		(java.awt.event.KeyEvent.VK_P, 'p'),
		Q		(java.awt.event.KeyEvent.VK_Q, 'q'),
		R		(java.awt.event.KeyEvent.VK_R, 'r'),
		S		(java.awt.event.KeyEvent.VK_S, 's'),
		T		(java.awt.event.KeyEvent.VK_T, 't'),
		U		(java.awt.event.KeyEvent.VK_U, 'u'),
		V		(java.awt.event.KeyEvent.VK_V, 'v'),
		W		(java.awt.event.KeyEvent.VK_W, 'w'),
		X		(java.awt.event.KeyEvent.VK_X, 'x'),
		Y		(java.awt.event.KeyEvent.VK_Y, 'y'),
		Z		(java.awt.event.KeyEvent.VK_Z, 'z'),
		OpenBracket		(java.awt.event.KeyEvent.VK_OPEN_BRACKET, '['),
		BackSlash		(java.awt.event.KeyEvent.VK_BACK_SLASH, '\\'),
		CloseBracket	(java.awt.event.KeyEvent.VK_CLOSE_BRACKET, ']'),
		Numpad0			(java.awt.event.KeyEvent.VK_NUMPAD0),
		Numpad1			(java.awt.event.KeyEvent.VK_NUMPAD1),
		Numpad2			(java.awt.event.KeyEvent.VK_NUMPAD2),
		Numpad3			(java.awt.event.KeyEvent.VK_NUMPAD3),
		Numpad4			(java.awt.event.KeyEvent.VK_NUMPAD4),
		Numpad5			(java.awt.event.KeyEvent.VK_NUMPAD5),
		Numpad6			(java.awt.event.KeyEvent.VK_NUMPAD6),
		Numpad7			(java.awt.event.KeyEvent.VK_NUMPAD7),
		Numpad8			(java.awt.event.KeyEvent.VK_NUMPAD8),
		Numpad9			(java.awt.event.KeyEvent.VK_NUMPAD9),
		Multiply		(java.awt.event.KeyEvent.VK_MULTIPLY, '*'),
		Add				(java.awt.event.KeyEvent.VK_ADD, '+'),
		Separater		(java.awt.event.KeyEvent.VK_SEPARATER),
		Separator		(java.awt.event.KeyEvent.VK_SEPARATOR),
		Subtract		(java.awt.event.KeyEvent.VK_SUBTRACT, '-'),
		Decimal			(java.awt.event.KeyEvent.VK_DECIMAL, '.'),
		Divide			(java.awt.event.KeyEvent.VK_DIVIDE, '/'),
		Delete			(java.awt.event.KeyEvent.VK_DELETE),
		NumLock			(java.awt.event.KeyEvent.VK_NUM_LOCK),
		ScrollLock		(java.awt.event.KeyEvent.VK_SCROLL_LOCK),
		F1		(java.awt.event.KeyEvent.VK_F1),
		F2		(java.awt.event.KeyEvent.VK_F2),
		F3		(java.awt.event.KeyEvent.VK_F3),
		F4		(java.awt.event.KeyEvent.VK_F4),
		F5		(java.awt.event.KeyEvent.VK_F5),
		F6		(java.awt.event.KeyEvent.VK_F6),
		F7		(java.awt.event.KeyEvent.VK_F7),
		F8		(java.awt.event.KeyEvent.VK_F8),
		F9		(java.awt.event.KeyEvent.VK_F9),
		F10		(java.awt.event.KeyEvent.VK_F10),
		F11		(java.awt.event.KeyEvent.VK_F11),
		F12		(java.awt.event.KeyEvent.VK_F12),
		F13		(java.awt.event.KeyEvent.VK_F13),
		F14		(java.awt.event.KeyEvent.VK_F14),
		F15		(java.awt.event.KeyEvent.VK_F15),
		F16		(java.awt.event.KeyEvent.VK_F16),
		F17		(java.awt.event.KeyEvent.VK_F17),
		F18		(java.awt.event.KeyEvent.VK_F18),
		F19		(java.awt.event.KeyEvent.VK_F19),
		F20		(java.awt.event.KeyEvent.VK_F20),
		F21		(java.awt.event.KeyEvent.VK_F21),
		F22		(java.awt.event.KeyEvent.VK_F22),
		F23		(java.awt.event.KeyEvent.VK_F23),
		F24		(java.awt.event.KeyEvent.VK_F24),
		Printscreen	(java.awt.event.KeyEvent.VK_PRINTSCREEN),
		Insert		(java.awt.event.KeyEvent.VK_INSERT),
		Help		(java.awt.event.KeyEvent.VK_HELP),
		Meta		(java.awt.event.KeyEvent.VK_META),
		BackQuote	(java.awt.event.KeyEvent.VK_BACK_QUOTE, '`'),
		Quote		(java.awt.event.KeyEvent.VK_QUOTE, '\''),
		KpUp		(java.awt.event.KeyEvent.VK_KP_UP),
		KpDown		(java.awt.event.KeyEvent.VK_KP_DOWN),
		KpLeft		(java.awt.event.KeyEvent.VK_KP_LEFT),
		KpRight		(java.awt.event.KeyEvent.VK_KP_RIGHT),
		DeadGrave		(java.awt.event.KeyEvent.VK_DEAD_GRAVE),
		DeadAcute		(java.awt.event.KeyEvent.VK_DEAD_ACUTE),
		DeadCircumflex	(java.awt.event.KeyEvent.VK_DEAD_CIRCUMFLEX),
		DeadTilde		(java.awt.event.KeyEvent.VK_DEAD_TILDE, '~'),
		DeadMacron		(java.awt.event.KeyEvent.VK_DEAD_MACRON),
		DeadBreve		(java.awt.event.KeyEvent.VK_DEAD_BREVE),
		DeadAbovedot		(java.awt.event.KeyEvent.VK_DEAD_ABOVEDOT),
		DeadDiaeresis		(java.awt.event.KeyEvent.VK_DEAD_DIAERESIS),
		DeadAbovering		(java.awt.event.KeyEvent.VK_DEAD_ABOVERING),
		DeadDoubleacute		(java.awt.event.KeyEvent.VK_DEAD_DOUBLEACUTE),
		DeadCaron		(java.awt.event.KeyEvent.VK_DEAD_CARON),
		DeadCedilla		(java.awt.event.KeyEvent.VK_DEAD_CEDILLA),
		DeadOgonek		(java.awt.event.KeyEvent.VK_DEAD_OGONEK),
		DeadIota		(java.awt.event.KeyEvent.VK_DEAD_IOTA),
		DeadVoicedSound		(java.awt.event.KeyEvent.VK_DEAD_VOICED_SOUND),
		DeadSemivoicedSound	(java.awt.event.KeyEvent.VK_DEAD_SEMIVOICED_SOUND),
		Ampersand		(java.awt.event.KeyEvent.VK_AMPERSAND, '&'),
		Asterisk		(java.awt.event.KeyEvent.VK_ASTERISK, '*'),
		Quotedbl		(java.awt.event.KeyEvent.VK_QUOTEDBL, '"'),
		Less			(java.awt.event.KeyEvent.VK_LESS, '<'),
		Greater			(java.awt.event.KeyEvent.VK_GREATER, '>'),
		Braceleft		(java.awt.event.KeyEvent.VK_BRACELEFT, '{'),
		Braceright		(java.awt.event.KeyEvent.VK_BRACERIGHT, '}'),
		At				(java.awt.event.KeyEvent.VK_AT, '@'),
		Colon			(java.awt.event.KeyEvent.VK_COLON, ':'),
		Circumflex		(java.awt.event.KeyEvent.VK_CIRCUMFLEX),
		Dollar			(java.awt.event.KeyEvent.VK_DOLLAR, '$'),
		EuroSign		(java.awt.event.KeyEvent.VK_EURO_SIGN),
		ExclamationMark			(java.awt.event.KeyEvent.VK_EXCLAMATION_MARK, '!'),
		InvertedExclamationMark	(java.awt.event.KeyEvent.VK_INVERTED_EXCLAMATION_MARK),
		LeftParenthesis			(java.awt.event.KeyEvent.VK_LEFT_PARENTHESIS, '('),
		NumberSign		(java.awt.event.KeyEvent.VK_NUMBER_SIGN, '#'),
		Plus			(java.awt.event.KeyEvent.VK_PLUS, '+'),
		RightParenthesis(java.awt.event.KeyEvent.VK_RIGHT_PARENTHESIS, ')'),
		Underscore		(java.awt.event.KeyEvent.VK_UNDERSCORE, '_'),
		Windows			(java.awt.event.KeyEvent.VK_WINDOWS),
		ContextMenu		(java.awt.event.KeyEvent.VK_CONTEXT_MENU),
		Final			(java.awt.event.KeyEvent.VK_FINAL),
		Convert			(java.awt.event.KeyEvent.VK_CONVERT),
		Nonconvert		(java.awt.event.KeyEvent.VK_NONCONVERT),
		Accept			(java.awt.event.KeyEvent.VK_ACCEPT),
		Modechange		(java.awt.event.KeyEvent.VK_MODECHANGE),
		Kana			(java.awt.event.KeyEvent.VK_KANA),
		Kanji			(java.awt.event.KeyEvent.VK_KANJI),
		Alphanumeric	(java.awt.event.KeyEvent.VK_ALPHANUMERIC),
		Katakana		(java.awt.event.KeyEvent.VK_KATAKANA),
		Hiragana		(java.awt.event.KeyEvent.VK_HIRAGANA),
		FullWidth		(java.awt.event.KeyEvent.VK_FULL_WIDTH),
		HalfWidth		(java.awt.event.KeyEvent.VK_HALF_WIDTH),
		RomanCharacters	(java.awt.event.KeyEvent.VK_ROMAN_CHARACTERS),
		AllCandidates	(java.awt.event.KeyEvent.VK_ALL_CANDIDATES),
		PreviousCandidate	(java.awt.event.KeyEvent.VK_PREVIOUS_CANDIDATE),
		CodeInput			(java.awt.event.KeyEvent.VK_CODE_INPUT),
		JapaneseKatakana	(java.awt.event.KeyEvent.VK_JAPANESE_KATAKANA),
		JapaneseHiragana	(java.awt.event.KeyEvent.VK_JAPANESE_HIRAGANA),
		JapaneseRoman		(java.awt.event.KeyEvent.VK_JAPANESE_ROMAN),
		KanaLock			(java.awt.event.KeyEvent.VK_KANA_LOCK),
		InputMethodOnOff	(java.awt.event.KeyEvent.VK_INPUT_METHOD_ON_OFF),
		Cut			(java.awt.event.KeyEvent.VK_CUT),
		Copy		(java.awt.event.KeyEvent.VK_COPY),
		Paste		(java.awt.event.KeyEvent.VK_PASTE),
		Undo		(java.awt.event.KeyEvent.VK_UNDO),
		Again		(java.awt.event.KeyEvent.VK_AGAIN),
		Find		(java.awt.event.KeyEvent.VK_FIND),
		Props		(java.awt.event.KeyEvent.VK_PROPS),
		Stop		(java.awt.event.KeyEvent.VK_STOP),
		Compose		(java.awt.event.KeyEvent.VK_COMPOSE),
		AltGraph	(java.awt.event.KeyEvent.VK_ALT_GRAPH),
		Begin		(java.awt.event.KeyEvent.VK_BEGIN),
		Undefined	(java.awt.event.KeyEvent.VK_UNDEFINED);
		
		private Key(int code) {
			mCode = code;
			mChar = '\0';
		}
		
		private Key(int code, char print) {
			mCode = code;
			mChar = print;
		}
		
		public int getCode() {
			return mCode;
		}
		
		public boolean getPrintable() {
			return mChar != '\0';
		}
		
		public char getChar() {
			assert mChar != '\0' : "Key is not printable";
			return mChar;
		}
		
		public char getUChar() {
			assert mChar != '\0' : "Key is not printable";
			if (mChar >= 'a' && mChar <= 'z') {
				return Character.toUpperCase(mChar);
			}
			return mChar;
		}
		
		public static Key translateCode(int code) {
			return mKeyMap.get(code);
		}
		
		private int mCode;
		private char mChar;
		private static HashMap<Integer, Key> mKeyMap = new HashMap<Integer, Key>();	
		
		//init meymap
		static {
			for (Key k : values()) {
				mKeyMap.put(k.getCode(), k);
			}
		}
	}
	
	public static interface Listener {
		public void onKeyEvent(KeyEvent e);
	}
	
	public static class Signal {
		public void connect(Listener l) {
			mListeners.add(l);
		}
		
		public void fire(KeyEvent e) {
			for (Listener l : mListeners) {
				l.onKeyEvent(e);
				if (e.consumed())
					break;
			}
		}
		
		private ArrayList<Listener> mListeners = new ArrayList<Listener>();
	}
	
	public KeyEvent(KeyEventType ty, Input input, Key key) {
		super(Event.EventType.Key, input);
		mKey = key;
		mKeyEventType = ty;
	}
	
	public KeyEventType getKeyEventType() {
		return mKeyEventType;
	}
	
	public Key getKey() {
		return mKey;
	}
	
	private Key mKey;
	private KeyEventType mKeyEventType;
}
