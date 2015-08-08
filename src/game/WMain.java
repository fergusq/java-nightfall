package game;

import util.*;
import gui.*;
import input.*;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WMain extends CoreFrame {
	public static final int WIDTH = 1000;

	public void onInit() {
		setRenderSize(new Vec(WIDTH, 600));
		setFPS(30);
		GameSession session = new GameSession();
		mGui = new WidgetRoot();
		session.setGuiRoot(mGui);
		session.enterNodeMap();
		mGui.invokeLayout(new Vec(WIDTH, 600));		
	}
	
	long i = 0;

	public void onStep() {
		mGui.invokeRender(getRenderTarget());
		render();
	}
	
	public void onEvent(Event e) {
		mGui.invokeEvent(e);
	}
	
    public static void main(String[] args) {
    	WMain main = new WMain();
    	final Frame window = new Frame();
    	window.add(main);
    	window.setTitle("Nightfall");
    	window.setSize(WIDTH, 640);
    	window.setVisible(true);
    	window.addWindowListener(new WindowAdapter() {
    		@Override
    		public void windowClosing(WindowEvent e) {
    			window.dispose();
    			System.exit(0);
    		}
		});
    }

	WidgetRoot mGui;
}
