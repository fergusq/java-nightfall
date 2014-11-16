package game;

import java.awt.Color;
import java.awt.Graphics;

import game.NodeMap.Node.Status;
import gui.*;
import gui.WidgetText.TextAlign;
import util.*;
import input.*;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

public class NodeMapView extends WidgetRect {
	private interface IAction {
		public void onSelect();
		public void onDeselect();
		public void onClick(NodeMap.Node nd);
	}
	
	/***************************************/
	/***************************************/
	/***************************************/
	private class CutoffCornerWidget extends WidgetRect {
		// a---b
		// |   |
		// c---d
		public CutoffCornerWidget(int a, int b, int c, int d) {
			mA = a; mB = b; mC = c; mD = d;
		}
		public void onRender(RenderTarget t) {
			Rect r = getRect();
			Graphics g = t.getContext();
			int h = r.getHeight();
			int[] xs = {r.getX()+h*mA, r.getX()+r.getWidth()-h*mB, r.getX()+r.getWidth()-h*mD, r.getX()+h*mC};
			int[] ys = {r.getY(), r.getY(), r.getY()+h, r.getY()+h};
			g.setColor(getColor());
			g.fillPolygon(xs, ys, 4);
		}
		private int mA, mB, mC, mD;
	}

	private class ActionPanOrSelectNode implements IAction {
		private void scrollify(Widget w, final Vec dir) {
			w.onMouseDown.connect(new MouseEvent.Listener() {
				public void onMouseEvent(MouseEvent e) {
					mScroll = dir;
				}
			});
			w.onMouseUp.connect(new MouseEvent.Listener() {
				public void onMouseEvent(MouseEvent e) {
					mScroll = new Vec(0, 0);
				}
			});
		}
		public ActionPanOrSelectNode() {
			mWidget = new Widget() {
				public void onRender(RenderTarget t) {
					mOriginOffset = mOriginOffset.add(mScroll);
				}
			};
			mWidget.setSize(new UDim(0, 0, 1, 1));
			mWidget.setActive(false);
			mWidget.setParent(NodeMapView.this);
			mWidget.setVisible(false);
			//
			WidgetImage scrollRight = new WidgetImage();
			scrollRight.setPos(new UDim(-64, -64, 1, 0.5f));
			scrollRight.setSize(new UDim(64, 128));
			scrollRight.setParent(mWidget);
			scrollRight.setImage(ResourceLoader.LoadImage("scroll-right.png"));
			scrollify(scrollRight, new Vec(-MAP_SCROLL_SPEED, 0));
			//
			WidgetImage scrollLeft = new WidgetImage();
			scrollLeft.setPos(new UDim(0, -64, 0, 0.5f));
			scrollLeft.setSize(new UDim(64, 128));
			scrollLeft.setParent(mWidget);
			scrollLeft.setImage(ResourceLoader.LoadImage("scroll-left.png"));
			scrollify(scrollLeft, new Vec(MAP_SCROLL_SPEED, 0));
			//
			WidgetImage scrollDown = new WidgetImage();
			scrollDown.setPos(new UDim(-64, -64, 0.5f, 1));
			scrollDown.setSize(new UDim(128, 64));
			scrollDown.setParent(mWidget);
			scrollDown.setImage(ResourceLoader.LoadImage("scroll-down.png"));
			scrollify(scrollDown, new Vec(0, -MAP_SCROLL_SPEED));
			//
			WidgetImage scrollup = new WidgetImage();
			scrollup.setPos(new UDim(-64, 0, 0.5f, 0));
			scrollup.setSize(new UDim(128, 64));
			scrollup.setParent(mWidget);
			scrollup.setImage(ResourceLoader.LoadImage("scroll-up.png"));
			scrollify(scrollup, new Vec(0, MAP_SCROLL_SPEED));
			//inv button
			//
			CutoffCornerWidget inventoryButton = new CutoffCornerWidget(0, 0, 1, 0);
			inventoryButton.setSize(new UDim(150, 30));
			inventoryButton.setPos(new UDim(-150, 0, 1, 0));
			inventoryButton.setColor(HEADER_COLOR);
			inventoryButton.setParent(NodeMapView.this);
			WidgetLabel invtext = new WidgetLabel();
			invtext.setSize(new UDim(0, 0, 1, 1));
			invtext.setBackground(false);
			invtext.setActive(false);
			invtext.setTextColor(HEADER_TEXT_COLOR);
			invtext.setText("INVENTORY");
			invtext.setParent(inventoryButton);
			inventoryButton.onMouseDown.connect(new MouseEvent.Listener() {
				public void onMouseEvent(MouseEvent e) {
					setAction(mActionInventory);
				}
			});
		}
		public void onSelect() {
			showGui();
		}
		public void onDeselect() {
			hideGui();
		}
		public void onClick(NodeMap.Node nd) {
			if (nd.NStatus != NodeMap.Node.Status.Unknown) {
				mActionViewNodeStats.setState(nd);
				setAction(mActionViewNodeStats);
			}
		}
		public void showGui() {
			mWidget.setVisible(true);
		}
		public void hideGui() {
			mWidget.setVisible(false);
		}
		
