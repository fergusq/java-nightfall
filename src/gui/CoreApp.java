package gui;

import util.Vec;
import input.Event;
import input.Input;

public interface CoreApp {
	public RenderTarget getRenderTarget();
	public Input getInput();
	public void render();
	public void terminate();
	public void setFPS(float fps);
	public void setRenderSize(Vec v);
	//
	public void onEvent(Event e);
	public void onStep();
	public void onInit();
	public void onMain();
}
