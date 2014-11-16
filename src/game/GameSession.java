package game;

import java.awt.Color;

import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import util.UDim;

import game.Agent.TurnState;
import game.AgentInfo.Ability;
import game.NodeMap.Node;
import gui.WidgetRoot;
import json.*;

public class GameSession {
	public GameSession() {
		try {
			//// load the agent library
			mAgentLibrary.addAbilitySource("BasicAttack", new AgentLibrary.AbilitySource() {
				public Ability loadAbility(JSNode nd) {
					JSObject obj = (JSObject)nd;
					String name = obj.get("name").getStringValue();
					String desc = obj.get("desc").getStringValue();
					int dmg = (int)obj.get("damage").getNumberValue();
					int range = (int)obj.get("range").getNumberValue();
					int minsize = (int)obj.get("minsize").getNumberValue();
					return new AgentInfo.AbilityDamageGeneric(name, desc, range, minsize, dmg);
				}
			});
			mAgentLibrary.addAbilitySource("SelfHarmingAttack", new AgentLibrary.AbilitySource() {
				public Ability loadAbility(JSNode nd) {
					JSObject obj = (JSObject)nd;
					String name = obj.get("name").getStringValue();
					String desc = obj.get("desc").getStringValue();
					int dmg = (int)obj.get("damage").getNumberValue();
					int range = (int)obj.get("range").getNumberValue();
					int minsize = (int)obj.get("minsize").getNumberValue();
					int selfdmg = (int)obj.get("self damage").getNumberValue();
					return new AgentInfo.AbilityDamageSelfHarming(name, desc, range, minsize, dmg, selfdmg);
				}
			});
			mAgentLibrary.addAbilitySource("ExpandTail", new AgentLibrary.AbilitySource() {
				public Ability loadAbility(JSNode nd) {
					JSObject obj = (JSObject)nd;
					String name = obj.get("name").getStringValue();
					String desc = obj.get("desc").getStringValue();
					int size = (int)obj.get("expand").getNumberValue();
					int range = (int)obj.get("range").getNumberValue();
					int minsize = (int)obj.get("minsize").getNumberValue();
					int selfdmg = (int)obj.get("self damage").getNumberValue();
					return new AgentInfo.AbilityExpandTail(name, desc, range, minsize, size, selfdmg);
				}
			});
			mAgentLibrary.addAbilitySource("ModifyProperties", new AgentLibrary.AbilitySource() {
				public Ability loadAbility(JSNode nd) {
					JSObject obj = (JSObject)nd;
					String name = obj.get("name").getStringValue();
					String desc = obj.get("desc").getStringValue();
					int move = (int)obj.get("move").getNumberValue();
					int size = (int)obj.get("size").getNumberValue();
					int range = (int)obj.get("range").getNumberValue();
					int minsize = (int)obj.get("minsize").getNumberValue();
					int selfdmg = (int)obj.get("self damage").getNumberValue();
					return new AgentInfo.AbilityModifyProperties(name, desc, range, minsize, move, size, selfdmg);
				}
			});
			mAgentLibrary.addAbilitySource("ModifyGrid", new AgentLibrary.AbilitySource() {
				public Ability loadAbility(JSNode nd) {
					JSObject obj = (JSObject)nd;
					String name = obj.get("name").getStringValue();
					String desc = obj.get("desc").getStringValue();
					boolean type = (boolean)obj.get("type").getBooleanValue();
					int range = (int)obj.get("range").getNumberValue();
					int minsize = (int)obj.get("minsize").getNumberValue();
					return new AgentInfo.AbilityModifyGrid(name, desc, range, minsize, type);
				}
			});
			mAgentLibrary.addAgents(ResourceLoader.LoadJSON("AgentData.json"));
			for (JSNode node :
				     ResourceLoader.LoadAddonJSONFiles("AgentData.json")) {
				mAgentLibrary.addAgents(node);
			}

			//// load the data battle library
			mDataBattleLibrary.addDataBattles(ResourceLoader.LoadJSON("BattleData.json"), 
												mAgentLibrary);
			for (JSNode node :
				     ResourceLoader.LoadAddonJSONFiles("BattleData.json")) {
				mDataBattleLibrary.addDataBattles(node, mAgentLibrary);
			}

			mMessageLibrary.addMessages(ResourceLoader.LoadJSON("MessageData.json"));
			for (JSNode node :
				     ResourceLoader.LoadAddonJSONFiles("MessageData.json")) {
				mMessageLibrary.addMessages(node);
			}

			//// load node map
			mNodeMap = new NodeMap(this);
			mNodeMap.loadFrom(ResourceLoader.LoadJSON("NodeData.json"));
			for (JSNode node :
				     ResourceLoader.LoadAddonJSONFiles("NodeData.json")) {
				mNodeMap.loadFrom(node);
			}
			
			//// load the user's saved agents
			loadSave();
		} catch (Exception e) {
			System.err.println("Failed to load game data because: " + e.getMessage());
			e.printStackTrace();
		} 
	}
	