		private Vec mScroll = new Vec(0, 0);
		private Widget mWidget;
	}
	
	private class ActionViewNodeStats implements IAction {
		public ActionViewNodeStats() {
			mEnterBattleWidget = new WidgetRect();
			mEnterBattleWidget.setSize(new UDim(400, 400));
			mEnterBattleWidget.setPos(new UDim(-200, -200, 0.5f, 0.5f));
			mEnterBattleWidget.setColor(new Color(128, 128, 128, 200));
			mEnterBattleWidget.setParent(NodeMapView.this);
			mEnterBattleWidget.setVisible(false);
			//
			WidgetLabel nodename = new WidgetLabel();
			nodename.setSize(new UDim(0, 20, 1, 0));
			nodename.setBackground(false);
			nodename.setTextColor(Color.yellow);
			nodename.setParent(mEnterBattleWidget);
			mNodeNameWidget = nodename;
			//
			WidgetImage nodeimage = new WidgetImage();
			nodeimage.setSize(new UDim(64, 64));
			nodeimage.setPos(new UDim(20, -32, 0, 0.5f));
			nodeimage.setParent(mEnterBattleWidget);
			mNodeImageWidget = nodeimage;
			//
			WidgetText nodedesc = new WidgetText();
			nodedesc.setSize(new UDim(-124, 128, 1, 0));
			nodedesc.setPos(new UDim(104, -64, 0, 0.5f));
			nodedesc.setBackground(false);
			nodedesc.setTextColor(Color.yellow);
			nodedesc.setTextAlign(TextAlign.Left);
			nodedesc.setParent(mEnterBattleWidget);
			mNodeDescWidget = nodedesc;
			//
			WidgetLabel enter = new WidgetLabel();
			enter.setSize(new UDim(0, 30, 0.5f, 0));
			enter.setPos(new UDim(0, -30, 0, 1));
			enter.setColor(Color.green);
			enter.setTextColor(Color.black);
			enter.setText("ENTER NODE");
			enter.setParent(mEnterBattleWidget);
			WidgetPadding.applyPadding(enter, 4);
			enter.onMouseDown.connect(new MouseEvent.Listener() {
				public void onMouseEvent(MouseEvent e) {
					if (mTargetNode.DataBattle != null && mTargetNode.NType == NodeMap.Node.Type.Battle) {
						if (mTargetNode.isOpen()) {
							enterBattle();
						}
					}
					else if (mTargetNode.Warez != null && mTargetNode.NType == NodeMap.Node.Type.Warez) {
						enterWarez();
					}
				}
			});
			mEnterButton = enter;
			//
			WidgetLabel cancel = new WidgetLabel();
			cancel.setSize(new UDim(0, 30, 0.5f, 0));
			cancel.setPos(new UDim(0, -30, 0.5f, 1));
			cancel.setColor(Color.red);
			cancel.setTextColor(Color.black);
			cancel.setText("CANCEL");
			cancel.setParent(mEnterBattleWidget);
			WidgetPadding.applyPadding(cancel, 4);
			cancel.onMouseDown.connect(new MouseEvent.Listener() {
				public void onMouseEvent(MouseEvent e) {
					setAction(mActionPanOrSelectNode);
				}
			});
		}
		public void onSelect() {
			showGui();
		}
		public void onDeselect() {
			hideGui();
		}
		public void onClick(NodeMap.Node nd) {
			
		}
		public void showGui() {
			mEnterBattleWidget.setVisible(true);
		}
		public void hideGui() {
			mEnterBattleWidget.setVisible(false);
		}
		public void setState(NodeMap.Node nd) {
			mTargetNode = nd;
			mNodeNameWidget.setText(nd.Title);
			mNodeImageWidget.setImage(nd.Image);
			mNodeDescWidget.setText(nd.Desc);
			if (nd.isOpen()) mEnterButton.setColor(Color.green);
			else mEnterButton.setColor(Color.gray);
		}
		public void enterBattle() {
			NodeMapView.this.setVisible(false);
			mGame.enterDataBattle(mTargetNode.DataBattle, mTargetNode);
		}
		public void enterWarez() {
			mTargetNode.setDefeated();
			mActionViewWarez.setState(mTargetNode.Warez);
			setAction(mActionViewWarez);
		}
		
