package logic;

import java.util.HashSet;
import java.util.Set;

/**
 * Album is a class that contains a set of photos and keeps track of the parent-
 * and subalbums
 * 
 * @author vkall
 */
public class Album {

	private String name;
	private Set<Photo> photoSet = new HashSet<Photo>();
	private Set<Album> subalbums = new HashSet<Album>();
	private Set<Album> parentalbums = new HashSet<Album>();

	public Album(String albumName) {
		// Creates a root album with no parentalbums

		// precondition:
		assert albumName != null && albumName != "";

		name = albumName;
		
		// postcondition
		assert invariant() && this.name == albumName;
	}

	public Album(String albumName, Album parentalbum) {
		// Creates an album, adds the parentalbums to its parentalbum set
		// and adds it to the subalbum sets of its parentalbums

		// precondition:
		assert albumName != null && albumName != ""
				&& !parentalbum.equals(this);

		name = albumName;
		this.addParentalbum(parentalbum);
		parentalbum.addSubalbum(this);
		Set<Album> parents = parentalbum.getParentalbums();
		for (Album a : parents) {
			this.addParentalbum(a);
			a.addSubalbum(this);
		}
		// postcondition
		assert invariant() && this.name == albumName
				&& this.parentalbums.contains(parentalbum);
	}

	public void deleteAlbum() {
		// Remove this album from the subalbum sets of its parentalbums
		for (Album a : parentalbums)
			a.removeSubalbum(this);

	}

	public void addPhotos(Set<Photo> photos) {
		// Adds the photos to this album and to its parentalbums

		// precondition:
		assert invariant();

		this.addPhotosToSet(photos);
		for (Album a : parentalbums)
			a.addPhotosToSet(photos);

		// postcondition:
		assert invariant() && this.photoSet.containsAll(photos);
	}

	private void addPhotosToSet(Set<Photo> photos) {
		photoSet.addAll(photos);
	}

	public void removePhotos(Set<Photo> photos) {
		// removes the photo from this album and from its subalbums

		// precondition:
		assert invariant();

		this.removePhotosFromSet(photos);
		for (Album a : subalbums)
			a.removePhotosFromSet(photos);

		// postcondition:
		assert invariant() && !this.photoSet.contains(photos);
	}

	private void removePhotosFromSet(Set<Photo> photos) {
		photoSet.removeAll(photos);
	}

	public Set<Photo> getPhotoSet() {
		return photoSet;
	}

	public void addSubalbum(Album subalbum) {
		subalbums.add(subalbum);
	}

	public void removeSubalbum(Album subalbum) {
		subalbums.remove(subalbum);
	}

	public Set<Album> getSubalbums() {
		return subalbums;
	}

	public void addParentalbum(Album parentalbum) {
		parentalbums.add(parentalbum);
	}

	public void removeParentalbum(Album parentalbum) {
		parentalbums.remove(parentalbum);
	}

	public Set<Album> getParentalbums() {
		return parentalbums;
	}

	@Override
	public String toString() {
		return name;
	}

	
	public Iterator createIterator() {
		Set<Album> albums = new HashSet<Album>();
		albums.add(this);
		albums.addAll(subalbums);
		Iterator i = new AlbumIterator(albums.toArray(new Album[albums.size()]));
		return i;
	}

	private boolean invariant() {
		return (name != null && name != "" && !subalbums.contains(this) && !parentalbums
				.contains(this));
	}

}
