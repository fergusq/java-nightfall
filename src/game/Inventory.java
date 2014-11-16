package game;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
	public static class InventoryEntry {
		public InventoryEntry(AgentInfo a) {
			Agent = a;
		}
		public int Quantity = 0;
		public AgentInfo Agent;
	}
	
	public void addAgent(AgentInfo info) {
		InventoryEntry entry = mEntryLookup.get(info);
		if (entry == null) {
			entry = new InventoryEntry(info);
			mEntryLookup.put(info, entry);
			mAgents.add(entry);
		}
		entry.Quantity++;
	}
	
	public ArrayList<InventoryEntry> getAgents() {
		return mAgents;
	}
	
	public int getCredit() {
		return mCredit;
	}
	
	public void setCredit(int c) {
		mCredit = c;
	}
	
	public void modCredit(int delta) {
		mCredit += delta;
	}
	
	private ArrayList<InventoryEntry> mAgents = new ArrayList<InventoryEntry>();
	private HashMap<AgentInfo, InventoryEntry> mEntryLookup = new HashMap<AgentInfo, InventoryEntry>();
	private int mCredit = 0;
}
