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

import net.miginfocom.swing.MigLayout;

import com.flurnamenpuzzle.generator.Observable;
import com.flurnamenpuzzle.generator.Observer;
import com.flurnamenpuzzle.generator.PuzzleGeneratorConfig;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;

public class TargetPathSelectionCard extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	private PuzzleGeneratorController controller;
	private JFileChooser fileChooser;
	private JButton chooseButton;
	private JLabel savePathLabel;
	private JButton nextButton;
	private JTextField pathField;

	public TargetPathSelectionCard(PuzzleGeneratorController controller) {
		this.controller = controller;
		initializeComponents();
		addEvents();
		layoutComponents();
	}

	private void addEvents() {
		chooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(null);
				// check if user has selected a file
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedFolder = fileChooser.getSelectedFile();
					String pathOfSelectedFolder = selectedFolder.getPath();
					pathField.setText(pathOfSelectedFolder);
				}
			}
		});

		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = pathField.getText();
				controller.setTargetAndSavePuzzle(path);
			}
		});
	}

	private void layoutComponents() {
		add(savePathLabel, "span, gapbottom 5");
		add(pathField, "height :30:, pushx, growx");
		add(chooseButton, "height :30:, wrap");
		add(nextButton, "right, span, gaptop 40");
	}

	private void initializeComponents() {
		setLayout(new MigLayout());
		setBorder(new EmptyBorder(20, 200, 20, 200));
		setSize(new Dimension(600, 600));
		setBackground(PuzzleGeneratorConfig.BACKGROUND_COLOR);

		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooseButton = new JButton("Durchsuchen");
		chooseButton.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		savePathLabel = new JLabel(
				"Bitte wählen Sie den Speicherort für das Puzzle aus:");
		savePathLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		nextButton = new JButton("Weiter");
		nextButton.setFont(PuzzleGeneratorConfig.FONT_BOLD);
		pathField = new JTextField();
		pathField.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
	}

	@Override
	public void update(Observable observable) {
	}

}
