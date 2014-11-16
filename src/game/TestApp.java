package game;

import util.*;
import game.AgentInfo.Ability;
import gui.*;
import input.*;
import java.awt.Color;
import java.io.IOException;

public class TestApp extends CoreApplet {
	public static final int WIDTH = 1000;

	public static <T> T[] A(T... ts) {return ts;}
	public void onInit() {
		setRenderSize(new Vec(WIDTH, 600));
		setFPS(30);
		//
		GameSession session = new GameSession();
		mGui = new WidgetRoot();
		session.setGuiRoot(mGui);
		//session.enterDataBattle(session.getDataBattleLibrary().getDataBattleByName("TestMap"));
		session.enterNodeMap();
		mGui.invokeLayout(new Vec(WIDTH, 600));		
	}
	
	public void onStep() {
		//mAI.step();
		mGui.invokeRender(getRenderTarget());
		render();
	}
	
	public void onEvent(Event e) {
		mGui.invokeEvent(e);
	}
	
	WidgetRoot mGui;
	//DataBattleView mView;
	//AIController mAI;
}
