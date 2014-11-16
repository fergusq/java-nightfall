package game;

import util.*;
import game.Agent.TurnState;
import game.DataBattle.TileOverlay;

import java.util.LinkedList;

public class AIAction {
	private abstract class IAction {
		public IAction(int steps) {
			mStepsLeft = steps;
		}
		public void act(Agent a) {
			onAct(a);
			mStepsLeft--;
		}
		public abstract void onAct(Agent a);
		public boolean done() {
			return mStepsLeft == 0;
		}
		public int getStep() {
			return mStepsLeft;
		}
		private int mStepsLeft;
	}
	
	public void addSelect() {
		mActions.addLast(new IAction(10) {
			public void onAct(Agent a) {
				if (getStep() == 10) {
					a.getBoard().clear(true, true, true);
					//for (Vec v : mMoves)
					//	a.getBoard().getTile(v).Overlay = TileOverlay.Mod;
					a.getBoard().getTile(a.getPos()).Overlay = TileOverlay.Sel;
				}
			}
		});
	}
	
	public void addMove(final Vec pos) {
		mActions.addLast(new IAction(10) {
			public void onAct(Agent a) {
				switch (getStep()) {
				case 10:
					a.getBoard().clear(true, true, true);
					a.getBoard().moveFlood(a.getPos(), a.getMoveLeft());
					a.getBoard().getTile(a.getPos()).Overlay = TileOverlay.Sel;
					break;
				case 1:
					a.move(pos);
					break;
				}
			}
		});
	}
	
	public void addAttack(final AgentInfo.Ability ability, final Vec pos) {
		mActions.addLast(new IAction(10) {
			public void onAct(Agent a) {
				switch (getStep()) {
				case 10:
					a.getBoard().clear(true, true, true);
					ability.select(a);
					break;
				case 1:
					ability.apply(a, a.getBoard().getTile(pos).Agent, pos);
					a.getBoard().clear(true, true, true);
					break;
				}
			}
		});
	}
	
	public void addShowAttack(final AgentInfo.Ability ability) {
		mActions.addLast(new IAction(10) {
			public void onAct(Agent a) {
				if (getStep() == 10) {
					a.getBoard().clear(true, true, true);
					ability.select(a);
				}
			}
		});
	}
	
	public void addDone() {
		mActions.addLast(new IAction(1) {
			public void onAct(Agent a) {
				if (getStep() == 1) {
					a.getBoard().clear(true, true, true);
					a.setTurnState(TurnState.Done);
				}
			}
		});
	}
	
	public void addDebugPath(Vec v) {
		mMoves.add(v);
	}
	
	public boolean done() {
		return mActions.isEmpty();
	}
	
	public void act(Agent a) {
		if (!mActions.isEmpty()) {
			IAction ac = mActions.peekFirst();
			ac.act(a);
			if (ac.done())
				mActions.removeFirst();
		}
	}

	private final LinkedList<Vec> mMoves = new LinkedList<Vec>();
	private final LinkedList<IAction> mActions = new LinkedList<IAction>();
}
