package input;

import util.Vec;
import gui.Widget;

public class Input {
	public void setKeyState(KeyEvent.Key key, boolean state) {
		int kc = key.getCode();
		assert kc < NUM_KEYS && kc >= 0 : "Bad KeyCode " + kc;
		mKeyState[kc] = state;
	}
	
	public boolean getKeyState(KeyEvent.Key key) {
		int kc = key.getCode();
		assert kc < NUM_KEYS && kc >= 0 : "Bad KeyCode " + kc;
		return mKeyState[kc];
	}
	
	public void setMousePos(Vec pos) {
		mMousePos = pos;
	}
	
	public Vec getMousePos() {
		return mMousePos;
	}
	
	public void setButton(MouseEvent.MouseButton bn, boolean state) {
		mButtonState[bn.getNum()] = state;
	}
	
	public boolean getButton(MouseEvent.MouseButton bn) {
		return mButtonState[bn.getNum()];
	}
	
	public void setMouseTarget(Widget w) {
		mMouseTarget = w;
	}
	
	public Widget getMouseTarget() {
		return mMouseTarget;
	}
	
	public void setKeyTarget(Widget w) {
		mKeyTarget = w;
	}
	
	public Widget getKeyTarget() {
		return mKeyTarget;
	}
	
	public void setPrevMouseTarget() {
		mPrevMouseTarget = mMouseTarget;
	}
	
	public Widget getPrevMouseTarget() {
		return mPrevMouseTarget;
	}
	
	private final static int NUM_BUTTON = 3;
	private final static int NUM_KEYS = 512;
	private boolean[] mKeyState = new boolean[NUM_KEYS];
	private boolean[] mButtonState = new boolean[NUM_BUTTON];
	private Vec mMousePos = new Vec();
	//
	private Widget mMouseTarget; //current target
	private Widget mPrevMouseTarget; //old target, for mouse-leave purposes
	private Widget mKeyTarget; //current selection
}
