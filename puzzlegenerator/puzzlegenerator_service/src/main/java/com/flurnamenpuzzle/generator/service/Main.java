package com.flurnamenpuzzle.generator.service;

import javax.swing.SwingUtilities;

import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;
import com.flurnamenpuzzle.generator.ui.model.PuzzleGeneratorModel;

/**
 * This class exists only to start the application.
 *
 */
public class Main {

    public static void main(String[] args) {
	PuzzleGeneratorModel puzzleGeneratorModel = new PuzzleGeneratorModel();
	PuzzleGeneratorController puzzleGeneratorController = new PuzzleGeneratorController(puzzleGeneratorModel);
	SwingUtilities.invokeLater(puzzleGeneratorController::initializeView);
    }
    
}
