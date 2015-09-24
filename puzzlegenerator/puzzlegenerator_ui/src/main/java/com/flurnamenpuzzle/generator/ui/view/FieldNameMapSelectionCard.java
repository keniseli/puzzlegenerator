package com.flurnamenpuzzle.generator.ui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import com.flurnamenpuzzle.generator.Observable;
import com.flurnamenpuzzle.generator.Observer;
import com.flurnamenpuzzle.generator.PuzzleGeneratorConfig;
import com.flurnamenpuzzle.generator.domain.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;

public class FieldNameMapSelectionCard extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	private PuzzleGeneratorController controller;
	private JFileChooser shapeFileChooser;
	private JFileChooser tifFileChooser;

	private JLabel stateDropdownLabel;
	private JLabel chooseFieldNameLabel;
	private JLabel chooseMapLabel;

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
		this.setBackground(PuzzleGeneratorConfig.BACKGROUND_COLOR);

		shapeFileChooser = new JFileChooser();
		FileNameExtensionFilter shapeFileFilter = new FileNameExtensionFilter("Shape-Datei", "shp", "shx", "dbf");
		shapeFileChooser.setFileFilter(shapeFileFilter);
		shapeFileChooser.setAcceptAllFileFilterUsed(false);
		tifFileChooser = new JFileChooser();
		FileNameExtensionFilter tifFileFilter = new FileNameExtensionFilter("TIFF-Datei", "tif", "tiff");
		tifFileChooser.setFileFilter(tifFileFilter);
		tifFileChooser.setAcceptAllFileFilterUsed(false);
		mapPath = new JTextField();
		mapPath.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		fieldNamePath = new JTextField();
		fieldNamePath.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		stateDropdownLabel = new JLabel("Bitte wählen Sie eine Gemeinde aus:");
		stateDropdownLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		chooseFieldNameLabel = new JLabel("Wählen Sie das Shapefile der Flurnamen:");
		chooseFieldNameLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		chooseMapLabel = new JLabel("Wählen Sie das Tiff-File mit dem Kartenmaterial:");
		chooseMapLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		chooseFieldNameButton = new JButton("Durchsuchen");
		chooseFieldNameButton.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		chooseMapButton = new JButton("Durchsuchen");
		chooseMapButton.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
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
				controller.saveFieldNameFilePathAndCardMaterialFilePath(fieldNamePathText, mapPathText, stateName);
			}
		});

		chooseFieldNameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shapeFileChooser.showOpenDialog(null);
				fieldNamePath.setText(shapeFileChooser.getSelectedFile().getPath());
			}
		});

		chooseMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tifFileChooser.showOpenDialog(null);
				mapPath.setText(tifFileChooser.getSelectedFile().getPath());
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
}
