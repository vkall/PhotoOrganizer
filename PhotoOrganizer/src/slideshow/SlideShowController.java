package slideshow;

import java.util.*;

import logic.Album;
import logic.Photo;

public class SlideShowController {

	private enum State {
		FADEIN, FADEOUT, SHOWPHOTO, PAUSED
	};

	private State state, previous;
	private Album album;
	private SlideShowView view;
	private int counter;
	private Iterator<Photo> i;
	private float alpha;

	public SlideShowController(Album a, SlideShowView v) {
		album = a;
		view = v;
		counter = 0;
		view.setText(album.toString());
		state = State.SHOWPHOTO;
		i = album.getPhotoSet().iterator();
	}

	public void timeTick() {
		switch (state) {
		case FADEIN:
			alpha = view.getAlpha();
			counter++;
			if (counter >= 50) {
				state = State.SHOWPHOTO;
				counter = 0;
			} else {
				view.setAlpha((float) (alpha + 0.02));
			}
			break;
		case FADEOUT:
			alpha = view.getAlpha();
			counter++;
			if (counter >= 50) {
				counter = 0;
				if (!i.hasNext())
					i = album.getPhotoSet().iterator();
				Photo p = i.next();
				view.setPhoto(p);
				state = State.FADEIN;
			} else {
				view.setAlpha((float) (alpha - 0.02));
			}
			break;
		case SHOWPHOTO:
			view.setAlpha(1);
			counter++;
			if (counter >= 75) {
				view.setText("");
				counter = 0;
				state = State.FADEOUT;
			}
			break;
		case PAUSED:
			break;
		default:
			break;
		}
	}

	public void startPressed() {
		counter = 0;
		view.setText(album.toString());
		i = album.getPhotoSet().iterator();
		state = State.SHOWPHOTO;
		Photo p = i.next();
		view.setPhoto(p);
	}

	public void nextSlidePressed() {
		counter = 0;
		if (!i.hasNext())
			i = album.getPhotoSet().iterator();
		Photo p = i.next();
		view.setPhoto(p);
		state = State.SHOWPHOTO;
	}

	public void pauseUnpausePressed() {
		if (state != State.PAUSED) {
			previous = state;
			state = State.PAUSED;
		} else {
			state = previous;
		}
	}

}