		private NodeMap.Node mTargetNode;
		private WidgetRect mEnterBattleWidget;
		private WidgetLabel mNodeNameWidget;
		private WidgetImage mNodeImageWidget;
		private WidgetText mNodeDescWidget;
		private WidgetLabel mEnterButton;
	}

	private class ActionViewMessage implements IAction {
		public ActionViewMessage() {
			mMessageWidget = new WidgetRect();
			mMessageWidget.setSize(new UDim(400, 400));
			mMessageWidget.setPos(new UDim(-200, -200, 0.5f, 0.5f));
			mMessageWidget.setColor(new Color(128, 128, 128, 200));
			mMessageWidget.setParent(NodeMapView.this);
			mMessageWidget.setVisible(false);
			//
			WidgetLabel title = new WidgetLabel();
			title.setSize(new UDim(0, 20, 1, 0));
			title.setBackground(false);
			title.setTextColor(Color.yellow);
			title.setParent(mMessageWidget);
			mTitleWidget = title;
			//
			WidgetImage image = new WidgetImage();
			image.setSize(new UDim(64, 64));
			image.setPos(new UDim(20, 40, 0, 0f));
			image.setParent(mMessageWidget);
			mImageWidget = image;
			//
			WidgetText text = new WidgetText();
			text.setSize(new UDim(-124, 128, 1, 0));
			text.setPos(new UDim(104, 40, 0, 0f));
			text.setBackground(false);
			text.setTextColor(Color.yellow);
			text.setTextAlign(TextAlign.Left);
			text.setParent(mMessageWidget);
			mTextWidget = text;
			//
			
		}
		public void onSelect() {
			showGui();
		}
		public void onDeselect() {
			hideGui();
		}
		public void onClick(NodeMap.Node nd) {
			
		}
		public void showGui() {
			mMessageWidget.setVisible(true);
		}
		public void hideGui() {
			mMessageWidget.setVisible(false);
		}
		public void setState(MessageInfo msg) {
			mTargetMessage = msg;
			mTitleWidget.setText(msg.getTitle());
			mImageWidget.setImage(msg.getImage());
			mTextWidget.setText(msg.getText());

			for (WidgetLabel w : mButtons) {
				w.setParent(null);
			}
			mButtons.clear();

			int i = 0;

			for (final MessageInfo.ButtonEntry button : msg.getButtons()) {
				WidgetLabel widget = new WidgetLabel();
				widget.setSize(new UDim(0, 30, 1, 0));
				widget.setPos(new UDim(0, i-=30, 0, 1));
				widget.setColor(Color.blue);
				widget.setTextColor(Color.white);
				widget.setText(button.Text);
				widget.setParent(mMessageWidget);
				WidgetPadding.applyPadding(widget, 4);
				widget.onMouseDown.connect(new MouseEvent.Listener() {
						public void onMouseEvent(MouseEvent e) {
							for (MessageInfo. Action a : button.OnClick) {
								switch (a.Type) {
								case OpenDatabattle:
									enterBattle(a.Databattle);
									break;
								case OpenNode:
									enterNode(a.Node);
									break;
								case SetVisible:
									mTarget.getNodeByName(a.Node).setVisible();
									break;
								case ShowMessage:
									setState(mGame.getMessageLibrary().getMessageByName(a.NextMessage));
									break;
								case GiveCredit:
									mGame.getInventory().modCredit(a.Credit);
									break;
								case GiveAgent:
									mGame.getInventory().addAgent(mGame.getAgentLibrary().getAgentByName(a.Agent));
									break;
								case Close:
									mGame.saveData();
									setAction(mActionPanOrSelectNode);
									checkNewMessages();
									break;
							}
							}
						}
					});
				mButtons.add(widget);
			}
		}
		public void enterBattle(String name) {
			NodeMapView.this.setVisible(false);
			mGame.enterDataBattle(mGame.getDataBattleLibrary().getDataBattleByName(name), null);
		}
		public void enterNode(String name) {
			NodeMap.Node node = mTarget.getNodeByName(name);
			NodeMapView.this.setVisible(false);
			mGame.enterDataBattle(node.DataBattle, node);
		}
		
