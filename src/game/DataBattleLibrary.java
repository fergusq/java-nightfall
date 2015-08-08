package game;

import java.util.ArrayList;
import java.util.HashMap;

import json.*;

public class DataBattleLibrary {
	public DataBattleInfo getDataBattleByName(String name) {
		return mDataBattlesByName.get(name);
	}
	
	public void addDataBattles(JSNode source, AgentLibrary unitprovider) {
		for (JSNode battlend : ((JSArray)source).getChildren()) {
			JSObject battle = (JSObject)battlend;
			DataBattleInfo info = new DataBattleInfo();
			info.setName(battle.get("name").getStringValue());
			info.setBoardSize(JSUtil.toFVec(battle.get("size")).vec());
			
			//board filled
			JSArray rows = ((JSArray)battle.get("data"));
			for (int y = 0; y < rows.size(); ++y) {
				String row = rows.get(y).getStringValue();
				for (int x = 0; x < row.length(); ++x) {
					info.setFilled(x, y, row.charAt(x) != ' ');
				}
			}
			
			//upload zones
			ArrayList<DataBattleInfo.UploadEntry> tmpUploads = new ArrayList<DataBattleInfo.UploadEntry>();
			for (JSNode upload : ((JSArray)battle.get("uploads")).getChildren()) {
				DataBattleInfo.UploadEntry ent = new DataBattleInfo.UploadEntry();
				ent.Pos = JSUtil.toFVec(upload).vec();
				tmpUploads.add(ent);
			}
			info.setUploads(tmpUploads.toArray(new DataBattleInfo.UploadEntry[0]));

			//data slots
			ArrayList<DataBattleInfo.DataEntry> tmpDatas = new ArrayList<DataBattleInfo.DataEntry>();
			if (battle.get("dataSlots") != null)
				for (JSNode data : ((JSArray)battle.get("dataSlots")).getChildren()) {
					DataBattleInfo.DataEntry ent = new DataBattleInfo.DataEntry();
					ent.Pos = JSUtil.toFVec(data).vec();
					tmpDatas.add(ent);
				}
			info.setDatas(tmpDatas.toArray(new DataBattleInfo.DataEntry[0]));
			
			//credits
			//TODO:FIXME
			ArrayList<DataBattleInfo.CreditEntry> tmpCredits = new ArrayList<DataBattleInfo.CreditEntry>();
			for (JSNode credit : ((JSArray)battle.get("credits")).getChildren()) {
				DataBattleInfo.CreditEntry ent = new DataBattleInfo.CreditEntry();
				JSObject credito = (JSObject)credit;
				ent.Pos = JSUtil.toFVec(credito.get("pos")).vec();
				ent.Amount = credito.get("amount").getIntValue();
				tmpCredits.add(ent);
			}
			info.setCredits(tmpCredits.toArray(new DataBattleInfo.CreditEntry[0]));
			
			//units
			ArrayList<DataBattleInfo.UnitEntry> tmpUnits = new ArrayList<DataBattleInfo.UnitEntry>();
			for (JSNode unit : ((JSArray)battle.get("units")).getChildren()) {
				DataBattleInfo.UnitEntry ent = new DataBattleInfo.UnitEntry();
				JSObject unito = (JSObject)unit;
				ent.Pos = JSUtil.toFVec(unito.get("pos")).vec();
				ent.Info = unitprovider.getAgentByName(unito.get("name").getStringValue());
				tmpUnits.add(ent);
			}
			info.setUnits(tmpUnits.toArray(new DataBattleInfo.UnitEntry[0]));
			
			int bonus = 0;
			if (battle.get("reward") != null) bonus = battle.get("reward").getIntValue();

			info.setBonus(bonus);

			addDataBattle(info);
		};
	}
	
	public void addDataBattle(DataBattleInfo info) {
		mDataBattlesByName.put(info.getName(), info);
	}
	
	HashMap<String, DataBattleInfo> mDataBattlesByName = new HashMap<String, DataBattleInfo>();
}
