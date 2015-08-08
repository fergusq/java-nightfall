package gui;

import java.awt.Panel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import util.Vec;
import input.*;

public class CoreFrame extends Panel implements CoreApp, 
	MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {	
	//CoreApp methods
	
	public RenderTarget getRenderTarget() {
		return mRenderTarget;
	}
	
	public Input getInput() {
		return mInput;
	}

	public final void render() {
		repaint();
	}
	
	public final void terminate() {
		mRunning = false;
	}

	public final void setFPS(float fps) {
		mFPS = fps;
	}
	
	public final void setRenderSize(Vec v) {
		setSize(v.getX(), v.getY());
		mAppletSize = v;
	}

	public void onEvent(Event e) {}
	public void onStep() {}
	public void onInit() {}
	public void onMain() {}
	
	private void mainLoop() {
		while (mRunning) {
			//drain the event queue
			while (!mEventQueue.isEmpty()) {
				try {
					onEvent(mEventQueue.take());
				} catch (InterruptedException e) {}
			}
			//step
			onStep();
			try {
				Thread.sleep((long)(1/mFPS*1000));
			} catch (InterruptedException e) {}
		}
	}
	
	private void onEventInternal(Event e) {
		mEventQueue.offer(e);
	}
	
	//////////////// event handlers //////////////
	//////////////////////////////////////////////
	
	public void keyTyped(java.awt.event.KeyEvent arg0) {}
	public void mouseClicked(java.awt.event.MouseEvent arg0) {}
	public void mouseExited(java.awt.event.MouseEvent arg0) {}
	public void mouseEntered(java.awt.event.MouseEvent arg0) {}
	public void keyPressed(java.awt.event.KeyEvent evt) {
		KeyEvent.Key k = KeyEvent.Key.translateCode(evt.getKeyCode());
		mInput.setKeyState(k, true);
		onEventInternal(new KeyEvent(KeyEvent.KeyEventType.Press, mInput, k));
	}
	public void keyReleased(java.awt.event.KeyEvent evt) {
		KeyEvent.Key k = KeyEvent.Key.translateCode(evt.getKeyCode());
		mInput.setKeyState(k, false);
		onEventInternal(new KeyEvent(KeyEvent.KeyEventType.Release, mInput, k));
	}
	public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
		onEventInternal(new MouseEvent(MouseEvent.MouseEventType.Scroll, mInput, evt.getWheelRotation()));
	}
	public void mouseDragged(java.awt.event.MouseEvent evt) {
		mouseMoved(evt);
	}
	public void mouseMoved(java.awt.event.MouseEvent evt) {
		mInput.setMousePos(new Vec(evt.getX(), evt.getY()));
		onEventInternal(new MouseEvent(MouseEvent.MouseEventType.Move, mInput));
	}
	public MouseEvent.MouseButton translateButton(int bn) {
		switch (bn) {
		case java.awt.event.MouseEvent.BUTTON1:
			return MouseEvent.MouseButton.Left;
		case java.awt.event.MouseEvent.BUTTON3:
			return MouseEvent.MouseButton.Right;
		case java.awt.event.MouseEvent.BUTTON2:
			return MouseEvent.MouseButton.Center;
		default:
			throw new RuntimeException("Bad button `" + bn + "` to translateButton(int button)");
		}
	}
	public void mousePressed(java.awt.event.MouseEvent evt) {
		MouseEvent.MouseButton bn = translateButton(evt.getButton());
		mInput.setButton(bn, true);
		onEventInternal(new MouseEvent(MouseEvent.MouseEventType.Press, mInput, bn));
	}
	public void mouseReleased(java.awt.event.MouseEvent evt) {
		MouseEvent.MouseButton bn = translateButton(evt.getButton());
		mInput.setButton(bn, false);
		onEventInternal(new MouseEvent(MouseEvent.MouseEventType.Release, mInput, bn));
	}
	
	///////////// members ////////////////
	//////////////////////////////////////
	
	private LinkedBlockingQueue<Event> mEventQueue = new LinkedBlockingQueue<Event>();
	private Vec mAppletSize = new Vec((int)getSize().getWidth(), (int)getSize().getHeight());
	private boolean mRunning = true;
	private float mFPS = 30;
	private Image mBackBuffer;
	private Input mInput = new Input();
	private RenderTarget mRenderTarget = new RenderTarget() {
		public Vec getRenderSize() {
			return mAppletSize;
		}
		public Graphics getContext() {
			return mBackBuffer.getGraphics();
		}	
	};

	///////////// applet //////////////
	///////////////////////////////////
	
	private static final long serialVersionUID = 1L;
	public CoreFrame() {
		//Y_TRANS = getInsets().top > 0 ? getInsets().top : 50;
		onInit();
		//System.out.println("Created Buffer size: <" + getSize().getWidth() + ", " + getSize().getHeight() + ">");
		mBackBuffer = new BufferedImage((int)getSize().getWidth(), (int)getSize().getHeight(), BufferedImage.TYPE_INT_ARGB);
		new Thread() {
			public void run() {mainLoop();}
		}.start();
		new Thread() {
			public void run() {onMain();}
		}.start();
		addMouseListener(CoreFrame.this);
		addKeyListener(CoreFrame.this);
		addMouseMotionListener(CoreFrame.this);
		addMouseWheelListener(CoreFrame.this);
	}
	public void update(Graphics g) {
		paint(g);
		getRenderTarget().getContext().clearRect(0, 0, (int)getSize().getWidth(), (int)getSize().getHeight());
	}
	public void paint(Graphics g) {
		g.drawImage(mBackBuffer, 0, 0, null);
	}
}


