package input;

import java.util.ArrayList;

public class MouseEvent extends Event {
	public static enum MouseEventType {
		Move	(1<<0),
		Enter	(1<<1),
		Leave	(1<<2),
		Press	(1<<3),
		Release	(1<<4),
		Scroll	(1<<5);
		
		public static final int ButtonMask = Press.getMask() | Release.getMask();
		public static final int MotionMask = Move.getMask() | Enter.getMask() | Leave.getMask();
		public static final int ScrollMask = Scroll.getMask();
		
		private MouseEventType(int mask) {
			mMask = mask;
		}
		
		public int getMask() {
			return mMask;
		}
		
		private int mMask;
	}
	
	public static enum MouseButton {
		Left	(0),
		Center	(1),
		Right	(2);
		
		private MouseButton(int num) {
			mNum = num;
		}
		
		public int getNum() {
			return mNum;
		}
		
		private int mNum;
	}
	
	public static interface Listener {
		public void onMouseEvent(MouseEvent e);
	}
	
	public static class Signal {	
		public void connect(Listener l) {
			mListeners.add(l);
		}
		
		public void fire(MouseEvent e) {
			for (Listener l : mListeners) {
				l.onMouseEvent(e);
				if (e.consumed())
					break;
			}
		}

		private ArrayList<Listener> mListeners = new ArrayList<Listener>();
	}
	
	public MouseEvent(MouseEventType ty, Input input) {
		super(Event.EventType.Mouse, input);
		assert (ty.getMask() & MouseEventType.MotionMask) != 0;
		mMouseEventType = ty;
	}
	
	public MouseEvent(MouseEventType ty, Input input, MouseButton bn) {
		super(Event.EventType.Mouse, input);
		assert (ty.getMask() & MouseEventType.ButtonMask) != 0;
		mMouseEventType = ty;
		mButton = bn;
	}
	
	public MouseEvent(MouseEventType ty, Input input, int scrolldelta) {
		super(Event.EventType.Mouse, input);
		assert (ty.getMask() & MouseEventType.ScrollMask) != 0;
		mMouseEventType = ty;
		mScrollDelta = scrolldelta;
	}
	
	public MouseEventType getMouseEventType() {
		return mMouseEventType;
	}
	
	public MouseButton getButton() {
		assert mButton != null;
		return mButton;
	}
	
	public int getScrollDelta() {
		assert mScrollDelta != 0;
		return mScrollDelta;
	}
	
	private int mScrollDelta = 0;
	private MouseButton mButton = null;
	private MouseEventType mMouseEventType;
}