		private MessageInfo mTargetMessage;
		private WidgetRect mMessageWidget;
		private WidgetLabel mTitleWidget;
		private WidgetImage mImageWidget;
		private WidgetText mTextWidget;

		private ArrayList<WidgetLabel> mButtons = new ArrayList<WidgetLabel>();
	}

	/************************************************/
	private class ActionInventory implements IAction {
		private class AbilityDisplay {
			WidgetText Name;
			WidgetText Desc;
			Widget Container;
			AgentInfo.Ability Ability;
		}
		
		private class AgentThumbDisplay extends WidgetImage {
			public void onRender(RenderTarget t) {
				if (getImage() != null) {
					Rect r = getRect();
					Graphics g = t.getContext();
					g.setColor(mAgentColor);
					//g.fillOval(r.getX(), r.getY(), r.getWidth(), r.getHeight());
					g.drawImage(getImage(), r.getX()+r.getWidth()/2-7, r.getY()+r.getHeight()/2-7, null);
				}
			}
			public void setAgentColor(Color c) {
				mAgentColor = c;
			}
			private Color mAgentColor;
		}

		public ActionInventory() {
			mWidget = new Widget();
			
			mWidget.setParent(NodeMapView.this);
			mWidget.setVisible(false);
			mWidget.setActive(false); //just a container, no need to get events
			mWidget.setSize(new UDim(0, 0, 1, 1));
			//
			mAbilityContainer = new WidgetRect();
			mAbilityContainer.setSize(new UDim(250, 150));
			mAbilityContainer.setPos(new UDim(0, -150, 0, 1));
			mAbilityContainer.setColor(CONTENT_COLOR);
			mAbilityContainer.setParent(mWidget);
			CutoffCornerWidget header = new CutoffCornerWidget(0, 1, 0, 0);
			header.setSize(new UDim(0, 20, 1, 0));
			header.setPos(new UDim(0, -20, 0, 0));
			header.setColor(HEADER_COLOR);
			header.setParent(mAbilityContainer);
			WidgetLabel headerlb = new WidgetLabel();
			headerlb.setSize(new UDim(0, 0, 1, 1));
			headerlb.setBackground(false);
			headerlb.setActive(false);
			headerlb.setTextColor(HEADER_TEXT_COLOR);
			headerlb.setText("ABILITIES");
			headerlb.setParent(header);
			//
			WidgetRect agentContainer = new WidgetRect();
			agentContainer.setSize(new UDim(150, 150));
			agentContainer.setPos(new UDim(0, -150-170, 0, 1));
			agentContainer.setColor(CONTENT_COLOR);
			agentContainer.setParent(mWidget);
			header = new CutoffCornerWidget(0, 1, 0, 0);
			header.setSize(new UDim(0, 20, 1, 0));
			header.setPos(new UDim(0, -20, 0, 0));
			header.setColor(HEADER_COLOR);
			header.setParent(agentContainer);
			headerlb = new WidgetLabel();
			headerlb.setSize(new UDim(0, 0, 1, 1));
			headerlb.setBackground(false);
			headerlb.setTextColor(HEADER_TEXT_COLOR);
			headerlb.setText("AGENT");
			headerlb.setParent(header);
			//
			mAgentThumb = new AgentThumbDisplay();
			mAgentThumb.setSize(new UDim(18, 18));
			mAgentThumb.setPos(new UDim(-10, 6, 0.5f, 0));
			mAgentThumb.setParent(agentContainer);
			//
			mAgentName = new WidgetText();
			mAgentName.setSize(new UDim(0, 14, 1, 0));
			mAgentName.setPos(new UDim(0, 30, 0, 0));
			mAgentName.setTextAlign(TextAlign.Left);
			mAgentName.setTextColor(CONTENT_TEXT_COLOR);
			mAgentName.setBackground(false);
			mAgentName.setParent(agentContainer);
			//
			mAgentDesc = new WidgetText();
			mAgentDesc.setSize(new UDim(-5, -44, 1, 1));
			mAgentDesc.setPos(new UDim(5, 44));
			mAgentDesc.setTextAlign(TextAlign.Left);
			mAgentDesc.setTextColor(CONTENT_TEXT_COLOR);
			mAgentDesc.setBackground(false);
			mAgentDesc.setParent(agentContainer);
			
			//close button
			//
			CutoffCornerWidget closeButton = new CutoffCornerWidget(0, 0, 1, 0);
			closeButton.setSize(new UDim(150, 30));
			closeButton.setPos(new UDim(-150, 0, 1, 0));
			closeButton.setColor(HEADER_COLOR);
			closeButton.setParent(mWidget);
			WidgetLabel closetext = new WidgetLabel();
			closetext.setSize(new UDim(0, 0, 1, 1));
			closetext.setBackground(false);
			closetext.setActive(false);
			closetext.setTextColor(HEADER_TEXT_COLOR);
			closetext.setText("LOG OUT");
			closetext.setParent(closeButton);
			closeButton.onMouseDown.connect(new MouseEvent.Listener() {
				public void onMouseEvent(MouseEvent e) {
					setAction(mActionPanOrSelectNode);
				}
			});

			//credits view
			//
			CutoffCornerWidget credits = new CutoffCornerWidget(1, 0, 0, 0);
			credits.setSize(new UDim(150, 30));
			credits.setPos(new UDim(-150, -30, 1, 1));
			credits.setColor(HEADER_COLOR);
			credits.setParent(mWidget);
			WidgetLabel creditstext = new WidgetLabel();
			creditstext.setSize(new UDim(0, 0, 1, 1));
			creditstext.setBackground(false);
			creditstext.setActive(false);
			creditstext.setTextColor(HEADER_TEXT_COLOR);
			creditstext.setParent(credits);
			mCreditText = creditstext;

			//agent list
			Widget agentSelectContainer = new Widget();
			agentSelectContainer.setSize(new UDim(100, 200));
			agentSelectContainer.setParent(mWidget);
			//
			mAgentListContainer = new WidgetRect();
			mAgentListContainer.setSize(new UDim(0.0f, -20.0f, 1, 1));
			mAgentListContainer.setColor(CONTENT_COLOR);
			mAgentListContainer.setParent(agentSelectContainer);
			CutoffCornerWidget bottom = new CutoffCornerWidget(0, 0, 0, 1);
			bottom.setSize(new UDim(0, 20, 1, 0));
			bottom.setPos(new UDim(0, -20, 0, 1));
			bottom.setColor(HEADER_COLOR);
			bottom.setParent(agentSelectContainer);
			update();
		}
		
