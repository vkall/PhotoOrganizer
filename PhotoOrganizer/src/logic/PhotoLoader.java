package logic;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

/**
 * PhotoLoader finds photo files on disk.
 * 
 * @author rcm
 */
public class PhotoLoader {
	/**
	 * Returns a set of photos representing all .JPG files under a given folder
	 * (including subfolders as well).
	 * 
	 * @param path
	 *            the string representing the path to the folder. If path does
	 *            not correspond to an actual folder in the filesystem, returns
	 *            an empty set.
	 */
	public static Set<Photo> loadPhotos(String path) {
		Set<Photo> set = new HashSet<Photo>();

		if (path == null)
			return set;

		File f = new File(path);
		if (!f.isDirectory())
			return set;

		addPhotosToSet(f, set);
		return set;
	}

	// Find all JPG files in folder and its subfolders and add them to set.
	// Requires folder to be an actual folder on disk and set != null.
	private static void addPhotosToSet(File folder, Set<Photo> set) {
		for (File f : findJPGFiles(folder)) {
			System.out.println("Loading... " + f.getAbsolutePath());
			set.add(new Photo(f));
		}
		for (File g : findSubFolders(folder)) {
			addPhotosToSet(g, set);
		}
	}

	// Return all the JPG files that are immediate children of folder.
	// Requires folder to be an actual folder on disk.
	private static File[] findJPGFiles(File folder) {
		return folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				int i = name.lastIndexOf('.');
				return "jpg".equals(name.substring(i + 1).toLowerCase());
			}
		});
	}

	// Return all the immediate subfolders of folder.
	// Requires folder to be an actual folder on disk.
	private static File[] findSubFolders(File folder) {
		return folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return (pathname.isDirectory());
			}
		});
	}

}
