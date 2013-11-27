package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

import logic.Photo;

@SuppressWarnings("serial")
public class ThumbnailProxy extends JComponent {
	
	private Photo photo;
	private Thumbnail realThumbnail;
	public static final int THUMBNAIL_SIZE = 150;
	private int width;
	private int height;
	private boolean isSelected;
	private BufferedImage bufferedImage;

	
	public ThumbnailProxy(Photo p) {
		this.photo = p;
		this.realThumbnail = null;
		this.width = THUMBNAIL_SIZE;
		this.height = THUMBNAIL_SIZE;
		
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D imageGraphic = bufferedImage.createGraphics();

		// draw the thumbnail image
		imageGraphic.setColor(Color.gray);
		int x = 0;
		int y = 0;
		imageGraphic.fillRect(x, y, width, height);
		
		imageGraphic.setColor(Color.black);
		Font f = new Font("f", Font.BOLD, 8);
		imageGraphic.setFont(f);
		imageGraphic.drawString(photo.getFile().getName(), 10, 20);
		
		
		setPreferredSize(new Dimension(width, height));

		isSelected = false;

		addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				toggle();
			}
		});
		
	}

	
	/**
	 * @return the photo for this thumbnail
	 */
	public Photo getPhoto() {
		return photo;
	}

	/**
	 * @return true iff this thumbnail was selected by the user
	 */
	public boolean isSelected() {
		if (realThumbnail == null) {
			return isSelected;
		} else {
			return realThumbnail.isSelected();
		}
	}

	/**
	 * Toggles the selection state of this thumbnail.
	 */
	public void toggle() {
		isSelected = !isSelected;
		if (realThumbnail != null) {
			realThumbnail.toggle();
		}
		
		repaint();
	}

	@Override
	public void paintComponent(final Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (realThumbnail == null) {

			// put the origin at the center of our bounds
			Dimension d = getSize();
			g2.translate(d.width / 2, d.height / 2);
			
			int x = -width / 2;
			int y = -height / 2;
			g2.drawImage(bufferedImage, x, y, this);
			
			// draw selection highlight over the image
			if (isSelected()) {
				g2.setColor(new Color(0.0f /* red */, 0.0f /* green */,
						1.0f /* blue */, 0.5f /* alpha (transparency) */));
				g2.fillRect(x, y, width, height);
			}
		}
		else {
			realThumbnail.paintComponent(g2);
		}
	}


	public void loadRealThumbnail() {
		realThumbnail = new Thumbnail(photo);
	}
	

}
