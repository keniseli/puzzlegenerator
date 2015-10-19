package com.flurnamenpuzzle.generator.ui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.flurnamenpuzzle.generator.Observable;
import com.flurnamenpuzzle.generator.Observer;
import com.flurnamenpuzzle.generator.PuzzleGeneratorConfig;
import com.flurnamenpuzzle.generator.domain.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;

public class ConfirmGenerationCard extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	private static final String FILE_PATH_SHORTENING_REGEX = String.format("(.*?\\%s.*?\\%s).*", File.separator,
			File.separator);
	private static final String FILE_NAME_SHORTENING_REGEX = "(.{10}).*";
	private static final String FILE_PATH_SHORTENING_REPLACEMENT = "$1...";
	private static final String FILE_NAME_SHORTENING_REPLACEMENT = "$1..";

	private PuzzleGeneratorController controller;

	private JLabel stateLabel;
	private JLabel stateNameLabel;
	private JLabel stateShapefileLabel;
	private JLabel stateShapefilePathLabel;
	private JLabel fieldnameShapefileLabel;
	private JLabel fieldnameShapefilePathLabel;
	private JLabel cardTiffLabel;
	private JLabel cardTiffPathLabel;

	private JButton generateButton;
	private JButton backButton;
	private String stateName;
	private String stateShapefilePath;
	private String fieldnameShapefilePath;
	private String cardTiffPath;

	/**
	 * Constructs a new instance
	 * 
	 * @param controller
	 *            the instance to control this class' behaviour.
	 */
	public ConfirmGenerationCard(PuzzleGeneratorController controller) {
		this.controller = controller;
		initializeComponents();
		addComponentsToPanel();
		addEvents();
	}

	/**
	 * working with migLayout to set the components in place
	 */
	private void addComponentsToPanel() {
		add(stateLabel, "gaptop 40");
		add(stateNameLabel, "gap :40:, pushx, growx, gapbottom 10, wrap");
		add(stateShapefileLabel);
		add(stateShapefilePathLabel, "gap :40:, pushx, growx, gapbottom 10, wrap");
		add(fieldnameShapefileLabel);
		add(fieldnameShapefilePathLabel, "gap :40:, pushx, growx, gapbottom 10, wrap");
		add(cardTiffLabel);
		add(cardTiffPathLabel, "gap :40:, pushx, growx, gapbottom 10, wrap");
		add(backButton, "left, gaptop 40");
		add(generateButton, "right, gaptop 40");
	}

	/**
	 * initialize all components needed for the panel
	 */
	private void initializeComponents() {
		setLayout(new MigLayout());
		setBorder(new EmptyBorder(20, 200, 20, 200));
		setSize(new Dimension(600, 600));
		setBackground(PuzzleGeneratorConfig.BACKGROUND_COLOR);

		stateLabel = new JLabel("Gemeinde");
		stateLabel.setFont(PuzzleGeneratorConfig.FONT_BOLD);

		stateNameLabel = new JLabel(stateName);
		stateNameLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		stateShapefileLabel = new JLabel("Gemeinde-Shapefile");
		stateShapefileLabel.setFont(PuzzleGeneratorConfig.FONT_BOLD);

		stateShapefilePathLabel = new JLabel(stateShapefilePath);
		stateShapefilePathLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		fieldnameShapefileLabel = new JLabel("Flurnamen-Shapefile");
		fieldnameShapefileLabel.setFont(PuzzleGeneratorConfig.FONT_BOLD);

		fieldnameShapefilePathLabel = new JLabel(fieldnameShapefilePath);
		fieldnameShapefilePathLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		cardTiffLabel = new JLabel("Karten-Tiff");
		cardTiffLabel.setFont(PuzzleGeneratorConfig.FONT_BOLD);

		cardTiffPathLabel = new JLabel(cardTiffPath);
		cardTiffPathLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		backButton = new JButton("Zurück");
		backButton.setFont(PuzzleGeneratorConfig.FONT_BOLD);

		generateButton = new JButton("Generieren");
		generateButton.setFont(PuzzleGeneratorConfig.FONT_BOLD);
	}

	/**
	 * add all actionlistener to the buttons
	 */
	private void addEvents() {
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.confirmGeneration();
			}
		});
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showPreviousCard();
			}
		});
	}

	@Override
	public void update(Observable observable) {
		PuzzleGeneratorModel model = (PuzzleGeneratorModel) observable;
		stateName = model.getStateName();
		stateNameLabel.setText(stateName);
		stateShapefilePath = shortenFilePath(model.getStateFilePath());
		stateShapefilePathLabel.setText(stateShapefilePath);
		fieldnameShapefilePath = shortenFilePath(model.getFieldNameFilePath());
		fieldnameShapefilePathLabel.setText(fieldnameShapefilePath);
		cardTiffPath = shortenFilePath(model.getMapFilePath());
		cardTiffPathLabel.setText(cardTiffPath);
	}

	/**
	 * Shortens the given path if
	 * <ul>
	 * <li>the length of the name is longer than 10 character</li>
	 * <li>the path has 4 or more directories</li>
	 * <li>the name of the path is longer than 40 characters</li>
	 * </ul>
	 * 
	 * @param filePathToShorten
	 *            : path structure: path, filename, name extension
	 * @return shorted pathname
	 */
	public String shortenFilePath(String filePathToShorten) {
		if (filePathToShorten != null) {
			String pathToShorten = FilenameUtils.getFullPath(filePathToShorten);
			String nameToShorten = FilenameUtils.getBaseName(filePathToShorten);
			String nameExtension = FilenameUtils.getExtension(filePathToShorten);

			int amountOfPathSeparators = StringUtils.countMatches(pathToShorten, File.separator);
			if (pathToShorten.length() > 40 || amountOfPathSeparators > 4) {
				pathToShorten = pathToShorten.replaceAll(FILE_PATH_SHORTENING_REGEX, FILE_PATH_SHORTENING_REPLACEMENT);
				pathToShorten = String.format("%s%s", pathToShorten, File.separator);
			}

			if (nameToShorten.length() > 10) {
				nameToShorten = nameToShorten.replaceAll(FILE_NAME_SHORTENING_REGEX, FILE_NAME_SHORTENING_REPLACEMENT);
			}

			filePathToShorten = String.format("%s%s.%s", pathToShorten, nameToShorten, nameExtension);
			return filePathToShorten;
		}
		return "";
	}

}
