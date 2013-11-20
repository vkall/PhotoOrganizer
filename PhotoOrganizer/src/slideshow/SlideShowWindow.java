package slideshow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Album;
import logic.Photo;
import logic.PhotoLoader;

/**
 * SlideShowWIndow is a window to play slideshows
 * 
 * @author iporres
 */
@SuppressWarnings("serial")
public class SlideShowWindow extends JFrame implements WindowListener {

	private Timer timer = new Timer();
	private static final int MSPERTICK = 40; // 25 fps
	private Album root;
	private SlideShowView view;
	private SlideShowController controller;

	/**
	 * Main entry point to run the slide show as a stand alone application
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SlideShowWindow main = new SlideShowWindow();
				main.setVisible(true);
				main.setDefaultCloseOperation(EXIT_ON_CLOSE);
			}
		});
	}

	protected void tick() {
		// This method is invoked by a timer every MSPERTICK miliseconds
		if (controller != null)
			controller.timeTick();
	}

	public SlideShowWindow() {

		root = new Album("All photos");
		loadPhotos("sample-photos");
		init();
	};

	public SlideShowWindow(Album a) {
		root = a;
		init();
	};

	public void init() {
		JPanel contents = new JPanel();
		contents.setVisible(true);
		this.add(contents);
		contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
		// set up the panel on the left with two subpanels in a vertical layout
		JPanel catalogPanel = new JPanel();
		catalogPanel
				.setLayout(new BoxLayout(catalogPanel, BoxLayout.PAGE_AXIS));

		// make the row of buttons
		JPanel buttonPanel = makeButtonPanel();
		catalogPanel.add(buttonPanel);

		// make the slide show viewer
		view = new SlideShowView();
		controller = new SlideShowController(root, view);

		view.setVisible(true);
		catalogPanel.setVisible(true);

		contents.add(view);
		contents.add(catalogPanel);

		// give the whole window a good size
		this.setTitle("Slide Show");
		this.setSize(580, 500);
		this.setResizable(false);

		addWindowListener(this);

		// setup the timer
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				tick();
			}
		}, MSPERTICK, MSPERTICK);

		controller.startPressed();
	}

	/**
	 * Load the photos found in all subfolders of a path on disk. If path is not
	 * an actual folder on disk, has no effect.
	 */
	public void loadPhotos(String path) {
		Set<Photo> photos = PhotoLoader.loadPhotos(path);
		root.addPhotos(photos);
	}

	/**
	 * Make the button panel for manipulating albums and photos.
	 */
	private JPanel makeButtonPanel() {
		JPanel panel = new JPanel();

		// Using a BoxLayout so that buttons will be horizontally aligned
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		JButton newButton = new JButton("Start");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.startPressed();

			}
		});
		panel.add(newButton);

		JButton nextButton = new JButton("Next");
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.nextSlidePressed();

			}
		});
		panel.add(nextButton);

		JButton pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.pauseUnpausePressed();
			}
		});
		panel.add(pauseButton);

		return panel;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		timer.cancel();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// You do not need to do anything here
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// You do not need to do anything here
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// You do not need to do anything here

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// You do not need to do anything here
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// You do not need to do anything here
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// You do not need to do anything here
	}

}
