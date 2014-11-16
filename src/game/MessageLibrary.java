package game;

import java.util.ArrayList;
import java.util.HashMap;

import json.*;

public class MessageLibrary {
	public MessageInfo getMessageByName(String name) {
		return mMessagesByName.get(name);
	}

	public MessageInfo getMessageToShowOnEvent(String event, Object data) {
		return mMessagesByTriggerEvent.get(event+"/"+data);
	}
	
	public void addMessages(JSNode source) {
		for (JSNode messagend : ((JSArray)source).getChildren()) {
			JSObject message = (JSObject)messagend;
			MessageInfo info = new MessageInfo();
			info.setName(message.get("name").getStringValue());
			
			info.setTitle(message.get("title").getStringValue());
			info.setText(message.get("text").getStringValue());
			if (message.get("image") != null)
				info.setImage(ResourceLoader.LoadImage(message.get("image").getStringValue()));

			if (message.get("show after") != null) {
				JSObject showAfter = (JSObject) message.get("show after");
				if (showAfter.get("event").getStringValue().equals("victory")) {
					info.setTriggerEvent("victory", showAfter.get("battle").getStringValue());
				}
				else if (showAfter.get("event").getStringValue().equals("defeat")) {
					info.setTriggerEvent("defeat", showAfter.get("battle").getStringValue());
				}
				else if (showAfter.get("event").getStringValue().equals("newgame")) {
					info.setTriggerEvent("newgame", null);
				}
				else {
					// Unsupported event type
				}
			}

			//buttons
			ArrayList<MessageInfo.ButtonEntry> tmpButtons = new ArrayList<MessageInfo.ButtonEntry>();
			for (JSNode button : ((JSArray)message.get("buttons")).getChildren()) {
				MessageInfo.ButtonEntry ent = new MessageInfo.ButtonEntry();
				JSObject buttono = (JSObject)button;
				ent.Text = buttono.get("text").getStringValue();

				ArrayList<MessageInfo.Action> tmpActions = new ArrayList<MessageInfo.Action>();
				for (JSNode onclicknd : ((JSArray) buttono.get("onclick")).getChildren()) {
					JSObject onclick = (JSObject) onclicknd;
					MessageInfo.Action action = new MessageInfo.Action();
					switch (onclick.get("action").getStringValue()) {
					case "show message":
						action.Type = MessageInfo.ActionType.ShowMessage;
						action.NextMessage = onclick.get("name").getStringValue();
						break;
					case "open databattle":
						action.Type = MessageInfo.ActionType.OpenDatabattle;
						action.Databattle = onclick.get("name").getStringValue();
						break;
					case "open node":
						action.Type = MessageInfo.ActionType.OpenNode;
						action.Node = onclick.get("name").getStringValue();
						break;
					case "set visible":
						action.Type = MessageInfo.ActionType.SetVisible;
						action.Node = onclick.get("name").getStringValue();
						break;
					case "give credit":
						action.Type = MessageInfo.ActionType.GiveCredit;
						action.Credit = onclick.get("amount").getIntValue();
						break;
					case "give agent":
						action.Type = MessageInfo.ActionType.GiveAgent;
						action.Agent = onclick.get("name").getStringValue();
						break;
					case "close":
						action.Type = MessageInfo.ActionType.Close;
						break;
					default:
						// Unsupported action
						break;
					}
					tmpActions.add(action);
				}
				ent.OnClick = tmpActions.toArray(new MessageInfo.Action[0]);	
			
				tmpButtons.add(ent);
			}
		
			info.setButtons(tmpButtons.toArray(new MessageInfo.ButtonEntry[0]));
		
			addMessage(info);
		}
	}
	
	public void addMessage(MessageInfo info) {
		mMessagesByName.put(info.getName(), info);
		
		for (String event : info.getTriggerEvents()) {
			mMessagesByTriggerEvent.put(event + "/" + info.getTriggerEventData(event), info);
		}
	}
	
	HashMap<String, MessageInfo> mMessagesByName = new HashMap<String, MessageInfo>();
	HashMap<String, MessageInfo> mMessagesByTriggerEvent = new HashMap<String, MessageInfo>();
}
