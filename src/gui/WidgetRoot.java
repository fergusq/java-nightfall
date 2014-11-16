package gui;

import input.Event;
import input.Input;
import input.KeyEvent;
import input.MouseEvent;
import input.Event.EventType;
import input.MouseEvent.MouseEventType;
import util.*;

public class WidgetRoot extends Widget {
	public void invokeLayout(Vec windowsize) {
		setSize(new UDim(windowsize.fvec()));
		internalOnLayout(true);
	}
	
	public void invokeRender(RenderTarget t) {
		internalOnRender(t);
	}
	
	public void invokeEvent(Event e) {
		Input in = e.getInput();
		in.setPrevMouseTarget();
		//
		if (e.getEventType() == EventType.Key) {
			if (in.getKeyTarget() == null) {
				internalOnEvent(e);
			} else {
				KeyEvent ke = (KeyEvent)e;
				switch (ke.getKeyEventType()) {
				case Press:
					in.getKeyTarget().onKeyDown.fire(ke);
					break;
				case Release:
					in.getKeyTarget().onKeyUp.fire(ke);
					break;
				}
			}
		} else {
			internalOnEvent(e);
		}
		//
		if (in.getMouseTarget() != in.getPrevMouseTarget()) {
			if (in.getPrevMouseTarget() != null)
				in.getPrevMouseTarget().onMouseLeave.fire(new MouseEvent(MouseEventType.Leave, in));
			if (in.getMouseTarget() != null)
				in.getMouseTarget().onMouseEnter.fire(new MouseEvent(MouseEventType.Enter, in));
		}
	}
}
