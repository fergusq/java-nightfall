package game;

import util.Vec;

public class DataBattleInfo {
	public static class CreditEntry {
		Vec Pos;
		int Amount;
	}
	
	public static class UnitEntry {
		Vec Pos;
		AgentInfo Info;
	}
	
	public static class UploadEntry {
		Vec Pos;
	}

	public static class DataEntry {
		Vec Pos;
	}
	
	public Vec getBoardSize() {
		return mBoardSize;
	}
	
	public void setBoardSize(Vec v) {
		mBoardSize = v;
		mFilled = new boolean[v.getX()][v.getY()];
	}
	
	public void setBoardSize(int w, int h) {
		mBoardSize = new Vec(w, h); 
		mFilled = new boolean[w][h];
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getName() {
		return mName;
	}

	public DataEntry[] getDatas() {
		return mData;
	}

	public void setDatas(DataEntry[] datas) {
		mData = datas;
	}
	
	public CreditEntry[] getCredits() {
		return mCredits;
	}

	public void setCredits(CreditEntry[] credits) {
		mCredits = credits;
	}

	public UnitEntry[] getUnits() {
		return mUnits;
	}

	public void setUnits(UnitEntry[] units) {
		mUnits = units;
	}

	public UploadEntry[] getUploads() {
		return mUploads;
	}

	public void setUploads(UploadEntry[] uploads) {
		mUploads = uploads;
	}
	
	public boolean getFilled(int x, int y) {
		return mFilled[x][y];
	}
	
	public boolean getFilled(Vec v) {
		return getFilled(v.getX(), v.getY());
	}
	
	public void setFilled(int x, int y, boolean state) {
		mFilled[x][y] = state;
	}
	
	public void setFilled(Vec v, boolean state) {
		setFilled(v.getX(), v.getY(), state);
	}

	public int getBonus() {
		return mBonus;
	}

	public void setBonus(int b) {
		mBonus = b;
	}

	private CreditEntry[] mCredits;
	private UnitEntry[] mUnits;
	private UploadEntry[] mUploads;
	private DataEntry[] mData;
	private boolean[][] mFilled;
	private String mName;
	private Vec mBoardSize;

	private int mBonus;
}
