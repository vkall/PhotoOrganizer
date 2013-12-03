package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import logic.*;

/**
 * PhotoOrganizer is a window that allows arranging photos into hierarchical
 * albums and viewing the photos in each album.
 * 
 * Original @author rcm
 */
@SuppressWarnings("serial")
public class PhotoOrganizerView extends JFrame {

	private JTree albumTree;
	private final PreviewPane previewPane;
	private JPanel catalogPanel;
	private JButton newAlbumButton;
	private JButton deleteAlbumButton;
	private JButton addPhotosButton;
	private JButton removePhotosButton;
	private JButton flagPhotosButton;
	private JButton ratePhotosButton;
	private JButton slideshowButton;
	private JButton newWindowButton;
	private JButton undoButton;
	private JButton redoButton;
	private JButton exportButton;
	private Timer enableButtonsTimer;

	public PhotoOrganizerView() {
		
		// make the row of buttons
		JPanel buttonPanel = makeButtonPanel();
		
		// set up the panel on the left with two subpanels in a vertical layout
		catalogPanel = new JPanel();
		catalogPanel.setLayout(new BoxLayout(catalogPanel, BoxLayout.PAGE_AXIS));
		
		// make the image previewer
		previewPane = new PreviewPane();
		
		// put the catalog tree and image previewer side by side,
		// with an adjustable splitter between
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				catalogPanel, previewPane);
		splitPane.setDividerLocation(256);

		this.add(buttonPanel, BorderLayout.PAGE_START);
		this.add(splitPane, BorderLayout.CENTER);
		
		// give the whole window a good default size
		this.setTitle("Photo Organizer");
		this.setSize(1024, 576);

		// Stop the timer when closing window
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				enableButtonsTimer.stop();
			}
		});

		// Close window when the user presses the window's Close button.
		// Terminate program if all windows are closed.
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}
	
	public PreviewPane getPreviewPane() {
		return this.previewPane;
	}

	/**
	 * Make the button panel for manipulating albums and photos.
	 */
	private JPanel makeButtonPanel() {
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(2, 6));

		newAlbumButton = new JButton("New Album");
		panel.add(newAlbumButton);

		deleteAlbumButton = new JButton("Delete Album");
		panel.add(deleteAlbumButton);

		addPhotosButton = new JButton("Add Photos");
		panel.add(addPhotosButton);

		removePhotosButton = new JButton("Remove Photos");
		panel.add(removePhotosButton);

		slideshowButton = new JButton("Slide Show");
		panel.add(slideshowButton);

		newWindowButton = new JButton("New Window");
		panel.add(newWindowButton);
		
		flagPhotosButton = new JButton("Flag/Unflag");
		panel.add(flagPhotosButton);

		ratePhotosButton = new JButton("Rate Photos");
		panel.add(ratePhotosButton);

		undoButton = new JButton("Undo");
		panel.add(undoButton);

		redoButton = new JButton("Redo");
		panel.add(redoButton);

		exportButton = new JButton("Export");
		panel.add(exportButton);

		return panel;
	}

	public void addNewAlbumListener(ActionListener listener) {
		newAlbumButton.addActionListener(listener);
	}

	public void addDeleteAlbumListener(ActionListener listener) {
		deleteAlbumButton.addActionListener(listener);
	}

	public void addAddPhotosListener(ActionListener listener) {
		addPhotosButton.addActionListener(listener);
	}

	public void addRemovePhotosListener(ActionListener listener) {
		removePhotosButton.addActionListener(listener);
	}

	public void addSlideshowListener(ActionListener listener) {
		slideshowButton.addActionListener(listener);
	}

	public void addFlagPhotosListener(ActionListener listener) {
		flagPhotosButton.addActionListener(listener);
	}

	public void addRatePhotosListener(ActionListener listener) {
		ratePhotosButton.addActionListener(listener);
	}

	public void addNewWindowListener(ActionListener listener) {
		newWindowButton.addActionListener(listener);
	}

	public void addUndoListener(ActionListener listener) {
		undoButton.addActionListener(listener);
	}

	public void addRedoListener(ActionListener listener) {
		redoButton.addActionListener(listener);
	}

	public void addExportListener(ActionListener listener) {
		exportButton.addActionListener(listener);
	}

	/**
	 * Timer that enables and disables buttons
	 */
	public void enableButtonTimer(ActionListener timerTask) {
		enableButtonsTimer = new Timer(200, timerTask);
		enableButtonsTimer.start();
	}

	public void enableButtons(boolean newAlbum, boolean deleteAlbum,
			boolean addPhotos, boolean removePhotos, boolean flagPhotos,
			boolean ratePhotos, boolean slideshow, boolean newWindow,
			boolean undo, boolean redo) {
		newAlbumButton.setEnabled(newAlbum);
		deleteAlbumButton.setEnabled(deleteAlbum);
		addPhotosButton.setEnabled(addPhotos);
		removePhotosButton.setEnabled(removePhotos);
		flagPhotosButton.setEnabled(flagPhotos);
		ratePhotosButton.setEnabled(ratePhotos);
		slideshowButton.setEnabled(slideshow);
		newWindowButton.setEnabled(newWindow);
		undoButton.setEnabled(undo);
		redoButton.setEnabled(redo);
	}

	/**
	 * Make the tree showing album names.
	 */
	public void makeCatalogTree(DefaultTreeModel model) {

		final JTree tree = new JTree(model);
		tree.setMinimumSize(new Dimension(200, 400));

		tree.setToggleClickCount(3); // so that we can use double-clicks for
										// previewing instead of
										// expanding/collapsing

		DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
		selectionModel
				.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setSelectionModel(selectionModel);

		tree.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// if left-double-click @@@changed =2 to ==1
				if (e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() == 2) {
					Album album = (Album) (getSelectedTreeNode()
							.getUserObject());
					previewPane.display(album.getPhotoSet());
					System.out.println("show the photos for album "
							+ getSelectedTreeNode());

				}
			}
		});

		albumTree = tree;
		catalogPanel.add(new JScrollPane(albumTree));
	}
	
	public JTree getAlbumTree() {
		return this.albumTree;
	}

	/**
	 * Return the album currently selected in the album tree. Returns null if no
	 * selection.
	 */
	public DefaultMutableTreeNode getSelectedTreeNode() {
		return (DefaultMutableTreeNode) albumTree
				.getLastSelectedPathComponent();
	}

	/**
	 * Pop up a dialog box prompting the user for a name for a new album.
	 * Returns the name, or null if the user pressed Cancel.
	 */
	public String promptForAlbumName() {
		return (String) JOptionPane.showInputDialog(albumTree, "Album Name: ",
				"Add Album", JOptionPane.PLAIN_MESSAGE, null, null, "");
	}

	public int promptForPhotoRating() {
		int rating = Integer.parseInt((String) JOptionPane.showInputDialog(
				albumTree, "Rating: ", "Rate Photos",
				JOptionPane.PLAIN_MESSAGE, null, null, ""));
		return rating;
	}

	public int exportPrompt() {
		String[] options = new String[] {
				"Cancel",
				"Folder structure", 
				"HTML"
				};
		return JOptionPane.showOptionDialog(this, "Export as: ", "Export", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, 
				null, options, options[0]);
	}
}
