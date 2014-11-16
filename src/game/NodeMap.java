package game;

import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import json.*;
import util.*;

public class NodeMap {
	public static class Link {
		public Node NodeA, NodeB;
	}
	public static class WarezItem {
		public AgentInfo Agent;
		public int Value;
	}
	public static class Node {
		public static enum Type {
			Warez,
			Battle;
		}
		public static enum Status {
			Unknown,
			Visible, Open,
			Defeated;
		}
		public void setDefeated() {
			NStatus = Status.Defeated;
			for (Node n : Adjacent) {
				if (n.NStatus == Status.Unknown || n.NStatus == Status.Visible)
					n.NStatus = Status.Open;
			}
		}
		public void setVisible() {
			NStatus = Status.Visible;
		}
		public void setOpen() {
			NStatus = Status.Open;
		}
		public boolean isOpen() {
			return NStatus == Status.Defeated || NStatus == Status.Open;
		}
		public String Name;
		public DataBattleInfo DataBattle;
		public List<WarezItem> Warez;
		public Type NType;
		public Status NStatus;
		public String Title;
		public String Desc;
		public Image Image;
		public Image DarkImage;
		public Vec Pos;
		public ArrayList<Node> Adjacent = new ArrayList<Node>();
		public int GroupId;
	}
	
	public NodeMap(GameSession session) {
		mGame = session;
	}
	
	private static int groupidcounter = 0;
	private static final java.util.Random rnd = new java.util.Random(10);
	private static int groupycounter = 0;

	public void loadFrom(JSNode node) {
		int group = groupidcounter++;
		int groupx = group != 0 ? rnd.nextInt(4) : 0;
		int groupy = group != 0 ? (groupycounter+=1) : 0;

		JSObject nodeo = (JSObject)node;
		
		//nodes
		for (JSNode gamenode : ((JSArray)nodeo.get("nodes")).getChildren()) {
			JSObject gamenodeo = (JSObject)gamenode;
			Node nd = new Node();
			nd.GroupId = group;

			nd.Name = gamenodeo.get("name").getStringValue();
			nd.Title = gamenodeo.get("title").getStringValue();
			nd.Desc = gamenodeo.get("desc").getStringValue();
			//
			nd.Image = ResourceLoader.LoadImage(gamenodeo.get("image").getStringValue());
			BufferedImage tmpImg = new BufferedImage(nd.Image.getWidth(null), nd.Image.getHeight(null), 
													BufferedImage.TYPE_INT_ARGB);
			tmpImg.getGraphics().drawImage(nd.Image, 0, 0, null);
			ColorSpace grayColorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(grayColorSpace, tmpImg.getColorModel().getColorSpace(), null);
			op.filter(tmpImg, tmpImg);
			nd.DarkImage = tmpImg;
			//
			nd.Pos = JSUtil.toFVec(gamenodeo.get("pos")).vec();
			nd.Pos = new Vec(nd.Pos.getX()+groupx, nd.Pos.getY()+groupy);
			if (nd.Pos.getY() > groupycounter) groupycounter = nd.Pos.getY();
			JSNode battlename = gamenodeo.get("battle");
			if (battlename == null) {
				nd.NType = Node.Type.Warez;
				nd.Warez = new ArrayList<WarezItem>();
				for (JSNode itemnode : ((JSArray)gamenodeo.get("warez")).getChildren()) {
					WarezItem item = new WarezItem();
					JSObject itemo = (JSObject)itemnode;
					item.Agent = mGame.getAgentLibrary().getAgentByName(itemo.get("agent").getStringValue());
					item.Value = itemo.get("value").getIntValue();
					nd.Warez.add(item);
				}
			} else {
				nd.NType = Node.Type.Battle;
				nd.DataBattle = mGame.getDataBattleLibrary().getDataBattleByName(battlename.getStringValue());
			}
			if (gamenodeo.get("status") != null) switch (gamenodeo.get("status").getStringValue()) {
			    case "open":
				nd.NStatus = Node.Status.Open;
				break;
			    case "visible":
				nd.NStatus = Node.Status.Visible;
				break;
			    case "unknown":
				nd.NStatus = Node.Status.Unknown;
			}
			else nd.NStatus = Node.Status.Unknown;
			mNodes.add(nd);
			mNodesByName.put(nd.Name, nd);
		}
		
		//links
		for (JSNode link : ((JSArray)nodeo.get("links")).getChildren()) {
			JSArray arr = (JSArray)link;
			Link l = new Link();
			System.err.println(arr);
			Node na = mNodesByName.get(arr.get(0).getStringValue());
			Node nb = mNodesByName.get(arr.get(1).getStringValue());
			l.NodeA = na;
			l.NodeB = nb;
			na.Adjacent.add(nb);
			nb.Adjacent.add(na);
			mLinks.add(l);
		}
	}
	
	public Node getNodeByName(String name) {
		return mNodesByName.get(name);
	}
	
	public ArrayList<Node> getNodes() {
		return mNodes;
	}
	
	public ArrayList<Link> getLinks() {
		return mLinks;
	}
	
	private final GameSession mGame;
	
	private HashMap<String, Node> mNodesByName = new HashMap<String, Node>();
	private ArrayList<Node> mNodes = new ArrayList<Node>();
	private ArrayList<Link> mLinks = new ArrayList<Link>();
}
