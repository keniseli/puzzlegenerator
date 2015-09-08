package com.flurnamenpuzzle.generator;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;
import com.flurnamenpuzzle.generator.ui.model.PuzzleGeneratorModel;

/**
 * This class exists only to start the application.
 *
 */
public class Main {

	public static void main(String[] args) {
		setSystemLookAndFeel();
		PuzzleGeneratorModel puzzleGeneratorModel = new PuzzleGeneratorModel();
		PuzzleGeneratorController puzzleGeneratorController = new PuzzleGeneratorController(
				puzzleGeneratorModel);
		SwingUtilities.invokeLater(puzzleGeneratorController::initializeView);
	}

	
	public static void setSystemLookAndFeel(){
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
	}
}
