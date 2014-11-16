package game;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

import util.*;

public class AIController {
	public class Tile {
		public boolean Filled;
		public Agent Agent;
		public int Credit;
		public Vec Pos;
		
		//all purpose `has been calculated`
		public boolean Flood;
		
		//astar stuff
		public int RangeTo; //TMP
		public boolean IsMove; //move or range?
		public Tile CameFrom;
		public int GScore;
		public int FScore;
		public int HScore;
		
//		//floodpaths stuff
//		public Tile CameFrom; //tile came from
//		public boolean Reachable; //can it be reached
//		public int MoveLeft;	//move it takes to get there
//		public int RangeLeft;	//range it takes to get there after moving
//		public boolean IsMove;
//		
//		public boolean betterMove(Tile other) {
//			if (MoveLeft < other.MoveLeft) {
//				return true;
//			} else if (MoveLeft == other.MoveLeft) {
//				return RangeLeft < other.RangeLeft;
//			} else {
//				return false;
//			}
//		}
	}
	
	public static class Path {
		public static class Node {
			public Node Next;
			public Tile Tile;
			public int RangeToGoal;
		}
		public Node Start;
		public Node End;
		public int Move = 0;
		public int Range = 0;
	}
	
	public AIController(DataBattle target, Team team) {
		mTarget = target;
		mTeam = team;
		mW = target.getSize().getX();
		mH = target.getSize().getY();
		mBoard = new Tile[mW][mH];
		for (int x = 0; x < mW; ++x) {
			for (int y = 0; y < mH; ++y) {
				DataBattle.Tile tl = mTarget.getTile(x, y);
				mBoard[x][y] = new Tile();
				mBoard[x][y].Agent = tl.Agent;
				mBoard[x][y].Filled = tl.Filled;
				mBoard[x][y].Credit = tl.Credit;
				mBoard[x][y].Pos = new Vec(x, y);
			}
		}		
	}
	
	public void clean() {
		for (int x = 0; x < mW; ++x) {
			for (int y = 0; y < mH; ++y) {
				DataBattle.Tile tl = mTarget.getTile(x, y);
				Tile mtl = mBoard[x][y];
				mtl.Filled = tl.Filled;
				mtl.Agent = tl.Agent;
				mtl.Flood = false;
				//mtl.Reachable = false;
			}
		}
	}
	
	public static int distBetween(Tile a, Tile b) {
		int xdif = a.Pos.getX() - b.Pos.getX();
		int ydif = a.Pos.getY() - b.Pos.getY();
		return (int)Math.sqrt((float)(xdif*xdif + ydif*ydif));
	}
	
	public static int rangeBetween(Tile a, Tile b) {
		int xdif = Math.abs(a.Pos.getX() - b.Pos.getX());
		int ydif = Math.abs(a.Pos.getY() - b.Pos.getY());
		return xdif+ydif;
	}
	
