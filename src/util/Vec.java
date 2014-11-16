package util;

public class Vec {
	public Vec() {
		mX = mY = 0;
	}
	public Vec(int x, int y) {
		mX = x;
		mY = y;
	}
	public int getX() {
		return mX;
	}
	public int getY() {
		return mY;
	}
	public Vec add(Vec other) {
		return new Vec(mX + other.mX, mY + other.mY);
	}
	public Vec sub(Vec other) {
		return new Vec(mX - other.mX, mY - other.mY);
	}
	public Vec mul(int other) {
		return new Vec(mX*other, mY*other);
	}
	public Vec div(int other) {
		return new Vec(mX/other, mY/other);
	}
	public Vec neg() {
		return new Vec(-mX, -mY);
	}
	public boolean eq(Vec other) {
		return mX == other.mX && mY == other.mY;
	}
	public boolean eq(int x, int y) {
		return mX == x && mY == y;
	}
	public FVec fvec() {
		return new FVec((float)mX, (float)mY);
	}
	public static Vec[] getDirs() {
		return mDirs;
	}
	public static Vec[] getHorizontalDirs() {
		return mHorizontalDirs;
	}
	public static Vec[] getVerticalDirs() {
		return mVerticalDirs;
	}
	private int mX, mY;
	private static final Vec[] mDirs = {new Vec(0, 1), new Vec(0, -1), new Vec(1, 0), new Vec(-1, 0)};
	private static final Vec[] mVerticalDirs = {new Vec(0, 1), new Vec(0, -1)};
	private static final Vec[] mHorizontalDirs = {new Vec(1, 0), new Vec(-1, 0)};
	
	public String toString() {
		return "Vec(" + mX + ", " + mY + ")";
	}
}
