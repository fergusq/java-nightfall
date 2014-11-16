package game;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

import json.*;

public class AgentLibrary {
	public interface AbilitySource {
		public AgentInfo.Ability loadAbility(JSNode nd);
	}
	
	public AgentInfo getAgentByName(String agentname) {
		AgentInfo info = mAgentInfoByName.get(agentname);
		assert info != null: "Missing AgentInfo for agent `" + agentname + "`";
		return info;
	}
	
	public void addAgents(JSNode source) {
		for (JSNode nd : ((JSArray)source).getChildren()) {
			JSObject agentnd = (JSObject)nd;
			AgentInfo info = new AgentInfo();
			info.setName(agentnd.get("name").getStringValue());
			info.setDesc(agentnd.get("desc").getStringValue());
			info.setColor(JSUtil.toColor(agentnd.get("color")));
			info.setThumb(ResourceLoader.LoadImage(agentnd.get("thumb").getStringValue()));
			info.setMove((int)agentnd.get("move").getNumberValue());
			info.setSize((int)agentnd.get("size").getNumberValue());
			ArrayList<AgentInfo.Ability> tmpAbilities = new ArrayList<AgentInfo.Ability>();
			for (JSNode ability : ((JSArray)agentnd.get("abilities")).getChildren()) {
				JSObject abilitynd = (JSObject)ability;
				AbilitySource src = mAbilitySources.get(abilitynd.get("source").getStringValue());
				tmpAbilities.add(src.loadAbility(abilitynd.get("params")));
			}
			info.setAbilities(tmpAbilities.toArray(new AgentInfo.Ability[0]));
			addAgent(info);
		}
	}
	
	public void addAgent(AgentInfo a) {
		mAgentInfoByName.put(a.getName(), a);
	}
	
	public void addAbilitySource(String name, AbilitySource src) {
		mAbilitySources.put(name, src);
	}
	
	public Collection<AgentInfo> getAgents() {
		return mAgentInfoByName.values();
	}

	private HashMap<String, AgentInfo> mAgentInfoByName = new HashMap<String, AgentInfo>();
	private HashMap<String, AbilitySource> mAbilitySources = new HashMap<String, AbilitySource>();
}