		protected void update() {
			mCreditText.setText(mGame.getInventory().getCredit() + " Cr.");

			for (WidgetLabel row : mRows) {
				row.setParent(null);
			}
			mRows.clear();

			int yoffs = 0;
			for (final Inventory.InventoryEntry agententry : mGame.getInventory().getAgents()) {
				final WidgetLabel row = new WidgetLabel();
				row.setSize(new UDim(0, 16, 1, 0));
				row.setPos(new UDim(0, yoffs));
				yoffs += 16;
				row.setBackground(false);
				row.setTextColor(CONTENT_TEXT_COLOR);
				row.setText(agententry.Quantity + "x " + agententry.Agent.getName());
				row.setParent(mAgentListContainer);
				row.onMouseDown.connect(new MouseEvent.Listener() {
					public void onMouseEvent(MouseEvent e) {
						setAgentInfo(agententry.Agent);
					}
				});
				mRows.add(row);
			}
		}

		public void setAgentInfo(AgentInfo a) {

			if (a == null) {
				for (int i = 0; i < mAbilityDisplays.size(); ++i)
					mAbilityDisplays.get(i).Container.setVisible(false);
				mAgentThumb.setImage(null);
				mAgentName.setText("");
				mAgentDesc.setText("");
			} else {
				AgentInfo.Ability[] abs = a.getAbilities();
				
				//set up ability displays
				while (abs.length > mAbilityDisplays.size()) //make sure there's enough
					addNewAbilityDisplay();
				float frac = 1/((float)mAbilityDisplays.size());
				for (int i = 0; i < abs.length; ++i) { //show each ability
					AbilityDisplay disp = mAbilityDisplays.get(i);
					AgentInfo.Ability ab = abs[i];
					disp.Container.setSize(new UDim(0, 0, frac, 1));
					disp.Container.setPos(new UDim(0, 0, frac*i, 0));
					disp.Container.setVisible(true);
					disp.Name.setText(ab.getName());
					disp.Desc.setText(ab.getDesc());
					disp.Ability = ab;
				}
				for (int i = abs.length; i < mAbilityDisplays.size(); ++i) //hide extra displays
					mAbilityDisplays.get(i).Container.setVisible(false);
				
				//set up the agent name/desc display
				mAgentThumb.setImage(a.getThumb());
				mAgentThumb.setAgentColor(a.getColor());
				mAgentName.setText(a.getName());

				//set up desc information
				String desc = a.getDesc();
				desc += "\nMax size: " + a.getSize();
				desc += "\nMove: " + a.getMove();
				mAgentDesc.setText(desc);
			}
		}
		
