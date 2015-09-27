package com.flurnamenpuzzle.generator.ui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class FieldNameMapSelectionCard extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	private static final String SHAPE_FILE_EXTENSION = "shp";
	private static final String TIFF_FILE_EXTENSION = "tif";

	private PuzzleGeneratorController controller;
	private JFileChooser shapeFileChooser;
	private JFileChooser tifFileChooser;

	private JLabel stateDropdownLabel;
	private JLabel chooseFieldNameLabel;
	private JLabel chooseMapLabel;
	private JLabel notificationLabel;

	private JButton chooseFieldNameButton;
	private JButton chooseMapButton;
	private JButton nextButton;
	private JButton backButton;

	private JTextField fieldNamePath;
	private JTextField mapPath;

	private JComboBox<String> stateDropdown;
	private String[] selectableStates = {};

	/**
	 * constructor
	 * 
	 * @param controller
	 */
	public FieldNameMapSelectionCard(PuzzleGeneratorController controller) {
		this.controller = controller;
		initializeComponents();
		addComponentsToPanel();
		addEvents();
	}

	/**
	 * working with migLayout to set the components in place
	 */
	private void addComponentsToPanel() {
		add(stateDropdownLabel, "span");
		add(stateDropdown, "height :30:, pushx, growx, span, gapbottom 10");
		add(chooseFieldNameLabel, "span");
		add(fieldNamePath, "height :30:, pushx, growx");
		add(chooseFieldNameButton, "height :30:, span, wrap 10");
		add(chooseMapLabel, "span");
		add(mapPath, "height :30:, pushx, growx");
		add(chooseMapButton, "height :30:, wrap");
		add(notificationLabel, "span, gaptop 20");
		add(backButton, "left, gaptop 40");
		add(nextButton, "right, gaptop 40");
	}

	/**
	 * initialize all components needed for the panel
	 */
	private void initializeComponents() {
		setLayout(new MigLayout());
		setBorder(new EmptyBorder(20, 200, 20, 200));
		setSize(new Dimension(600, 600));
		setBackground(PuzzleGeneratorConfig.BACKGROUND_COLOR);

		shapeFileChooser = new JFileChooser();
		FileNameExtensionFilter shapeFileFilter = new FileNameExtensionFilter(
				"Shape-Datei", "shp", "shx", "dbf");
		shapeFileChooser.setFileFilter(shapeFileFilter);
		shapeFileChooser.setAcceptAllFileFilterUsed(false);

		tifFileChooser = new JFileChooser();
		FileNameExtensionFilter tifFileFilter = new FileNameExtensionFilter(
				"TIFF-Datei", "tif", "tiff");
		tifFileChooser.setFileFilter(tifFileFilter);
		tifFileChooser.setAcceptAllFileFilterUsed(false);

		mapPath = new JTextField();
		mapPath.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		fieldNamePath = new JTextField();
		fieldNamePath.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		stateDropdownLabel = new JLabel("Bitte wählen Sie eine Gemeinde aus:");
		stateDropdownLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		chooseFieldNameLabel = new JLabel(
				"Wählen Sie das Shapefile der Flurnamen:");
		chooseFieldNameLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		chooseMapLabel = new JLabel(
				"Wählen Sie das Tiff-File mit dem Kartenmaterial:");
		chooseMapLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		chooseFieldNameButton = new JButton("Durchsuchen");
		chooseFieldNameButton.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		chooseMapButton = new JButton("Durchsuchen");
		chooseMapButton.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		notificationLabel = new JLabel();
		notificationLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		nextButton = new JButton("Weiter");
		nextButton.setFont(PuzzleGeneratorConfig.FONT_BOLD);

		backButton = new JButton("Zurück");
		backButton.setFont(PuzzleGeneratorConfig.FONT_BOLD);

		stateDropdown = new JComboBox<>(selectableStates);
		stateDropdown.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
	}

	/**
	 * add all action listener to the buttons
	 */
	private void addEvents() {
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fieldNamePathText = fieldNamePath.getText();
				String mapPathText = mapPath.getText();
				String stateName = stateDropdown.getSelectedItem().toString();
				controller.saveFieldNameFilePathAndCardMaterialFilePath(
						fieldNamePathText, mapPathText, stateName);
				notificationLabel.setText("");
			}
		});

		chooseFieldNameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = shapeFileChooser.showOpenDialog(null);
				// check if user has selected a file
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String success = "Shape Datei wurde ausgewählt.";
					String fail = "Es wurde keine Shape-Datei ausgewählt.";
					handleSelectedFile(shapeFileChooser, fieldNamePath,
							success, fail, SHAPE_FILE_EXTENSION);
				}
			}
		});

		chooseMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = tifFileChooser.showOpenDialog(null);
				// check if user has selected a file
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String success = "Tiff-Datei wurde ausgewählt.";
					String fail = "Es wurde keine Tiff-Datei ausgewählt.";
					handleSelectedFile(tifFileChooser, mapPath, success, fail,
							TIFF_FILE_EXTENSION);
				}
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
		selectableStates = model.getStates();
		stateDropdown.removeAllItems();
		if (selectableStates != null) {
			for (String selectableState : selectableStates) {
				stateDropdown.addItem(selectableState);
			}
		}
	}

	private void handleSelectedFile(JFileChooser fc, JTextField label,
			String success, String fail, String ext) {
		File selectedFile = fc.getSelectedFile();
		String pathOfSelectedFile = selectedFile.getPath();
		label.setText(pathOfSelectedFile);
		String extension = FilenameUtils
				.getExtension(selectedFile.getName());;
		if (extension.equals(ext)) {
			notificationLabel
					.setForeground(PuzzleGeneratorConfig.SUCCESS_COLOR);
			notificationLabel.setText(success);
		} else {
			notificationLabel.setForeground(PuzzleGeneratorConfig.FAIL_COLOR);
			notificationLabel.setText(fail);
		}
	}
}
