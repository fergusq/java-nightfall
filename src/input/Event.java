package input;

import java.util.ArrayList;

public class Event {
	public static enum EventType {
		Key(1<<0),
		Mouse(1<<1),
		Custom(1<<2);
		
		private EventType(int mask) {
			mMask = mask;
		}
		
		public int getMask() {
			return mMask;
		}
		
		private int mMask;
	}
	
	public static abstract class Listener {
		public Listener(int mask) {mMask = mask;}
		public int getMask() {return mMask;} //accepted event types
		public abstract void onEvent(Event e);
		private int mMask;
	}
	
	public static class Signal {
		public void connect(Listener l) {
			mListeners.add(l);
		}
		
		public void fire(Event e) {
			for (Listener l : mListeners)
				if ((e.getEventMask() & l.getMask()) != 0) {
					l.onEvent(e);
					if (e.consumed())
						break;
				}
		}
		
		private ArrayList<Listener> mListeners = new ArrayList<Listener>();
	}
	
	public Event(EventType type, Input input) {
		mEventType = type;
		mInput = input;
	}
	
	public EventType getEventType() {
		return mEventType;
	}
	
	public int getEventMask() {
		return mEventType.getMask();
	}
	
	public boolean consumed() {
		return mConsumed;
	}
	
	public void consume() {
		mConsumed = true;
	}
	
	public Input getInput() {
		return mInput;
	}
	
	private Input mInput;
	private boolean mConsumed = false;
	private EventType mEventType;
}