		private void addNewAbilityDisplay() {
			final AbilityDisplay disp = new AbilityDisplay();
			disp.Container = new Widget();
			disp.Container.setSize(new UDim(0, 0, 0.5f, 1.0f));
			disp.Container.setParent(mAbilityContainer);
			disp.Name = new WidgetText();
			disp.Name.setSize(new UDim(0, 14, 1, 0));
			disp.Name.setBackground(false);
			disp.Name.setTextAlign(TextAlign.Left);
			disp.Name.setTextColor(CONTENT_TEXT_COLOR);
			disp.Name.setParent(disp.Container);
			disp.Desc = new WidgetText();
			disp.Desc.setSize(new UDim(-4, -28, 1, 1));
			disp.Desc.setPos(new UDim(4, 14, 0, 0));
			disp.Desc.setBackground(false);
			disp.Desc.setTextColor(CONTENT_TEXT_COLOR);
			disp.Desc.setTextAlign(TextAlign.Left);
			disp.Desc.setParent(disp.Container);
			//
			mAbilityDisplays.add(disp);
		}
		
		public void onSelect() {
			update();
			showGui();
		}
		public void onDeselect() {
			hideGui();
		}
		public void onClick(NodeMap.Node nd) {
			
		}
		public void showGui() {
			mWidget.setVisible(true);
		}
		public void hideGui() {
			mWidget.setVisible(false);
		}

		private Widget mWidget;
		private WidgetRect mAbilityContainer;
		private WidgetText mAgentName;
		private WidgetText mAgentDesc;
		private WidgetLabel mCreditText;
		private AgentThumbDisplay mAgentThumb;
		private ArrayList<AbilityDisplay> mAbilityDisplays = new ArrayList<AbilityDisplay>();
		private WidgetRect mAgentListContainer;
		private ArrayList<WidgetLabel> mRows = new ArrayList<WidgetLabel>();
	}

