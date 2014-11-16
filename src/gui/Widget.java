package gui;

import java.util.ArrayList;
import java.util.Collection;

import input.*;
import input.Event.EventType;
import input.MouseEvent.MouseEventType;
import input.KeyEvent.KeyEventType;
import util.*;

public class Widget {
	//public final static int NUM_LAYERS = 10;
	
	protected void onRender(RenderTarget t) {}
	protected void onLayout() {}
	
	protected final void internalOnEvent(Event e) {
		//not visible, break off
		if (!mVisible) 
			return;
		
		//make up to date
		if (!mRectValid)
			internalOnLayout(false);
		
		//clear any pending child removals, event handling may remove some children
		removePendingChildren();
		
		//do children, last added is on top, so it gets a chance 
		//at the event first
		mChildLock = true;
		for (int i = mChildren.size()-1; i >= 0; --i) {
			mChildren.get(i).internalOnEvent(e);
			if (e.consumed())
				return;
		}
		mChildLock = false;
		
		//if mouse is over, handle event
		if (contains(e.getInput().getMousePos())) {
			//active? consume the event
			if (mActive)
				e.consume();
			
			//set the input's MouseTarget to this
			e.getInput().setMouseTarget(this);
			
			//dispatch
			if (e.getEventType() == EventType.Mouse) {
				MouseEvent me = (MouseEvent)e;
				switch (me.getMouseEventType()) {
				case Press:
					//clicking on an active widget sets it to the target for keypresses
					e.getInput().setKeyTarget(this);
					onMouseDown.fire(me);
					break;
				case Release:
					onMouseUp.fire(me);
					break;
				case Scroll:
					onMouseWheel.fire(me);
					break;
				case Move:
					onMouseMove.fire(me);
					break;
				default:
					assert false: "Bad MouseEventType to internalOnEvent";
				}
			} else if (e.getEventType() == EventType.Key) {
				KeyEvent ke = (KeyEvent)e;
				switch (ke.getKeyEventType()) {
				case Press:
					onKeyDown.fire(ke);
					break;
				case Release:
					onKeyUp.fire(ke);
					break;
				default:
					assert false: "Bad KeyEventType to onEventInternal";
				}
			}
		}
	}
	
	protected final void internalOnRender(RenderTarget t) {
		//not visible, don't render
		if (!mVisible)
			return;
		
		//make up to date
		if (!mRectValid)
			internalOnLayout(false);
		
		//render self first
		onRender(t);
		
		//children, in order added
		//(don't clear pending child removals, there shouldn't be any from a render)
		mChildLock = true;
		for (Widget w : mChildren) {
			w.internalOnRender(t);
		}
		mChildLock = false;
	}
	
	protected final void internalOnLayout(boolean forceupdate) {
		//update self
		if (!mRectValid || forceupdate) {
			if (mParent != null) {
				int pw = mParent.getRect().getWidth();
				int ph = mParent.getRect().getHeight();
				mRect = new Rect((int)(mParent.getRect().getX() + mPos.getXOffset() + mPos.getXScale()*pw), 
								(int)(mParent.getRect().getY() + mPos.getYOffset() + mPos.getYScale()*ph), 
								(int)(mSize.getXOffset() + pw*mSize.getXScale()), 
								(int)(mSize.getYOffset() + ph*mSize.getYScale()));
			} else {
				mRect = new Rect(mPos.getOffset().vec(), mSize.getOffset().vec());
			}
			onLayout();
		}
		
		//update children
		// (don't clear pending child removals, there shouldn't be any from a layout)
		mChildLock = true;
		for (Widget child : mChildren) {
			child.internalOnLayout(forceupdate);
		}
		mChildLock = false;
	}
	
	public boolean contains(Vec v) {
		return mRect.contains(v);
	}
	
	public final void setParent(Widget parent) {
		if (mParent != null) {
			if (mParent.mChildLock) { //child list being iterated? don't mess with it
				mParent.mChildrenToRemove.add(this);
			} else {
				mParent.mChildren.remove(this);
			}
		}
		mParent = parent;
		if (mParent != null) {
			mParent.mChildren.add(this);
		}
	}
	
	public final Widget getParent() {
		return mParent;
	}
	
	public final UDim getPos() {
		return mPos;
	}
	
	public final void setPos(UDim p) {
		mPos = p;
		mRectValid = false;
	}
	
	public final UDim getSize() {
		return mSize;
	}
	
	public final void setSize(UDim s) {
		mSize = s;
		mRectValid = false;
	}

	public final void setLayer(int layer) {
		mLayer = layer;
	}

	public final int getLayer() {
		return mLayer;
	}
	
	public final boolean getActive() {
		return mActive;
	}
	
	public final void setActive(boolean active) {
		mActive = active;
	}
	
	public final boolean getVisible() {
		return mVisible;
	}
	
	public final void setVisible(boolean visible) {
		mVisible = visible;
	}
	
	public final Rect getRect() {
		return mRect;
	}
	
	public final void invalidate() {
		mRectValid = false;
	}
	
	public final Collection<Widget> getChildren() {
		return java.util.Collections.unmodifiableCollection(mChildren);
	}
	
	//events
	public final MouseEvent.Signal onMouseEnter = new MouseEvent.Signal();
	public final MouseEvent.Signal onMouseLeave = new MouseEvent.Signal();
	public final MouseEvent.Signal onMouseMove = new MouseEvent.Signal();
	public final MouseEvent.Signal onMouseDown = new MouseEvent.Signal();
	public final MouseEvent.Signal onMouseUp = new MouseEvent.Signal();
	public final MouseEvent.Signal onMouseWheel = new MouseEvent.Signal();
	public final KeyEvent.Signal onKeyDown = new KeyEvent.Signal();
	public final KeyEvent.Signal onKeyUp = new KeyEvent.Signal();
	
	//herarchy
	private Widget mParent;
	private ArrayList<Widget> mChildren = new ArrayList<Widget>();
	private boolean mChildLock = false;
	private ArrayList<Widget> mChildrenToRemove = new ArrayList<Widget>();
	private void removePendingChildren() {
		assert mChildLock == false;
		if (!mChildrenToRemove.isEmpty()) {
			mChildren.removeAll(mChildrenToRemove);
			mChildrenToRemove.clear();
		}
	}
	
	//layer
	private int mLayer;
	
	//layout
	private UDim mSize = new UDim();
	private UDim mPos = new UDim();
	private Rect mRect;
	private boolean mRectValid = false;
	
	//other state
	private boolean mVisible = true;
	private boolean mActive = true;
}






