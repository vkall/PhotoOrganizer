package commands;

import java.util.Set;

import logic.Album;
import logic.Photo;

public class RemovePhotosCommand implements Command {

	private Album selectedAlbum;
	private Set<Photo> photos;
	
	public RemovePhotosCommand (Album selectedAlbum, Set<Photo> photos) {
		this.selectedAlbum = selectedAlbum;
		this.photos = photos;
	}

	@Override
	public void doCommand() {

		selectedAlbum.removePhotos(photos);

		System.out.println("remove " + photos.size() + " photos to album " + selectedAlbum);
	}

	@Override
	public void undoCommand() {
		selectedAlbum.addPhotos(photos);
	}

	@Override
	public String commandName() {
		return "RemovePhotosCommand";
	}

}
