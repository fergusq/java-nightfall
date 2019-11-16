package game;

import game.DataBattle.TileOverlay;

import java.util.*;
import util.*;
import java.awt.Color;
import java.awt.Image;


public class AgentInfo {
	//an ability
	public static abstract class Ability {
		public abstract void apply(Agent src, Agent target, Vec trg);
		public abstract void select(Agent src);
		public abstract int range();
		public abstract int damage();
		public abstract int minSize();
		public final String getName() {
			return mName;
		}
		public final String getDesc() {
			return mDesc;
		}
		public final void setName(String s) {
			mName = s;
		}
		public final void setDesc(String s) {
			mDesc = s;
		}
		private String mDesc = "";
		private String mName = "";
	}
	
	public static class AbilityDamageGeneric extends Ability {
		public AbilityDamageGeneric(String name, String desc, int range, int minsize, int dmg) {
			setName(name);
			setDesc(desc);
			mMinSize = minsize;
			mRange = range;
			mDamage = dmg;
		}
		public void select(Agent src) {
			if (src.getSize() >= mMinSize)
				src.getBoard().attackFlood(src.getPos(), mRange, TileOverlay.Neg);
		}
		public void apply(Agent src, Agent target, Vec trg) {
			if (target != null && target.getTeam() != src.getTeam() && src.getSize() >= mMinSize)
				target.damage(mDamage);
		}
		public int range() {
			return mRange;
		}
		public int damage() {
			return mDamage;
		}
		public int minSize() {
			return mMinSize;
		}
		private int mDamage;
		private int mRange;
		private int mMinSize;
	}

	public static class AbilityDamageSelfHarming extends Ability {
		public AbilityDamageSelfHarming(String name, String desc, int range, int minsize, int dmg, int selfdmg) {
			setName(name);
			setDesc(desc);
			mMinSize = minsize;
			mRange = range;
			mDamage = dmg;
			mSelfDamage = selfdmg;
		}
		public void select(Agent src) {
			if (src.getSize() >= mMinSize)
				src.getBoard().attackFlood(src.getPos(), mRange, TileOverlay.Neg);
		}
		public void apply(Agent src, Agent target, Vec trg) {
			if (target != null && target.getTeam() != src.getTeam() && src.getSize() >= mMinSize) {
				target.damage(mDamage);
				src.damage(mSelfDamage);
			}
		}
		public int range() {
			return mRange;
		}
		public int damage() {
			return mDamage;
		}
		public int minSize() {
			return mMinSize;
		}
		private int mMinSize;
		private int mDamage;
		private int mSelfDamage;
		private int mRange;
	}

	public static class AbilityExpandTail extends Ability {
		public AbilityExpandTail(String name, String desc, int range, int minsize, int expand, int selfdmg) {
			setName(name);
			setDesc(desc);
			mMinSize = minsize;
			mRange = range;
			mExpand = expand;
			mSelfDamage = selfdmg;
		}
		public void select(Agent src) {
			if (src.getSize() >= mMinSize)
				src.getBoard().attackFlood(src.getPos(), mRange, TileOverlay.Pos);
		}
		public void apply(Agent src, Agent target, Vec trg) {
			if (target != null && target.getTeam() == src.getTeam() && src.getSize() >= mMinSize) {
				target.expand(mExpand);
				src.damage(mSelfDamage);
			}
		}
		public int range() {
			return mRange;
		}
		public int damage() {
			return 0;
		}
		public int minSize() {
			return mMinSize;
		}
		private int mMinSize;
		private int mExpand;
		private int mSelfDamage;
		private int mRange;
	}

	public static class AbilityModifyProperties extends Ability {
		public AbilityModifyProperties(String name, String desc, int range, int minsize, int movemod, int sizemod, int selfdmg) {
			setName(name);
			setDesc(desc);
			mMinSize = minsize;
			mRange = range;
			mMovemod = movemod;
			mSizemod = sizemod;
			mSelfDamage = selfdmg;
		}
		public void select(Agent src) {
			if (src.getSize() >= mMinSize)
				src.getBoard().attackFlood(src.getPos(), mRange, TileOverlay.Pos);
		}
		public void apply(Agent src, Agent target, Vec trg) {
			if (target != null /*&& target.getTeam() == src.getTeam()*/ && src.getSize() >= mMinSize) {
				target.modMove(mMovemod);
				target.modMaxSize(mSizemod);
				src.damage(mSelfDamage);
			}
		}
		public int range() {
			return mRange;
		}
		public int damage() {
			return 0;
		}
		public int minSize() {
			return mMinSize;
		}
		private int mMinSize;
		private int mMovemod;
		private int mSizemod;
		private int mSelfDamage;
		private int mRange;
	}

	public static class AbilityModifyGrid extends Ability {
		public AbilityModifyGrid(String name, String desc, int range, int minsize, boolean one) {
			setName(name);
			setDesc(desc);
			mMinSize = minsize;
			mRange = range;
			mOne = one;
		}
		public void select(Agent src) {
			src.getBoard().attackFlood(src.getPos(), mRange, zero(), TileOverlay.Mod);
		}
		public void apply(Agent src, Agent target, Vec trg) {
			if (target == null && src.getSize() >= mMinSize) {
				DataBattle board = src.getBoard();
				DataBattle.Tile t = board.getTile(trg);
				if (!t.Filled && one()) {
					t.Filled = true;
					board.onBoardChange.fire(trg);
				}
				else if (t.Filled && t.Agent == null && zero()) {
					t.Filled = false;
					board.onBoardChange.fire(trg);
				}
			}
		}
		public int range() {
			return mRange;
		}
		public boolean one() {
			return mOne;
		}
		public boolean zero() {
			return !mOne;
		}
		public int damage() {
			return 0;
		}
		public int minSize() {
			return mMinSize;
		}
		private int mMinSize;
		private boolean mOne;
		private int mRange;
	}
	
	protected void setAbilities(Ability[] abilities) {
		mAbilities = abilities;
	}
	
	public Ability[] getAbilities() {
		return mAbilities;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setDesc(String desc) {
		mDesc = desc;
	}
	
	public String getDesc() {
		return mDesc;
	}
	
	public void setSize(int size) {
		mSize = size;
	}
	
	public int getSize() {
		return mSize;
	}
	
	public void setMove(int move) {
		mMove = move;
	}
	
	public int getMove() {
		return mMove;
	}
	
	public void setColor(Color c) {
		mColor = c;
	}
	
	public Color getColor() {
		return mColor;
	}
	
	public void setThumb(Image im) {
		mThumb = im;
	}
	
	public Image getThumb() {
		return mThumb;
	}
	
	private Ability[] mAbilities = {};
	private String mName;
	private String mDesc;
	private int mSize;
	private int mMove;
	private Color mColor;
	private Image mThumb;
}
