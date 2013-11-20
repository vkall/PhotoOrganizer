package commands;

import java.util.Set;

import logic.Photo;

public class FlagPhotosCommand implements Command {

	private Set<Photo> photos;
	
	public FlagPhotosCommand (Set<Photo> photos) {
		this.photos = photos;
	}

	@Override
	public void doCommand() {
		for (Photo p : photos) {
			p.flag();
		}
		System.out.println("flagged/unflagged " + photos.size() + " photos");
	}

	@Override
	public void undoCommand() {
		for (Photo p : photos) {
			p.flag();
		}
	}

	@Override
	public String commandName() {
		return "FlagPhotosCommand";
	}

}
