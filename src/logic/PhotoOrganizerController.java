package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EmptyStackException;

import javax.swing.WindowConstants;
import javax.swing.tree.DefaultTreeModel;

import slideshow.SlideShowWindow;
import ui.PhotoOrganizerView;
import commands.*;

public class PhotoOrganizerController {
	private PhotoOrganizerView view;
	private PhotoOrganizerModel model;

	public PhotoOrganizerController(PhotoOrganizerView theView,
			PhotoOrganizerModel theModel) {
		this.view = theView;
		this.model = theModel;
		
		this.view.makeCatalogTree(this.model.getTreeModel());

		// Button listeners
		this.view.addNewAlbumListener(new NewAlbumListener());
		this.view.addDeleteAlbumListener(new DeleteAlbumListener());
		this.view.addAddPhotosListener(new AddPhotosListener());
		this.view.addRemovePhotosListener(new RemovePhotosListener());
		this.view.addSlideshowListener(new SlideshowListener());
		this.view.addFlagPhotosListener(new FlagPhotosListener());
		this.view.addRatePhotosListener(new RatePhotosListener());
		this.view.addNewWindowListener(new NewWindowListener());
		this.view.addUndoListener(new UndoListener());
		this.view.addRedoListener(new RedoListener());
		this.view.addExportListener(new ExportListener());

		this.view.getPreviewPane().display(this.model.getRoot().getPhotoSet());

		this.view.enableButtonTimer(new EnableButtonsListener());

	}

	private class EnableButtonsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			boolean newAlbum, deleteAlbum, addPhotos, removePhotos, flagPhotos, ratePhotos, slideshow, newWindow, undo, redo;

			if (view.getSelectedTreeNode() != null) {
				Album selectedAlbum = (Album) view.getSelectedTreeNode()
						.getUserObject();
				if (selectedAlbum.equals(model.getRoot())) {
					// Root album can't be deleted and user can't add photos to
					// it.
					newAlbum = true;
					deleteAlbum = false;
					addPhotos = false;
					removePhotos = true;
					flagPhotos = true;
					ratePhotos = true;
					slideshow = true;
					newWindow = true;
				} else if (selectedAlbum.equals(model.getFlaggedAlbum())
						|| selectedAlbum.equals(model.getRatedAlbum())) {
					// Search-based albums can't be edited.
					newAlbum = false;
					deleteAlbum = false;
					addPhotos = false;
					removePhotos = false;
					flagPhotos = true;
					ratePhotos = true;
					slideshow = true;
					newWindow = true;
				} else {
					// All buttons are enabled when normal albums are selected.
					newAlbum = true;
					deleteAlbum = true;
					addPhotos = true;
					removePhotos = true;
					flagPhotos = true;
					ratePhotos = true;
					slideshow = true;
					newWindow = true;
				}
			} else {
				// No album is selected, user can only flag and rate photos.
				newAlbum = false;
				deleteAlbum = false;
				addPhotos = false;
				removePhotos = false;
				flagPhotos = true;
				ratePhotos = true;
				slideshow = false;
				newWindow = true;
			}
			// Set undo and redo buttons
			undo = !model.getUndoStack().empty();
			redo = !model.getRedoStack().empty();
			
