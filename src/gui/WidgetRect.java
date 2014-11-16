package gui;

import java.awt.*;
import util.*;

public class WidgetRect extends Widget {
	public void onRender(RenderTarget t) {
		Graphics g = t.getContext();
		Rect r = getRect();
		if (mBackground) {
			g.setColor(mColor);
			g.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
		}
	}
	
	public void setColor(Color c) {
		mColor = c;
	}
	
	public Color getColor() {
		return mColor;
	}
	
	public void setBackground(boolean state) {
		mBackground = state;
	}
	
	public boolean getBackground() {
		return mBackground;
	}
	
	private boolean mBackground = true;
	private Color mColor = Color.blue;
}
