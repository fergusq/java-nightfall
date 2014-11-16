package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Font;
import java.util.ArrayList;

import util.*;

public class WidgetText extends WidgetRect {
	public enum TextAlign {
		Right,
		Left,
		Center;
	}
	
	//one line of text
	private class Line {
		public Vec Size;
		public String Text = "";
	}
	
	public void onRender(RenderTarget t) {
		if (!mTextBoundsValid)
			updateTextLayout(t.getContext().getFontMetrics());
		
		super.onRender(t);
		Rect r = getRect();
		Graphics g = t.getContext();
		g.setColor(mTextColor);
		//g.setFont(new Font("Liberation Mono", 0, 14));
		//
		int lny = r.getY();
		for (int i = 0; i < mTextLines.size(); ++i) {
			Line ln = mTextLines.get(i);
			lny += ln.Size.getY();
			int lnx;
			switch (mTextAlign) {
			case Right:
				lnx = r.getX() + r.getWidth() - ln.Size.getX();
				break;
			case Left:
				lnx = r.getX();
				break;
			case Center:
				lnx = r.getX() + r.getWidth()/2 - ln.Size.getX()/2;
				break;
			default:
				lnx = 0;
				break;
			}
			g.drawString(ln.Text, lnx, lny);
		}
	}
	
	public void onLayout() {
		mTextBoundsValid = false;
	}
	
	public void setText(String s) {
		mText = s;
	}
	
	public String getText() {
		return mText;
	}
	
	public void setTextColor(Color c) {
		mTextColor = c;
	}
	
	public Color getTextColor() {
		return mTextColor;
	}
	
	public void setTextAlign(TextAlign al) {
		mTextAlign = al;
	}
	
	public TextAlign getTextAlign() {
		return mTextAlign;
	}
	
	public void setEditable(boolean st) {
		assert !st: "Editable text controls not implemented yet.";
	}
	
	public boolean getEditable() {
		return mEditable;
	}
	
	private void updateTextLayout(FontMetrics fm) {
		mTextLines.clear();
		String[] lines = mText.split("\n");
		int totalH = 0;
		int largestW = 0;
		int lineH = fm.getHeight();
		int maxW = getRect().getWidth();
		for (String textln : lines) {
			Line line = new Line();
			totalH += lineH;
			int lineW = 0;
			String[] words = textln.split(" ");
			for (int i = 0; i < words.length; ++i) {
				String word = " "+words[i];
				int cw = 0;
				for (int j = 0; j < word.length(); j++) cw += fm.charWidth(word.charAt(j));
				if (lineW+cw > maxW) { //no more room? make a new line
					if (lineW > largestW)
						largestW = lineW;
					line.Size = new Vec(lineW, lineH);
					mTextLines.add(line);
					line = new Line();
					lineW = 0;
					totalH += lineH;
				}
				lineW += cw;
				line.Text = line.Text + word;
			}
			if (lineW > largestW)
				largestW = lineW;
			line.Size = new Vec(lineW, lineH);
			mTextLines.add(line);
		}
		mTextSize = new Vec(largestW, totalH);
		//
		mTextBoundsValid = true;
	}
	
	private boolean mEditable = false;
	private String mText = "";
	private Color mTextColor = Color.black;
	private TextAlign mTextAlign = TextAlign.Center;
	
	private boolean mTextBoundsValid = false;
	private ArrayList<Line> mTextLines = new ArrayList<Line>();
	private Vec mTextSize;
}


