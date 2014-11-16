package game;

import java.util.HashMap;
import java.util.Set;
import java.awt.Image;

public class MessageInfo {

	public static enum ActionType {
		OpenDatabattle,
			OpenNode,
			SetVisible,
			ShowMessage,
			GiveCredit,
			GiveAgent,
			Close
	}

	public static class Action {
		ActionType Type;
		String NextMessage;
		String Databattle;
		String Node;
		String Agent;
		int Credit;
	}

	public static class ButtonEntry {
		String Text;
		Action[] OnClick;
	}

	public void setText(String text) {
		mText = text;
	}

	public String getText() {
		return mText;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setImage(Image image) {
		mImage = image;
	}

	public Image getImage() {
		return mImage;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setButtons(ButtonEntry[] buttons) {
		mButtons = buttons;
	}

	public ButtonEntry[] getButtons() {
		return mButtons;
	}

	public void setTriggerEvent(String event, Object data) {
		mTriggerEvents.put(event, data);
	}

	public boolean hasTriggerEvent(String event) {
		return mTriggerEvents.get(event) != null;
	}

	public Set<String> getTriggerEvents() {
		return mTriggerEvents.keySet();
	}

	public Object getTriggerEventData(String event) {
		return mTriggerEvents.get(event);
	}
	
	String mText;
	String mTitle;
	Image mImage;
	String mName;
	HashMap<String, Object> mTriggerEvents = new HashMap<String, Object>();
	ButtonEntry[] mButtons;
}