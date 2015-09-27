package com.flurnamenpuzzle.generator.ui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import net.miginfocom.swing.MigLayout;

import com.flurnamenpuzzle.generator.Observable;
import com.flurnamenpuzzle.generator.Observer;
import com.flurnamenpuzzle.generator.PuzzleGeneratorConfig;
import com.flurnamenpuzzle.generator.domain.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;

/**
 * Class for the state selection panel
 * 
 */
public class StateSelectionCard extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	private static final String SHAPE_FILE_EXTENSION = "shp";

	private PuzzleGeneratorController controller;
	private JFileChooser fileChooser;

	private JLabel stateLabel;
	private JLabel notificationLabel;

	private JButton chooseButton;
	private JButton nextButton;

	private JTextField pathField;

	/**
	 * Constructs a new instance
	 * 
	 * @param controller
	 *            the instance to control this class' behaviour.
	 */
	public StateSelectionCard(PuzzleGeneratorController controller) {
		this.controller = controller;
		initializeComponents();
		addEvents();
		layoutComponents();
	}

	/**
	 * working with gridBag to set the components in place
	 */
	private void layoutComponents() {
		add(stateLabel, "span, gapbottom 5");
		add(pathField, "height :30:, pushx, growx");
		add(chooseButton, "height :30:, wrap");
		add(notificationLabel, "span, gaptop 20");
		add(nextButton, "right, span, gaptop 40");
	}

	/**
	 * initialize all components needed for the panel
	 */
	private void initializeComponents() {
		setLayout(new MigLayout());
		setBorder(new EmptyBorder(20, 200, 20, 200));
		setSize(new Dimension(600, 600));
		setBackground(PuzzleGeneratorConfig.BACKGROUND_COLOR);

		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter shapeFileFilter = new FileNameExtensionFilter(
				"Shape-Datei", "shp", "shx", "dbf");
		fileChooser.setFileFilter(shapeFileFilter);

		pathField = new JTextField();
		pathField.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		stateLabel = new JLabel("Bitte wählen Sie ein Gemeinde-Shape aus:");
		stateLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		notificationLabel = new JLabel();
		notificationLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		chooseButton = new JButton("Durchsuchen");
		chooseButton.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		nextButton = new JButton("Weiter");
		nextButton.setFont(PuzzleGeneratorConfig.FONT_BOLD);
	}

	/**
	 * add all actionlistener to the buttons
	 */
	private void addEvents() {
		chooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(null);
				// check if user has selected a file
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String pathOfSelectedFile = selectedFile.getPath();
					pathField.setText(pathOfSelectedFile);
					String extension = FilenameUtils
							.getExtension(selectedFile.getName());
					if (extension.equals(SHAPE_FILE_EXTENSION)) {
						notificationLabel
								.setForeground(PuzzleGeneratorConfig.SUCCESS_COLOR);
						controller.setNotification("Shape Datei ausgewählt.");
					} else {
						notificationLabel
								.setForeground(PuzzleGeneratorConfig.FAIL_COLOR);
						notificationLabel
								.setText("Es wurde keine Shape Datei ausgewählt.");
					}
				}
			}
		});

		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = pathField.getText();
				controller.saveStateFilePath(path);
				notificationLabel.setText("");
			}
		});
	}

	@Override
	public void update(Observable observable) {
		PuzzleGeneratorModel model = (PuzzleGeneratorModel) observable;
		String notification = model.getNotification();
		if(notification != null && !notification.isEmpty()){
			notificationLabel.setForeground(PuzzleGeneratorConfig.FAIL_COLOR);
			notificationLabel.setText(notification);
		}
	}
}
