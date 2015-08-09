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

import net.miginfocom.swing.MigLayout;

import com.flurnamenpuzzle.generator.ui.Observable;
import com.flurnamenpuzzle.generator.ui.Observer;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorConfig;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;
import com.flurnamenpuzzle.generator.ui.model.PuzzleGeneratorModel;

public class FieldNameMapSelectionCard extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	private PuzzleGeneratorController controller;
	private JFileChooser fileChooser;

	private JLabel stateDropdownLabel;
	private JLabel chooseFieldNameLabel;
	private JLabel chooseMapLabel;

	private JButton chooseFieldNameButton;
	private JButton chooseMapButton;
	private JButton nextButton;

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
		add(nextButton, "right, span, gaptop 40");
	}

	/**
	 * initialize all components needed for the panel
	 */
	private void initializeComponents() {
		setLayout(new MigLayout());
		setBorder(new EmptyBorder(20, 200, 20, 200));
		setSize(new Dimension(600, 600));

		fileChooser = new JFileChooser();
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
		stateDropdown = new JComboBox<>(selectableStates);
		stateDropdown.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
	}

	/**
	 * add all actionlistener to the buttons
	 */
	private void addEvents() {
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fieldNamePathText = fieldNamePath.getText();
				String mapPathText = mapPath.getText();
				controller.saveFieldNameFilePathAndCardMaterialFilePath(fieldNamePathText, mapPathText);
			}
		});

		chooseFieldNameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(null);
				fieldNamePath.setText(fileChooser.getSelectedFile().getPath());
			}
		});

		chooseMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(null);
				mapPath.setText(fileChooser.getSelectedFile().getPath());
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
