package logic;

import java.util.HashSet;
import java.util.Set;

public class RatedAlbum extends Album {

	public RatedAlbum(String albumName) {
		super(albumName);
	}

	public RatedAlbum(String albumName, Album parentAlbum) {
		super(albumName, parentAlbum);
	}

	@Override
	public Set<Photo> getPhotoSet() {
		Set<Photo> photos = new HashSet<Photo>();
		for (Photo p : super.getPhotoSet()) {
			if (p.getRating() > 3)
				photos.add(p);
		}
		return photos;
	}

}
