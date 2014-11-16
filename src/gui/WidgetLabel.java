package gui;

import util.*;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Graphics;

public class WidgetLabel extends WidgetRect {
	public void onRender(RenderTarget t) {
		Graphics g = t.getContext();
		//g.setFont(new Font("Liberation Mono", 0, 14));
		if (!mTextBoundsValid) {
			Rect r = getRect();
			FontMetrics fm = g.getFontMetrics();
			java.awt.geom.Rectangle2D fontr = fm.getStringBounds(mText, g);
			if (r.getWidth() < fm.charWidth(ELIPSES)) {
				//draw nothing, too small
				mTextBounds = r;
				mStringToDraw = "";
			} else if (fontr.getWidth() > r.getWidth()) {
				//draw start of string and the `...` in place of what can't fit
				int h = fm.getHeight();
				int availw = r.getWidth() - fm.charWidth(ELIPSES);
				for (int i = 0; i < mText.length(); ++i) {
					char c = mText.charAt(i);
					int cw = fm.charWidth(c);
					if (availw - cw < 0) {
						if (i == 0) {
							mStringToDraw = ""+ELIPSES;
						} else {
							mStringToDraw = mText.substring(0, i) + ELIPSES;
						}
						mTextBounds = new Rect(r.getX() + availw/2, 
												r.getY() + r.getHeight()/2 - h/2,
												r.getWidth() - availw, 
												h);
						break;
					} else {
						availw -= cw;
					}
				}
			} else {
				//good, it fits!
				mTextBounds = new Rect((int)(r.getX() + r.getWidth()/2 - fontr.getWidth()/2), 
										(int)(r.getY() + r.getHeight()/2 - fontr.getHeight()/2),
										(int)(fontr.getWidth()),
										(int)(fontr.getHeight()));
				mStringToDraw = mText;
			}
			mTextBoundsValid = true;
		}
		super.onRender(t);
		g.setColor(mTextColor);
		g.drawString(mStringToDraw, mTextBounds.getX(), mTextBounds.getY()+mTextBounds.getHeight());
		
	}
	
	public void onLayout() {
		mTextBoundsValid = false;
	}
	
	public void setText(String s) {
		mText = s;
		mTextBoundsValid = false;
	}
	
	public String getText() {
		return mText;
	}
	
	public void setTextColor(Color c) {
		mTextColor = c;
	}
	
	public Color getColor() {
		return mTextColor;
	}
	
	private String mText = "";
	private Color mTextColor = Color.black;
	//
	private Rect mTextBounds;
	private String mStringToDraw;
	//
	private boolean mTextBoundsValid = false;
	//
	private final static char ELIPSES = '\u2026';
}