	private class ActionViewWarez extends ActionInventory implements IAction {
		public ActionViewWarez() {
			super();
			mListWidget = new WidgetRect();
			mListWidget.setSize(new UDim(400, 400));
			mListWidget.setPos(new UDim(-200, -200, 0.5f, 0.5f));
			mListWidget.setColor(new Color(128, 128, 128, 200));
			mListWidget.setParent(NodeMapView.this);
			mListWidget.setVisible(false);
			//
			WidgetLabel title = new WidgetLabel();
			title.setSize(new UDim(0, 20, 1, 0));
			title.setBackground(false);
			title.setTextColor(Color.yellow);
			title.setParent(mListWidget);
			mTitleWidget = title;
			//
		}
		public void onSelect() {
			update();
			showGui();
		}
		public void onDeselect() {
			hideGui();
		}
		public void onClick(NodeMap.Node nd) {
			
		}
		public void showGui() {
			super.showGui();
			mListWidget.setVisible(true);
		}
		public void hideGui() {
			super.hideGui();
			mListWidget.setVisible(false);
		}
		public void setState(List<NodeMap.WarezItem> agents) {
			mTitleWidget.setText("AVAILABLE WAREZ");

			for (WidgetRect w : mItems) {
				w.setParent(null);
			}
			mItems.clear();

			int i = -30+20;

			Collections.sort(agents, new Comparator<NodeMap.WarezItem>() {
					public int compare(NodeMap.WarezItem a, NodeMap.WarezItem b) {
						return a.Value-b.Value;
					}
				});

			for (final NodeMap.WarezItem item : agents) {
				final AgentInfo agent = item.Agent;
				final int value = item.Value;

				WidgetRect widget = new WidgetRect();
				widget.setSize(new UDim(0, 30, 1, 0));
				widget.setPos(new UDim(0, i+=30, 0, 0f));
				widget.setParent(mListWidget);
				widget.setColor(new Color(128, 128, 128, 200));
				WidgetPadding.applyPadding(widget, 4);

				//
				WidgetLabel name = new WidgetLabel();
				name.setSize(new UDim(0, 22, 0.33f, 0));
				name.setPos(new UDim(0, 0, 0f, 0f));
				name.setColor(new Color(0, 0, 0, 255));
				name.setTextColor(Color.white);
				name.setText(agent.getName());
				name.setParent(widget);
				WidgetPadding.applyPadding(name, 2);
				//
				WidgetLabel view = new WidgetLabel();
				view.setSize(new UDim(0, 22, 0.34f, 0));
				view.setPos(new UDim(0, 0, 0.33f, 0f));
				view.setColor(Color.blue);
				view.setTextColor(Color.white);
				view.setText("VIEW");
				view.setParent(widget);
				WidgetPadding.applyPadding(view, 2);
				view.onMouseDown.connect(new MouseEvent.Listener() {
						public void onMouseEvent(MouseEvent e) {
							setAgentInfo(agent);
						}
					});
				//
				WidgetLabel buy = new WidgetLabel();
				buy.setSize(new UDim(0, 22, 0.33f, 0));
				buy.setPos(new UDim(0, 0, 0.67f, 0f));
				buy.setColor(Color.green);
				buy.setTextColor(Color.black);
				buy.setText("BUY (" + value + " Cr.)");
				buy.setParent(widget);
				WidgetPadding.applyPadding(buy, 2);
				buy.onMouseDown.connect(new MouseEvent.Listener() {
						public void onMouseEvent(MouseEvent e) {
							if (mGame.getInventory().getCredit() >= value) {
								mGame.getInventory().modCredit(-value);
								mGame.getInventory().addAgent(agent);
								update();
								mGame.saveData();
							}
						}
					});
				mItems.add(widget);
			}
		}
		
		private WidgetRect mListWidget;
		private WidgetLabel mTitleWidget;

		private ArrayList<WidgetRect> mItems = new ArrayList<WidgetRect>();
	}

	public NodeMapView(NodeMap target, GameSession game) {
		mTarget = target;
		mGame = game;
		//
		setColor(Color.black);
		//
		mActionPanOrSelectNode = new ActionPanOrSelectNode();
		mActionViewNodeStats = new ActionViewNodeStats();
		mActionViewMessage = new ActionViewMessage();
		mActionInventory = new ActionInventory();
		mActionViewWarez = new ActionViewWarez();
		setAction(mActionPanOrSelectNode);
		//
		onMouseDown.connect(new MouseEvent.Listener() {
			public void onMouseEvent(MouseEvent e) {
				if (e.getButton() == MouseEvent.MouseButton.Left) {
					//find node to click
					Vec pos = e.getInput().getMousePos();
					for (NodeMap.Node nd : mTarget.getNodes()) {
						Vec ndPos = nd.Pos.mul(MAP_SCALE).add(mOriginOffset);
						if (pos.getX() > ndPos.getX()-32 && pos.getX() < ndPos.getX()+32
								&& pos.getY() > ndPos.getY()-32 && pos.getY() < ndPos.getY()+32) {
							mCurrentAction.onClick(nd);
						}
					}
				} else if (e.getButton() == MouseEvent.MouseButton.Right) {
					//start pan action
					mStartDragOriginOffset = mOriginOffset;
					mStartDragMousePos = e.getInput().getMousePos();
					mDragging = true;
				}
			}
		});
		onMouseUp.connect(new MouseEvent.Listener() {
			public void onMouseEvent(MouseEvent e) {
				mDragging = false;
			}
		});
		onMouseMove.connect(new MouseEvent.Listener() {
			public void onMouseEvent(MouseEvent e) {
				if (mDragging && mCurrentAction == mActionPanOrSelectNode) {
					Vec dif = mStartDragMousePos.sub(e.getInput().getMousePos());
					mOriginOffset = mStartDragOriginOffset.sub(dif);
				}
			}
		});
	}
	
