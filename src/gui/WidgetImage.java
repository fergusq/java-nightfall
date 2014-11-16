package gui;

import java.awt.Image;

public class WidgetImage extends Widget {
	public void onRender(RenderTarget t) {
		if (mImage != null)
			t.getContext().drawImage(mImage, getRect().getX(), getRect().getY(), null);
	}
	
	public void setImage(Image img) {
		mImage = img;
	}
	
	public Image getImage() {
		return mImage;
	}
	
	private Image mImage;
}
