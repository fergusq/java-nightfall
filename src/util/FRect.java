package util;

public final class FRect {
	public FRect(FVec pos, FVec size) {
		mX = pos.getX();
		mY = pos.getY();
		mW = size.getX();
		mH = size.getY();
	}
	
	public FRect(float x, float y, float w, float h) {
		mX = x;
		mY = y;
		mW = w;
		mH = h;
	}
	
	public float getWidth() {
		return mW;
	}
	
	public float getHeight() {
		return mH;
	}
	
	public FVec getSize() {
		return new FVec(mW, mH);
	}
	
	public float getX() {
		return mX;
	}
	
	public float getY() {
		return mY;
	}
	
	public FVec getPos() {
		return new FVec(mX, mY);
	}
	
	public boolean contains(FVec v) {
		return v.getX() >= mX && v.getY() >= mY && v.getX() <= mX + mW && v.getY() <= mY + mH;
	}
	
	public Rect rect() {
		return new Rect((int)mX, (int)mY, (int)mW, (int)mH);
	}
	
	private float mX, mY, mW, mH;
}
