package logic;

import java.io.File;

/**
 * Photo is an immutable class representing a digital photo file on disk.
 * 
 * @author jbaek, rcm
 */
public class Photo {

	private final File file;
	private boolean flag;
	private int rating;

	/**
	 * Make a Photo for a file. Requires file != null.
	 */
	public Photo(File file) {
		this.file = file;
		flag = false;
		rating = 0;
	}

	/**
	 * @return the file containing this photo.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Flaggs / unflaggs the photo.
	 */
	public void flag() {
		if (flag)
			flag = false;
		else
			flag = true;
	}

	/**
	 * @return the flag variable.
	 */
	public boolean hasFlag() {
		return flag;
	}

	/**
	 * Set the rating of this photo to r if r is an int between 0 and 5.
	 */
	public void setRating(int r) {
		if (r >= 0 && r <= 5)
			rating = r;
	}

	/**
	 * @return the rating of this photo.
	 */
	public int getRating() {
		return rating;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Photo && ((Photo) obj).file.equals(file);
	}

	@Override
	public int hashCode() {
		return file.hashCode();
	}
}
