package logic;

public class AlbumIterator implements Iterator {
	
	private Album[] albums;
	private int position;
	
	public AlbumIterator(Album[] albums) {
		position = 0;
		this.albums = albums;
	}

	@Override
	public boolean hasNext() {
		if (position >= albums.length || albums[position] == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Object next() {
		Album album = albums[position];
		position++;
		return album;
	}

}
