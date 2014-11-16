package util;

public final class FVec {
	public FVec() {
		mX = mY = 0;
	}
	public FVec(float x, float y) {
		mX = x;
		mY = y;
	}
	public float getX() {
		return mX;
	}
	public float getY() {
		return mY;
	}
	public FVec add(FVec other) {
		return new FVec(mX + other.mX, mY + other.mY);
	}
	public FVec sub(FVec other) {
		return new FVec(mX - other.mX, mY - other.mY);
	}
	public FVec mul(float other) {
		return new FVec(mX*other, mY*other);
	}
	public FVec div(float other) {
		return new FVec(mX/other, mY/other);
	}
	public FVec neg() {
		return new FVec(-mX, -mY);
	}
	public boolean eq(FVec other) {
		return mX == other.mX && mY == other.mY;
	}
	public boolean eq(float x, float y) {
		return mX == x && mY == y;
	}
	public Vec vec() {
		return new Vec((int)mX, (int)mY);
	}
	public static FVec[] getDirs() {
		return mDirs;
	}
	private float mX, mY;
	private static final FVec[] mDirs = {new FVec(0, 1), new FVec(0, -1), new FVec(1, 0), new FVec(-1, 0)};
	
	public String toString() {
		return "FVec(" + mX + ", " + mY + ")";
	}
}