	private void loadSave() throws IOException {
		JSObject userdata;
		try {
			userdata = (JSObject) ResourceLoader.LoadSave("SaveData.json");
		} catch (Exception e) {
			System.err.println("Failed to load save because: " + e.getMessage());
			e.printStackTrace();
			return;
		}

		JSArray useragents = (JSArray)((JSObject)userdata.get("inventory")).get("agents");
		//agents
		for (JSNode nd : useragents.getChildren()) {
			JSObject agentnd = (JSObject)nd;
			int quantity = (int)agentnd.get("quantity").getNumberValue();
			AgentInfo agent = mAgentLibrary.getAgentByName(agentnd.get("name").getStringValue());
			for (int i = 0; i < quantity; ++i)
				mInventory.addAgent(agent);
		}
		//defeated nodes
		for (JSNode nd : ((JSArray)userdata.get("defeated")).getChildren()) {
			mNodeMap.getNodeByName(nd.getStringValue()).setDefeated();
		}
		//credits
		int credits = ((JSObject)userdata.get("inventory")).get("credits").getIntValue();
		getInventory().setCredit(credits);

		if (userdata.get("newgame") == null)
			newMessage(getMessageLibrary().getMessageToShowOnEvent("newgame", null));
	}

	public void saveData() {
		String save = "{";
		//
		save += "\t\"inventory\": {\n";
		//agents
		save += "\t\t\"agents\": [\n";
		for (int i = 0; i < mInventory.getAgents().size(); i++) {
			if (i != 0) save += ",\n";
			Inventory.InventoryEntry a = mInventory.getAgents().get(i);
			int quantity = a.Quantity;
			String name = a.Agent.getName();
			save += "\t\t\t{\"name\": \""+name+"\", \"quantity\": " + quantity + "}";
		}
		//credits
		save += "],\n\t\t\"credits\": " + mInventory.getCredit() + "\n\t},\n";
		//defeated
		save += "\t\"defeated\": [ ";
		int i = 0;
		for (NodeMap.Node node : mNodeMap.getNodes()) {
			if (node.NStatus == Node.Status.Defeated) {
				if (i++ != 0) save += ",";
				save += "\"" + node.Name + "\"";
			}
		}
		save += " ],\n\t\"newgame\": false}";
		
		ResourceLoader.WriteSave(save, "SaveData.json");
		
		System.out.println(save);
	}

	public void enterDataBattle(DataBattleInfo info, NodeMap.Node nd) {
		DataBattle battle = new DataBattle(info, nd);
		//
		Team myTeam = new Team();
		myTeam.setColor(Color.blue);
		myTeam.setInventory(getInventory());
		Team aiTeam = new Team();
		aiTeam.setColor(Color.red);
		//
		battle.addTeam(myTeam);
		battle.addTeam(aiTeam);
		//
		AIController ai = new AIController(battle, aiTeam);
		//
		for (DataBattleInfo.UnitEntry uent : info.getUnits()) {
			Agent a = new Agent(battle, aiTeam, uent.Pos, uent.Info);
			a.setTurnState(TurnState.Done);
			a.setAILogic(new AIStrategy.BasicAttackAIStrategy());
		}
		//
		for (DataBattleInfo.CreditEntry cent : info.getCredits()) {
			DataBattle.Tile tile = battle.getTile(cent.Pos);
			tile.Credit = cent.Amount;
		}
		//
		mDataBattleView = new DataBattleView(battle, myTeam, this, ai);
		mDataBattleView.setSize(new UDim(0, 0, 1, 1));
		mDataBattleView.setParent(mGuiRoot);
	}
	
	public void enterNodeMap() {
		saveData();
		if (mNodeMapView == null) {
			mNodeMapView = new NodeMapView(mNodeMap, this);
			mNodeMapView.setSize(new UDim(0, 0, 1, 1));
			mNodeMapView.setParent(mGuiRoot);
		}
		mNodeMapView.enter();
		mNodeMapView.setVisible(true);
	}
	
	public void onVictory(DataBattle battle) {
		MessageInfo msg = getMessageLibrary().getMessageToShowOnEvent("victory", battle.getInfo().getName());
		if (msg != null) {
			newMessage(msg);
		}
	}

	public void onDefeat(DataBattle battle) {
		MessageInfo msg = getMessageLibrary().getMessageToShowOnEvent("defeat", battle.getInfo().getName());
		if (msg != null) {
			newMessage(msg);
		}
	}

	public void newMessage(MessageInfo msg) {
		mNewMessages.add(msg);
	}

	public Inventory getInventory() {
		return mInventory;
	}
	
	public AgentLibrary getAgentLibrary() {
		return mAgentLibrary;
	}
	
	public DataBattleLibrary getDataBattleLibrary() {
		return mDataBattleLibrary;
	}

	public MessageLibrary getMessageLibrary() {
		return mMessageLibrary;
	}

	public ArrayList<MessageInfo> getNewMessages() {
		return mNewMessages;
	}

	public void setGuiRoot(WidgetRoot r) {
		mGuiRoot = r;
	}
	
	//guis
	private WidgetRoot mGuiRoot;
	private DataBattleView mDataBattleView;
	private NodeMapView mNodeMapView;
	
	//internal data
	private NodeMap mNodeMap;
	private MessageLibrary mMessageLibrary = new MessageLibrary();
	private DataBattleLibrary mDataBattleLibrary = new DataBattleLibrary();
	private AgentLibrary mAgentLibrary = new AgentLibrary();
	private Inventory mInventory = new Inventory();
	
	private ArrayList<MessageInfo> mNewMessages = new ArrayList<MessageInfo>();
}
