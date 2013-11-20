package commands;

import java.util.Set;

import logic.Album;
import logic.Photo;

public class AddPhotosCommand implements Command {

	private Album selectedAlbum;
	private Set<Photo> photos;
	
	public AddPhotosCommand (Album selectedAlbum, Set<Photo> photos) {
		this.selectedAlbum = selectedAlbum;
		this.photos = photos;
	}

	@Override
	public void doCommand() {

		selectedAlbum.addPhotos(photos);

		System.out.println("add " + photos.size() + " photos to album " + selectedAlbum);
	}

	@Override
	public void undoCommand() {
		selectedAlbum.removePhotos(photos);
	}

	@Override
	public String commandName() {
		return "AddPhotosCommand";
	}

}