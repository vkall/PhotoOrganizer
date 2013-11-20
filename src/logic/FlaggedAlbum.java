package logic;

import java.util.HashSet;
import java.util.Set;

public class FlaggedAlbum extends Album {

	public FlaggedAlbum(String albumName) {
		super(albumName);
	}

	public FlaggedAlbum(String albumName, Album parentAlbum) {
		super(albumName, parentAlbum);
	}

	@Override
	public Set<Photo> getPhotoSet() {
		Set<Photo> photos = new HashSet<Photo>();
		for (Photo p : super.getPhotoSet()) {
			if (p.hasFlag())
				photos.add(p);
		}
		return photos;
	}

}
