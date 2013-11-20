package main;

import javax.swing.SwingUtilities;

import ui.PhotoOrganizerView;
import logic.PhotoOrganizerController;
import logic.PhotoOrganizerModel;

public class PhotoOrganizer {

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				PhotoOrganizerModel model = null;

				if (args.length == 0) {
					model = new PhotoOrganizerModel();
				} else if (args.length == 1) {
					model = new PhotoOrganizerModel(args[0]);
				} else {
					System.err.println("too many command-line arguments");
					System.exit(0);
				}

				PhotoOrganizerView view = new PhotoOrganizerView();
				new PhotoOrganizerController(view, model);
				view.setVisible(true);
			}
		});
	}
}
