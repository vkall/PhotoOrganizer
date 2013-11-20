package slideshow;

//package ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import logic.Photo;

//import photo.Photo;

/**
 * SlideShowView implements the view part of a photo slide show
 * 
 * @author jbaek, rcm, iporres
 * 
 */
@SuppressWarnings("serial")
public class SlideShowView extends JComponent {

	/**
	 * Dimension of a thumbnail image. A thumbnail must fit in a square bounding
	 * box that is THUMBNAIL_SIZE pixels on a side.
	 */
	public static final int THUMBNAIL_SIZE = 500;

	// the photo on disk
	private Photo photo;

	// the loaded, displayable thumbnail image
	private BufferedImage bufferedImage;

	// width & height in pixels of the thumbnail image (not the
	// original photo!)
	private int width;
	private int height;

	private float alpha = 1.0f;
	private String text = "";

	public SlideShowView() {

		// setBackground(Color.BLACK);
		// setDoubleBuffered(true);
	}

	public float getAlpha() {
		return alpha;

	}

	/**
	 * sets the alpha value for the
	 * \href{http://en.wikipedia.org/wiki/Alpha_compositing}{alpha compositing}
	 * or transparency for the photo. The alpha value is a float in the interval
	 * $[0,1]$. The background of the slide show is black, so setting the alpha
	 * to 0 will show a black image while setting the alpha to 1 will show the
	 * picture without any alpha effect. The view will be repainted
	 * automatically.
	 * 
	 * @param alpha
	 *            the new alpha value
	 */
	public void setAlpha(float alpha) {
		assert alpha >= 0 && alpha <= 1;
		this.alpha = alpha;
		// force repaint
		repaint();
	}

	/**
	 * sets the photo to show in the SlideShowView. The view will be repainted
	 * automatically.
	 * 
	 * @param p
	 *            the new photo to show
	 */
	public void setPhoto(Photo p) {
		photo = p;

		if (p == null) {
			bufferedImage = null;
		} else {

			ImageIcon icon = new ImageIcon(p.getFile().getAbsolutePath());
			int w = icon.getIconWidth();
			int h = icon.getIconHeight();
			if (w < THUMBNAIL_SIZE && h < THUMBNAIL_SIZE) {
				// image smaller than the box
				width = w;
				height = h;
			} else if (w > h) {
				width = THUMBNAIL_SIZE;
				height = THUMBNAIL_SIZE * h / w;
			} else {
				height = THUMBNAIL_SIZE;
				width = THUMBNAIL_SIZE * w / h;
			}

			bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			bufferedImage.createGraphics().drawImage(icon.getImage(), 0, 0,
					width, height, null);
		}
		// force repaint
		repaint();

	}

	/**
	 * @return the photo for this thumbnail
	 */
	public Photo getPhoto() {
		return photo;
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setBackground(Color.BLACK);

		// put the origin at the center of our bounds
		Dimension d = getSize();
		g2.clearRect(0, 0, d.width, d.height);

		if (bufferedImage != null) {
			// draw the image at (-w/2, -h/2) relative to the center point
			int x = -width / 2;
			int y = -height / 2;

			// set the opacity
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					alpha));
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g2.drawImage(bufferedImage, d.width / 2 + x, d.height / 2 + y, this);

			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					1.0f));
			g2.setColor(Color.WHITE);
			g2.drawString(text, 20, d.height - 30);
		}

	}

	/**
	 * Sets the text to view shown in the view. The previous text is discarded.
	 * 
	 * @param new_text
	 *            the new text to show
	 */
	public void setText(String new_text) {
		text = new_text;

	}
}
