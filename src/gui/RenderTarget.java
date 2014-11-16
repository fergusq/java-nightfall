package gui;

import util.*;
import java.awt.Graphics;

public interface RenderTarget {
	Vec getRenderSize();
	Graphics getContext();
}
