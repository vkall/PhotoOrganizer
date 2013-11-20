package logic;

import java.util.Set;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import commands.Command;

public class PhotoOrganizerModel {

	private Album root = new Album("All photos");
	private FlaggedAlbum flaggedAlbum = new FlaggedAlbum("Flagged", root);
	private RatedAlbum greatAlbum = new RatedAlbum("Great Photos", root);
	private DefaultTreeModel treeModel;
	private SizedStack<Command> undoCommandStack;
	private SizedStack<Command> redoCommandStack;

	public PhotoOrganizerModel() {
		loadPhotos("sample-photos");
		makeTreeModel();
		makeFlaggedAndGreatAlbums();
		this.undoCommandStack = new SizedStack<Command>(10);
		this.redoCommandStack = new SizedStack<Command>(10);
	}

	public PhotoOrganizerModel(String path) {
		loadPhotos(path);
		makeTreeModel();
		makeFlaggedAndGreatAlbums();
		this.undoCommandStack = new SizedStack<Command>(10);
		this.redoCommandStack = new SizedStack<Command>(10);
	}

	/**
	 * Load the photos found in all subfolders of a path on disk. If path is not
	 * an actual folder on disk, has no effect.
	 */
	private void loadPhotos(String path) {
		Set<Photo> photos = PhotoLoader.loadPhotos(path);
		root.addPhotos(photos);
		flaggedAlbum.addPhotos(photos);
		greatAlbum.addPhotos(photos);
	}

	private void makeTreeModel() {

		DefaultMutableTreeNode tree_root = new DefaultMutableTreeNode(
				"All photos");
		tree_root.setUserObject(root);

		this.treeModel = new DefaultTreeModel(tree_root);
	}

	private void makeFlaggedAndGreatAlbums() {

		// add the flagged album and the great photos album to the tree

		DefaultMutableTreeNode flagnode = new DefaultMutableTreeNode();
		DefaultMutableTreeNode greatnode = new DefaultMutableTreeNode();
		DefaultMutableTreeNode rootnode = (DefaultMutableTreeNode) treeModel
				.getRoot();
		flagnode.setUserObject(this.flaggedAlbum);
		greatnode.setUserObject(this.greatAlbum);
		treeModel.insertNodeInto(flagnode, rootnode, rootnode.getChildCount());
		treeModel.insertNodeInto(greatnode, rootnode, rootnode.getChildCount());
	}
	
	public Album getRoot() {
		return this.root;
	}
	
	public FlaggedAlbum getFlaggedAlbum() {
		return this.flaggedAlbum;
	}
	
	public RatedAlbum getRatedAlbum() {
		return this.greatAlbum;
	}
	
	public DefaultTreeModel getTreeModel() {
		return this.treeModel;
	}
	
	public Stack<Command> getUndoStack() {
		return this.undoCommandStack;
	}

	public Stack<Command> getRedoStack() {
		return this.redoCommandStack;
	}

}
