package com.flurnamenpuzzle.generator.ui.view;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.flurnamenpuzzle.generator.ui.Observable;
import com.flurnamenpuzzle.generator.ui.Observer;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;

public class SaveTargetCard extends JPanel implements Observer{
	private static final long serialVersionUID = 1L;
	
	private PuzzleGeneratorController controller;
	private JFileChooser fileChooser;
	
	public SaveTargetCard(PuzzleGeneratorController controller) {
		this.controller = controller;
		initComponents();
		addComponentsToPanel();
		addEvents();
		
	}
	

	private void addEvents() {
		// TODO Auto-generated method stub
		
	}


	private void addComponentsToPanel() {
		// TODO Auto-generated method stub
		
	}


	private void initComponents() {
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		
	}


	@Override
	public void update(Observable observable) {
		// TODO Auto-generated method stub
		
	}

}
