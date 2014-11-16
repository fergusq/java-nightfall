package game;

import util.*;
import game.AgentInfo.Ability;
import gui.*;
import input.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.io.IOException;

public class WMain extends CoreFrame {
	public static final int WIDTH = 1000;

	public static <T> T[] A(T... ts) {return ts;}
	public void onInit() {
		setRenderSize(new Vec(WIDTH, 630));
		setFPS(30);
		//setLayout(new BorderLayout());
		//
		GameSession session = new GameSession();
		mGui = new WidgetRoot();
		session.setGuiRoot(mGui);
		//session.enterDataBattle(session.getDataBattleLibrary().getDataBattleByName("TestMap"));
		session.enterNodeMap();
		mGui.invokeLayout(new Vec(WIDTH, 600));		
	}
	
	long i = 0;

	public void onStep() {
		//mAI.step();
		mGui.invokeRender(getRenderTarget());
		render();
	}
	
	public void onEvent(Event e) {
		mGui.invokeEvent(e);
	}
	
    public static void main(String[] args) {
	WMain main = new WMain();
	main.setVisible(true);
    }

	WidgetRoot mGui;
	//DataBattleView mView;
	//AIController mAI;
}
