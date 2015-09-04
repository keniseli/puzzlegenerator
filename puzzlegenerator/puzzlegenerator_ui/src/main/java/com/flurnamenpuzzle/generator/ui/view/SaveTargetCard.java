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
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import net.miginfocom.swing.MigLayout;

import com.flurnamenpuzzle.generator.ui.Observable;
import com.flurnamenpuzzle.generator.ui.Observer;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorConfig;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;

public class SaveTargetCard extends JPanel implements Observer{
	private static final long serialVersionUID = 1L;
	
	private PuzzleGeneratorController controller;
	private JFileChooser fileChooser;
	private JButton chooseButton;
	private JLabel savePathLabel;
	private JButton nextButton;
	private JTextField pathField;
	
	public SaveTargetCard(PuzzleGeneratorController controller) {
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
					File selectedFodler = fileChooser.getCurrentDirectory();
					String pathOfSelectedFolder = selectedFodler.getPath();
					pathField.setText(pathOfSelectedFolder);
				}
			}
		});
		
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = pathField.getText();
				//TODO: controller save path for folder func saveTargetFolderPath
			}
		});
	}

	private void layoutComponents() {
		add(this.savePathLabel, "span, gapbottom 5");
		add(this.pathField, "height :30:, pushx, growx");
		add(this.chooseButton, "height :30:, wrap");
		add(this.nextButton, "right, span, gaptop 40");
		
	}

	private void initializeComponents() {
		this.setLayout(new MigLayout());
		this.setBorder(new EmptyBorder(20, 200, 20, 200));
		this.setSize(new Dimension(600, 600));
		
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.chooseButton = new JButton("Durchsuchen");
		this.chooseButton.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		this.savePathLabel = new JLabel("Bitte wählen Sie den Speicherort für das Puzzle aus:");
		this.savePathLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		this.nextButton = new JButton("Weiter");
		this.nextButton.setFont(PuzzleGeneratorConfig.FONT_BOLD);
		this.pathField = new JTextField();
		this.pathField.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
	}


	@Override
	public void update(Observable observable) {
		// TODO Auto-generated method stub
		
	}

}
