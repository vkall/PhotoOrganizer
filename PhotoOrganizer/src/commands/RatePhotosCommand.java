package commands;

import java.util.Set;

import logic.Photo;

public class RatePhotosCommand implements Command {

	private Photo[] photos;
	private int[] oldRatings;
	private int rating;
	
	public RatePhotosCommand (Set<Photo> photos, int rating) {
		this.photos = photos.toArray(new Photo[photos.size()]);
		this.rating = rating;

		this.oldRatings = new int[this.photos.length];
		
	}

	@Override
	public void doCommand() {
		for (int i = 0; i < photos.length; i++) {
			oldRatings[i] = photos[i].getRating();
			photos[i].setRating(rating);
		}
		System.out.println("gave " + photos.length + " photos a rating of " + rating);
	}

	@Override
	public void undoCommand() {
		for (int i = 0; i < photos.length; i++) {
			photos[i].setRating(oldRatings[i]);
		}
	}

	@Override
	public String commandName() {
		return "RatePhotosCommand";
	}

}
