package util;

public class UDim {
	public UDim() {
		this(0, 0, 0, 0);
	}
	public UDim(FVec offset) {
		this(offset.getX(), offset.getY(), 0, 0);
	}
	public UDim(float offsetx, float offsety) {
		this(offsetx, offsety, 0, 0);
	}
	public UDim(FVec offset, FVec scale) {
		this(offset.getX(), offset.getY(), scale.getX(), scale.getY());
	}
	public UDim(float offsetx, float offsety, float scalex, float scaley) {
		mXOffset = offsetx;
		mYOffset = offsety;
		mXScale = scalex;
		mYScale = scaley;
	}
	
	public FVec getOffset() {
		return new FVec(mXOffset, mYOffset);
	}
	
	public float getXOffset() {
		return mXOffset;
	}

	public float getYOffset() {
		return mYOffset;
	}
	
	public FVec getScale() {
		return new FVec(mXScale, mYScale);
	}

	public float getXScale() {
		return mXScale;
	}

	public float getYScale() {
		return mYScale;
	}

	private float mXOffset, mYOffset, mXScale, mYScale;
}