	public void onRender(RenderTarget t) {
		super.onRender(t);
		Graphics g = t.getContext();
		//links
		for (NodeMap.Link ln : mTarget.getLinks()) {
			Vec apos = ln.NodeA.Pos.mul(MAP_SCALE).add(mOriginOffset);
			Vec bpos = ln.NodeB.Pos.mul(MAP_SCALE).add(mOriginOffset);
			if (ln.NodeA.NStatus == Status.Defeated || ln.NodeB.NStatus == Status.Defeated) {
				g.setColor(MAP_LINK_COLOR);
			} else if (ln.NodeA.NStatus == Status.Open || ln.NodeB.NStatus == Status.Open) {
				g.setColor(MAP_LINK_DARK_COLOR);
			} else {
				g.setColor(Color.black);
			}
			g.drawLine(apos.getX(), apos.getY(), bpos.getX(), bpos.getY());
		}
		
		//nodes
		for (NodeMap.Node nd : mTarget.getNodes()) {
			Vec pos = nd.Pos.mul(MAP_SCALE).add(mOriginOffset);
			switch (nd.NStatus) {
			case Unknown:
				g.setColor(MAP_LINK_DARK_COLOR);
				g.fillOval(pos.getX()-10, pos.getY()-5, 20, 10);
				break;
			case Open:
			case Visible:
				g.setColor(MAP_LINK_DARK_COLOR);
				g.fillOval(pos.getX()-10, pos.getY()-5, 20, 10);
				g.drawImage(nd.DarkImage, pos.getX()-32, pos.getY()-32, null);			
				break;
			case Defeated:
				g.setColor(MAP_LINK_COLOR);
				g.fillOval(pos.getX()-10, pos.getY()-5, 20, 10);
				g.drawImage(nd.Image, pos.getX()-32, pos.getY()-32, null);
				break;
			}
		}
	}
	
	public void enter() {
		setAction(mActionPanOrSelectNode);
		checkNewMessages();
	}

	private void checkNewMessages() {
		if (mGame.getNewMessages().size() != 0) {
			mActionViewMessage.setState(mGame.getNewMessages().remove(0));
			setAction(mActionViewMessage);
		}
	}
	
	private void setAction(IAction act) {
		if (mCurrentAction != null)
			mCurrentAction.onDeselect();
		mCurrentAction = act;
		if (mCurrentAction != null)
			mCurrentAction.onSelect();
	}

	private Vec mOriginOffset = new Vec(300, 300);
	private Vec mStartDragOriginOffset;
	private Vec mStartDragMousePos;
	private boolean mDragging = false;
	
	private IAction mCurrentAction;
	private ActionViewNodeStats mActionViewNodeStats;
	private ActionPanOrSelectNode mActionPanOrSelectNode;
	private ActionViewMessage mActionViewMessage;
	private ActionInventory mActionInventory;
	private ActionViewWarez mActionViewWarez;

	private final GameSession mGame;
	private final NodeMap mTarget;
	
	private static final Color MAP_LINK_DARK_COLOR = new Color(0, 128, 0);
	private static final Color MAP_LINK_COLOR = Color.green;
	private static final int MAP_SCALE = 128;
	private static final int MAP_SCROLL_SPEED = 8;

	private static final Color HEADER_COLOR = new Color(0x22, 0x66, 0xab);
	private static final Color HEADER_TEXT_COLOR = new Color(0xec, 0xb0, 0x1f);
	private static final Color CONTENT_COLOR = new Color(0x12, 0x47, 0x6d, 128);
	private static final Color CONTENT_TEXT_COLOR = new Color(0xe8, 0xd9, 0xb0);
}
