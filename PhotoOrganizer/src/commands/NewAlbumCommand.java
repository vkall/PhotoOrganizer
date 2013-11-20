package commands;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import logic.Album;

public class NewAlbumCommand implements Command {

	private String newAlbumName;
	private Album parentAlbum;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode parentTreeNode;
	private Album newAlbum;
	private DefaultMutableTreeNode newNode;
	
	public NewAlbumCommand (String newAlbumName, Album parentAlbum, 
			DefaultTreeModel treeModel, DefaultMutableTreeNode parentTreeNode) {
		this.newAlbumName = newAlbumName;
		this.parentAlbum = parentAlbum;
		this.treeModel = treeModel;
		this.parentTreeNode = parentTreeNode;
	}

	@Override
	public void doCommand() {
		newAlbum = new Album(newAlbumName, parentAlbum);
	
		newNode = new DefaultMutableTreeNode();
		newNode.setUserObject(newAlbum);
		treeModel.insertNodeInto(newNode, parentTreeNode, parentTreeNode.getChildCount());

		System.out.println("Created new album " + newAlbum + " as subalbum of " + parentAlbum);
	}

	@Override
	public void undoCommand() {
		newAlbum.deleteAlbum();
		treeModel.removeNodeFromParent(newNode);
	}

	@Override
	public String commandName() {
		return "NewAlbumCommand";
	}

}