	public Path Astar(Tile start, Tile goal, int range) {
		//short cicuit, goal is end
		if (start == goal) {
			Path path = new Path();
			path.Move = 0;
			path.Range = 0;
			return path;
		}
		
		//get rid of previous info, update internal representation
		clean();
		LinkedList<Tile> open = new LinkedList<Tile>();
		start.GScore = 0;
		start.HScore = distBetween(start, goal);
		start.FScore = start.HScore;
		open.add(start);
		while (!open.isEmpty()) {
			//get and remove lowest fscore
			Tile cur = open.getFirst();
			for (Tile tl : open)
				if (tl.FScore < cur.FScore)
					cur = tl;
			open.remove(cur);
			
			//at goal, construct path
			if (cur == goal) {
				Path path = new Path();
				path.Move = 0;
				path.Range = 0;
				Path.Node curnd = new Path.Node();
				path.End = curnd;
				curnd.Tile = goal;
				Tile curtl = goal;
				if (curtl.IsMove)
					path.Move++;
				else
					path.Range++;
				while (curtl.CameFrom != start) {
					curtl = curtl.CameFrom;
					if (curtl.IsMove)
						path.Move++;
					else
						path.Range++;
					Path.Node prev = curnd;
					curnd = new Path.Node();
					curnd.Tile = curtl;
					curnd.Next = prev;
					curnd.RangeToGoal = curtl.RangeTo;
				}
				path.Start = curnd;
				return path;
			}
			
			//go to adjacent
			cur.Flood = true;
			for (Vec v : Vec.getDirs()) {
				Tile adj = getTile(cur.Pos.add(v));
				//checks
				if (adj == null || adj.Flood) 
					continue;
				//checks only for movement
				adj.RangeTo = rangeBetween(adj, goal);
				if (rangeBetween(adj, goal) > range-1) {
					adj.IsMove = true;
					if (!adj.Filled || (adj.Agent != null && adj.Agent != start.Agent)) //
						continue;
				} else {
					adj.IsMove = false;
				}
				
				int tentative_gscore = cur.GScore + 1;
				boolean tentative_better;
				
				//maybe att to open list
				if (!open.contains(adj)) {
					open.add(adj);
					tentative_better = true;
				} else if (tentative_gscore < adj.GScore) {
					tentative_better = true;
				} else {
					tentative_better = false;
				}
				
				//is better path
				if (tentative_better) {
					adj.CameFrom = cur;
					adj.GScore = tentative_gscore;
					adj.HScore = distBetween(cur, adj);
					adj.FScore = adj.GScore + adj.HScore;
				}
			}
		}
		return null;
	}
	
//	private void floodRangesInternal(Tile from, Tile tl, int rangel) {
//		//no tile? / Tile is origin? return
//		if (tl == null || (tl.Flood && tl.CameFrom == null)) return;
//		
//		//better path here?
//		if (tl.Flood && (tl.MoveLeft > 0 || tl.RangeLeft > rangel))
//			return;
//		
//		//put stats here
//		tl.MoveLeft = 0;
//		tl.RangeLeft = rangel;
//		tl.CameFrom = from;
//		tl.Flood = true;
//		tl.IsMove = false;
//		
//		//reachable?
//		tl.Reachable = (rangel >= 0);
//		
//		//adjacent
//		for (Vec v : Vec.getDirs()) {
//			floodRangesInternal(tl, getTile(tl.Pos.add(v)), rangel-1);
//		}
//	}
//	
//	private void floodPathsInternal(Agent a, Tile from, Tile tl, int movel, int rangel) {
//		//no tile? / Tile is origin? return
//		if (tl == null || (tl.Flood && tl.CameFrom == null)) return;
//		
//		//agent here? / movel < 0? pass it to floorRangesInternal and return
//		if ((tl.Agent != null && tl.Agent != a) || movel < 0 || !tl.Filled) {
//			floodRangesInternal(from, tl, rangel);
//			return;
//		}
//		
//		//better path here? return
//		if (tl.Flood && (tl.MoveLeft > movel))
//			return;
//		
//		//put stats there
//		tl.MoveLeft = movel;
//		tl.RangeLeft = rangel;
//		tl.CameFrom = from;
//		tl.Flood = true;
//		tl.IsMove = true;
//		
//		//reachable?
//		tl.Reachable = (movel >= 0);
//		
//		//adjacent
//		for (Vec dir : Vec.getDirs()) {
//			floodPathsInternal(a, tl, getTile(tl.Pos.add(dir)), movel-1, rangel);
//		}
//	}
	
//	public void floodPaths(Tile start, int move, int range) {
//		clean();
//		floodPathsInternal(start.Agent, null, start, move, range);
//	}
	
	public Tile getTile(int x, int y) {
		if (x >= 0 && x < mW && y >= 0 && y < mH) {
			return mBoard[x][y];
		} else {
			return null;
		}
	}
	
	public Tile getTile(Vec pos) {
		return getTile(pos.getX(), pos.getY());
	}
	
	public DataBattle getTarget() {
		return mTarget;
	}
	
	private Team mTeam;
	private DataBattle mTarget;
	private Tile[][] mBoard;
	private int mW, mH;
	
	///////////////////////////////////
	
	private interface AIControllerAction {
		public void step();
	}
	private Iterator<Agent> mAgentIterator;
	private Agent mAgentIteratorAgent;
	private AIAction mAgentAction;
	private AIControllerAction mActionIterate = new AIControllerAction() {public void step() {
		if (mAgentIterator.hasNext()) {
			//get an agent from my team
			mAgentIteratorAgent = mAgentIterator.next();
			while (mAgentIterator.hasNext() && mAgentIteratorAgent.getTeam() != mTeam)
				mAgentIteratorAgent = mAgentIterator.next();
			
			if (mAgentIteratorAgent.getTeam() == mTeam) {
				//do stuff
				mAgentAction = mAgentIteratorAgent.getAILogic().solve(AIController.this, mAgentIteratorAgent);
				mCurrentAction = mActionPerformAction;
			}
		} else {
			System.out.println("End AI turn");
			mTarget.nextTurn();
			mCurrentAction = mActionTurnWait;
		}
	}};
	private AIControllerAction mActionPerformAction = new AIControllerAction() {public void step() {
		if (mAgentAction.done()) {
			mCurrentAction = mActionIterate;
		} else {
			mAgentAction.act(mAgentIteratorAgent);
		}
	}};
	private AIControllerAction mActionTurnWait = new AIControllerAction() {public void step() {
		if (mTarget.getTurn() == mTeam) {
			System.out.println("Start AI turn");
			mAgentIterator = new ArrayList<Agent>(mTarget.getAgents()).iterator();
			mCurrentAction = mActionIterate;
		}
	}};
	private AIControllerAction mCurrentAction = mActionTurnWait;
	public void step() {
		mCurrentAction.step();
	}
}
