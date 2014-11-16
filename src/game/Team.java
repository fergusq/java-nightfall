package game;

import java.awt.Color;
import java.util.ArrayList;

public class Team {
	public static interface ListenerBeginTurn {
		public void onTurn();
	}
	public static class SignalBeginTurn {
		public void connect(ListenerBeginTurn t) {
			mListeners.add(t);
		}
		public void fire() {
			for (ListenerBeginTurn l : mListeners)
				l.onTurn();
		}
		ArrayList<ListenerBeginTurn> mListeners = new ArrayList<ListenerBeginTurn>();
	}
	
	public Team() {
		
	}
	
	public Color getColor() {
		return mColor;
	}
	
	public void setColor(Color c) {
		mColor = c;
	}

	public Inventory getInventory() {
		return mInventory;
	}

	public void setInventory(Inventory i) {
		mInventory = i;
	}
	
	public boolean isAlly(Team other) {
		return false;
	}
	
	public int getNumAgents() {
		return mNumAgents;
	}
	
	public void setNumAgents(int n) {
		mNumAgents = n;
	}

	public void collectCredit(int credit) {
		mTmpCredit += credit;
	}

	public void keepCredits(boolean keep) {
		if (keep) mInventory.modCredit(mTmpCredit);
		mTmpCredit = 0;
	}

	public int getCollectedCredit() {
		return mTmpCredit;
	}

	private int mTmpCredit;
	
	private int mNumAgents = 0;
	private Color mColor;
	private Inventory mInventory = null;
	public final SignalBeginTurn onBeginTurn = new SignalBeginTurn();
}
