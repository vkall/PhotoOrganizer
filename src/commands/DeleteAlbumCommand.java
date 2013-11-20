package commands;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import logic.Album;

public class DeleteAlbumCommand implements Command {

	private Album deletedAlbum;
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode deletedTreeNode;
	private DefaultMutableTreeNode parentTreeNode;
	
	public DeleteAlbumCommand (Album deletedAlbum, DefaultTreeModel treeModel, DefaultMutableTreeNode deletedTreeNode) {
		this.deletedAlbum = deletedAlbum;
		this.treeModel = treeModel;
		this.deletedTreeNode = deletedTreeNode;
		this.parentTreeNode = (DefaultMutableTreeNode)deletedTreeNode.getParent();
	}

	@Override
	public void doCommand() {

		deletedAlbum.deleteAlbum();
		treeModel.removeNodeFromParent(deletedTreeNode);

		System.out.println("Deleted album " + deletedAlbum);
	}

	@Override
	public void undoCommand() {
		Album parent = ((Album) parentTreeNode.getUserObject());
		Album a = new Album(deletedAlbum.toString(), parent);
		a.addPhotos(deletedAlbum.getPhotoSet());
		
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();
		newNode.setUserObject(a);
		treeModel.insertNodeInto(newNode, parentTreeNode, parentTreeNode.getChildCount());
		
	}

	@Override
	public String commandName() {
		return "DeleteAlbumCommand";
	}

}