			view.enableButtons(newAlbum, deleteAlbum, addPhotos, removePhotos,
					flagPhotos, ratePhotos, slideshow, newWindow, undo, redo);
		}
	}

	private class NewAlbumListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String newAlbumName = view.promptForAlbumName();
			if (newAlbumName == null)
				return;

			Album parentAlbum = (Album) (view.getSelectedTreeNode().getUserObject());
			DefaultTreeModel treeModel = model.getTreeModel();
			
			NewAlbumCommand c = new NewAlbumCommand(newAlbumName, parentAlbum, treeModel, view.getSelectedTreeNode());
			c.doCommand();
			model.getUndoStack().push(c);
			model.getRedoStack().clear();
			view.getPreviewPane().redraw();
		}

	}

	private class DeleteAlbumListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Album album = (Album) (view.getSelectedTreeNode().getUserObject());
			DefaultTreeModel treeModel = model.getTreeModel();
			
			DeleteAlbumCommand c = new DeleteAlbumCommand(album, treeModel, view.getSelectedTreeNode());
			c.doCommand();
			model.getUndoStack().push(c);
			model.getRedoStack().clear();
			view.getPreviewPane().redraw();
		}

	}

	private class AddPhotosListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Album album = (Album) (view.getSelectedTreeNode().getUserObject());

			AddPhotosCommand c = new AddPhotosCommand(album, view.getPreviewPane().getSelectedPhotos());
			c.doCommand();
			model.getUndoStack().push(c);
			model.getRedoStack().clear();
			view.getPreviewPane().redraw();
		}

	}

	class RemovePhotosListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Album album = (Album) (view.getSelectedTreeNode().getUserObject());

			RemovePhotosCommand c = new RemovePhotosCommand(album, view.getPreviewPane().getSelectedPhotos());
			c.doCommand();
			model.getUndoStack().push(c);
			model.getRedoStack().clear();
			
			view.getPreviewPane().display(album.getPhotoSet());

		}

	}

	private class FlagPhotosListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			FlagPhotosCommand c = new FlagPhotosCommand(view.getPreviewPane().getSelectedPhotos());
			c.doCommand();
			model.getUndoStack().push(c);
			model.getRedoStack().clear();
			
			view.getPreviewPane().redraw();
		}

	}

	private class RatePhotosListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int rating = view.promptForPhotoRating();
				if (rating >= 0 && rating <= 5) {
					RatePhotosCommand c = new RatePhotosCommand(view.getPreviewPane().getSelectedPhotos(), rating);
					c.doCommand();
					model.getUndoStack().push(c);
					model.getRedoStack().clear();
					view.getPreviewPane().redraw();
				} else {
					System.out.println("Rating has to be between 0 and 5");
				}
			} catch (NumberFormatException ex) {
				System.out.println("There was a Number Format Exception");
			}

		}

	}

	private class SlideshowListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Album album = (Album) (view.getSelectedTreeNode().getUserObject());
			SlideShowWindow slideShow;
			slideShow = new SlideShowWindow(album);
			slideShow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			slideShow.setVisible(true);

		}

	}

	private class UndoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Command c = model.getUndoStack().pop();
				c.undoCommand();
				System.out.println("Undo " + c.commandName());
				model.getRedoStack().push(c);
				view.getPreviewPane().redraw();
			} catch (EmptyStackException ex) {
				System.out.println("No commands to undo!");
			}
		}

	}

	private class RedoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Command c = model.getRedoStack().pop();
				c.doCommand();
				System.out.println("Redo " + c.commandName());
				model.getUndoStack().push(c);
				view.getPreviewPane().redraw();
			} catch (EmptyStackException ex) {
				System.out.println("No commands to redo!");
			}
		}

	}

	private class NewWindowListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			PhotoOrganizerView newWindow = new PhotoOrganizerView();
			new PhotoOrganizerController(newWindow, model);

			newWindow.setVisible(true);
			System.out.println("Open new window");
		}

	}

	private final int HTML_EXPORT = 2;
	private final int FOLDER_EXPORT = 1;
	private final int CANCEL_EXPORT = 0;
	
	private class ExportListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Iterator iterator = model.getRoot().createIterator();
			
			int choise = view.exportPrompt();
			
	        switch (choise) {
	            case HTML_EXPORT:
	            	System.out.println("Export as HTML");
	            	String filename = "exported.html";
	            	try {
		            	PrintWriter writer = new PrintWriter(filename, "UTF-8");
		            	writer.println("<html><head><title>PhotoOrganizer</title></head><body>");
		            	while (iterator.hasNext()) {
		    				Album a = (Album)iterator.next();
		    				writer.println("<h2>" + a + "</h2>");
		                	for (Photo p: a.getPhotoSet()){
		                		String path = p.getFile().getAbsolutePath();
		                		String name = p.getFile().getName();
		                		writer.println("<p><img src=\"" + path + "\" alt=\"" + name 
		                				+ "\" width=\"600px\"></p>");
		                	}
		                	writer.println("<br />");
		    			}
		            	writer.println("</body></html>");
		            	System.out.println("Created html at " + System.getProperty("user.dir") 
		            			+ System.getProperty("file.separator") + filename);
		            	writer.close();
	            	} catch (IOException ex) {
	            		System.out.println("Error while creating HTML-file");
	            	}
	            	break;
	            case FOLDER_EXPORT:
	            	System.out.println("Export as folders and files");
	            	try {
		            	String folder = "Exported";
		            	String path;
		            	File root = new File(folder);
	    				if (root.exists()) {
	    					deleteFolder(root);
	    				}
		            	root.mkdir();
		            	while (iterator.hasNext()) {
		    				Album a = (Album)iterator.next();
		    				path = root.getAbsolutePath() + System.getProperty("file.separator") + a;
		    				// Create folder for each album
		    				File dir = new File(path);
		    				dir.mkdir();
		                	for (Photo p: a.getPhotoSet()){
		                		// copy the photos to the folder
		                		File src = p.getFile();
		                		File dst = new File(path + System.getProperty("file.separator") + src.getName());
		                		FileInputStream fileInputStream = new FileInputStream(src);
		                		FileOutputStream fileOutputStream = new FileOutputStream(dst);
		                		int bufferSize;
		                        byte[] bufffer = new byte[512];
		                        while ((bufferSize = fileInputStream.read(bufffer)) > 0) {
		                                fileOutputStream.write(bufffer, 0, bufferSize);
		                        }
		                        fileInputStream.close();
		                        fileOutputStream.close();
		                	}
		            	}
		            	System.out.println("Exported folders and files to " + root.getAbsolutePath());
	            	} catch (IOException ex) {
	            		System.out.println("Error while exporting folders");
	            	}
	            	break;
	            case CANCEL_EXPORT:
	            	System.out.println("Export canceled");
	            	break;
            	default:
	            	System.out.println("Selection error!");
            		break;
	        }
		}
	}
	
    private boolean deleteFolder(File folder) {
        if (folder.isDirectory()) {
            String[] children = folder.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteFolder(new File(folder, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return folder.delete();
    }

}
