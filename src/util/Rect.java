package util;

public final class Rect {
	public Rect(Vec pos, Vec size) {
		mX = pos.getX();
		mY = pos.getY();
		mW = size.getX();
		mH = size.getY();
	}
	
	public Rect(int x, int y, int w, int h) {
		mX = x;
		mY = y;
		mW = w;
		mH = h;
	}
	
	public int getWidth() {
		return mW;
	}
	
	public int getHeight() {
		return mH;
	}
	
	public Vec getSize() {
		return new Vec(mW, mH);
	}
	
	public int getX() {
		return mX;
	}
	
	public int getY() {
		return mY;
	}
	
	public Vec getPos() {
		return new Vec(mX, mY);
	}
	
	public boolean contains(Vec v) {
		return v.getX() >= mX && v.getY() >= mY && v.getX() <= mX + mW && v.getY() <= mY + mH;
	}
	
	public FRect frect() {
		return new FRect((float)mX, (float)mY, (float)mW, (float)mH);
	}
	
	private int mX, mY, mW, mH;
}
