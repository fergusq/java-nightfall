package gui;

import util.UDim;

public class WidgetPadding extends Widget {
	public static void applyPadding(Widget w, int p) {
		applyPadding(w, p, p, p, p);
	}
	
	public static void applyPadding(Widget w, int r, int l, int t, int b) {
		UDim wsz = w.getSize();
		UDim wp = w.getPos();
		w.setSize(new UDim(wsz.getXOffset()-r-l, wsz.getYOffset()-t-b, 
				wsz.getXScale(), wsz.getYScale()));
		w.setPos(new UDim(wp.getXOffset()+l, wp.getYOffset()+t, 
				wp.getXScale(), wp.getYScale()));
	}
	
	public WidgetPadding(Widget w) {
		this(w, 0, 0, 0, 0);
	}
	
	public WidgetPadding(Widget w, int padding) {
		this(w, padding, padding, padding, padding);
	}
	
	public WidgetPadding(Widget w, int right, int left, int top, int bottom) {
		mPRight = right;
		mPLeft = left;
		mPTop = top;
		mPBottom = bottom;
		UDim wsz = w.getSize();
		UDim wp = w.getPos();
		setSize(wsz);
		setPos(wp);
		w.setSize(new UDim(-mPRight-mPLeft, -mPTop-mPBottom, 1, 1));
		w.setPos(new UDim(mPLeft, mPBottom, 0, 0));
	}
	
	private int mPBottom, mPTop, mPRight, mPLeft;
}
