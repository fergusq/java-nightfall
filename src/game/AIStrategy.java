package game;

import util.*;

public interface AIStrategy {
	public AIAction solve(AIController c, Agent a);
	
	public static class BasicAttackAIStrategy implements AIStrategy {
		public AIAction solve(AIController c, Agent a) {
			System.out.println("BasicAttackStrategy: evaluating...");
			if (a.getInfo().getAbilities().length == 0) {
				AIAction act = new AIAction();
				act.addSelect();
				act.addDone();
				return act;
			}
			AgentInfo.Ability ab = a.getInfo().getAbilities()[0];
			//
			AIController.Path bestPath = null; //min move, max size, attack
			boolean bestCanAttack = false; //can the best path attack yet?
			int desiredMove = a.getMaxSize() - a.getSize(); //how much move to get to full size
			//
			boolean foundKillPath = false;
			//
			for (Agent othera : c.getTarget().getAgents()) {
				if (othera.getTeam() != a.getTeam()) {
					for (Vec v1 : othera.getTail()) {
						for (Vec dif : Vec.getDirs()) {
							Vec v = v1.add(dif);
							DataBattle.Tile vtl = othera.getBoard().getTile(v);
							if (vtl == null)
								continue;
							if (vtl.Filled && (vtl.Agent == null || vtl.Agent == a)) {
								AIController.Path path = c.Astar(c.getTile(a.getPos()), c.getTile(v), ab.range()-1);
								if (path == null)
									continue; //there was no path to attack there
								
								//add true end node
								AIController.Path.Node pathend = new AIController.Path.Node();
								pathend.Tile = c.getTile(v1);
								if (path.End == null) {
									path.End = path.Start = pathend;
								} else {
									path.End.Next = pathend;
								}
								
								System.out.println("Path<Move: " + path.Move + ", Range: " + path.Range +  
										"> From: <" + path.Start.Tile.Pos.getX() + ", " + path.Start.Tile.Pos.getY()
										+ "> To: <" + path.End.Tile.Pos.getX() + ", " + path.End.Tile.Pos.getY() 
										+ ">");
								
								boolean canAttack = path.Move <= a.getMove();
								
								//short circuit, if you can kill something do so
								if (ab.damage() >= othera.getSize() && canAttack) {
									bestPath = path;
									foundKillPath = true;
									break;
								}
								
								if (bestPath == null) { //no path? this must be the best!
									bestPath = path;
									bestCanAttack = canAttack;
								} else if (canAttack) { 
									if (bestCanAttack) { //best can attack, do I have more optimal move?
										if (bestPath.Move < desiredMove) { //wants more move
											if (path.Move > bestPath.Move) { //I have more move, I win
												bestPath = path;
											}
										} else { //no more move, am I closer to optimal move, without going under?
											if (path.Move < bestPath.Move && path.Move > desiredMove) {
												bestPath = path;
											}
										}
									} else {			//can attack, best can't, I win
										bestPath = path;
										bestCanAttack = true;
									}
								} else if (!bestCanAttack) { //neither can attack
									if (path.Move < bestPath.Move) { //shortest path to enemy
										bestPath = path;
									}
								}
							}
						}
						if (foundKillPath)
							break;
					}
				}
				if (foundKillPath)
					break;
			}

			AIAction act = new AIAction();
			act.addSelect();
			//found path?
			if (bestPath != null) {
				System.out.println("Path found!");
				{
					System.out.println("Path stats:\n\tMove:" + bestPath.Move + "\n\tRange:" + bestPath.Range);
					AIController.Path.Node n = bestPath.Start;
					while (n != null) {
						System.out.println("\t<" + n.Tile.Pos.getX() + ", " + n.Tile.Pos.getY() + 
								"> Range to goal: " + n.RangeToGoal);
						n = n.Next;
					}
				}
				System.out.println("\t--------");
				//debug show path
				for (AIController.Path.Node nd = bestPath.Start; nd != null; nd = nd.Next)
					act.addDebugPath(nd.Tile.Pos);
				
				AIController.Path.Node n = bestPath.Start;
				//move
				for (int i = 0; i < Math.min(bestPath.Move, a.getMove()); ++i) {
					act.addMove(n.Tile.Pos);
					System.out.println("\tMov<" + n.Tile.Pos.getX() + ", " + n.Tile.Pos.getY() + ">");
					n = n.Next;
				}
				if (bestPath.Move <= a.getMove()) {
					//get square to attack if in range
					while (n.Next != null)
						n = n.Next;
					System.out.println("\tAtk<" + n.Tile.Pos.getX() + ", " + n.Tile.Pos.getY() + ">");
					act.addAttack(ab, n.Tile.Pos);
				} else {
					//show proxy attack, but don't attack
					act.addShowAttack(ab);
				}
				System.out.println();
			}
			act.addDone();
			return act;
		}
	}
	
//	public static class AttackStrategy2 implements AIStrategy {
//		public AIAction solve(AIController c, Agent a) {
//			AgentInfo.Ability ab = a.getInfo().getAbilities()[0];
//			int maxmove = a.getMove();					//max move
//			int minmove = a.getMaxSize()-a.getSize();	//min move to have full size
//			
//			//flood paths
//			c.floodPaths(c.getTile(a.getPos()), maxmove, ab.range());
//			
//			//best destination
//			AIController.Tile bestDst = null;
//			boolean bestDstCanAttack = false;
//			boolean bestDstCanKill = false;
//			
//			//do paths
//			for (Agent targeta : c.getTarget().getAgents()) {
//			if (targeta.getTeam() != a.getTeam()) {
//			for (Vec tpos : targeta.getTail()) {
//				AIController.Tile tlat = c.getTile(tpos);
//				if (tlat.Reachable) { //only look for an approach if it can actually be reached
//					//approachs that can be made
//					for (Vec dif : Vec.getDirs()) {
//						//get tile in question
//						Vec approach = tpos.add(dif);
//						AIController.Tile tlf = c.getTile(approach);
//						
//						//can't reach it
//						if (tlf == null || !tlf.Reachable) 
//							continue;
//						
//						//can I kill it?
//						if (ab.damage() >= targeta.getSize()) {
//							//killing is parmount!, choose this one
//							if (!bestDstCanKill || tlf.betterMove(bestDst)) {
//								bestDst = tlat;
//								bestDst.CameFrom = tlf;
//								bestDstCanKill = true;
//								bestDstCanAttack = true;
//								continue;
//							}
//						} else if (bestDstCanKill) {
//							//can kill and I can't, no approach will work so break
//							break;
//						}
//						
//						if (bestDst == null || !bestDstCanAttack) { 
//							//no best, or best can't attack, this is the best
//							bestDst = tlat;
//							bestDst.CameFrom = tlf;
//							bestDstCanAttack = true;
//						} else { //best can attack, have we moved more?
//							if (tlat.MoveLeft < bestDst.MoveLeft) {
//								//(able to ignore last move from adjacent, since it has to be range, not move)
//								bestDst = tlat;
//								bestDst.CameFrom = tlf;
//								bestDstCanAttack = true;
//							}
//						}
//					}
//				} else { //can't be reached, but it might be worth working towards
//					if (!bestDstCanAttack) { //if there is no way to attack yet
//						if (bestDst == null) {
//							bestDst = tlat;
//							bestDstCanAttack = false;
//						} else {
//							if (tlat.betterMove(bestDst)) {
//								//and this path is shorter to attack
//								bestDst = tlat;
//								bestDstCanAttack = false;
//							}
//						}
//					}
//				}
//			}
//			}
//			}
//			
//			AIAction act = new AIAction();
//			act.addDone();
//			
//			//reconstruct path if it exists
//			if (bestDst != null) {
//				if (bestDstCanAttack) {
//					act.addAttack(ab, bestDst.Pos);
//					System.out.println("Attack <");
//				}
//				
//				while (true) {
//					bestDst = bestDst.CameFrom;
//					if (bestDst.CameFrom == null)
//						break; //start node
//					if (bestDst.IsMove)
//						act.addMove(bestDst.Pos);
//				}
//			}
//			
//			act.addSelect();
//			
//			//no path to follow
//			return act;
//		}
//	}
}
