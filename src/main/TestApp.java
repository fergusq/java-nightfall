package main;

import java.awt.*;

import gui.*;
import input.*;
import input.Event;
import util.*;

public class TestApp extends CoreApplet {
	public void onInit() {
		setRenderSize(new Vec(300, 300));
		setFPS(30);
		//
		final WidgetRect bkg = new WidgetRect();
		bkg.setColor(Color.black);
		bkg.setSize(new UDim(0, 0, 1, 1));
		bkg.setParent(mRoot);
		//
		mSq = new WidgetRect();
		mSq.setColor(Color.red);
		mSq.setSize(new UDim(100, 100));
		mSq.setPos(new UDim(100, 100));
		mSq.setParent(bkg);
		mSq.onMouseEnter.connect(new MouseEvent.Listener() {
			public void onMouseEvent(MouseEvent e) {
				mSq.setColor(Color.yellow);
			}
		});
		mSq.onMouseLeave.connect(new MouseEvent.Listener() {
			public void onMouseEvent(MouseEvent e) {
				mSq.setColor(Color.red);
			}
		});
		mSq.onMouseMove.connect(new MouseEvent.Listener() {
			public void onMouseEvent(MouseEvent e) {
				if (e.getInput().getButton(MouseEvent.MouseButton.Left))
					mSq.setPos(new UDim(e.getInput().getMousePos().fvec().sub(new FVec(50, 50))));
			}
		});
		//
		mRoot.invokeLayout(new Vec(300, 300));
	}
	
	public void onStep() {
		//System.out.println("Step..\n");
		mRoot.invokeRender(getRenderTarget());
		render();
		int h = getInput().getKeyState(KeyEvent.Key.Right) ? 3 : 0;
		h += getInput().getKeyState(KeyEvent.Key.Left) ? -3 : 0;
		int v = getInput().getKeyState(KeyEvent.Key.Down) ? 3 : 0;
		v += getInput().getKeyState(KeyEvent.Key.Up) ? -3 : 0;
		mSq.setPos(new UDim(mSq.getPos().getOffset().add(new FVec(h, v))));
	}
	
	public void onEvent(Event e) {
		//System.out.println("Event..\n");
		mRoot.invokeEvent(e);
	}
	
	WidgetRect mSq;
	WidgetRoot mRoot = new WidgetRoot();
}
