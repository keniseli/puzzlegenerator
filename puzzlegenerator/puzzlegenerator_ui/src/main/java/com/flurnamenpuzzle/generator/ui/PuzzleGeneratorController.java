package com.flurnamenpuzzle.generator.ui;

import com.flurnamenpuzzle.generator.ui.model.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.ui.view.PuzzleGeneratorView;

/**
 * The {@link PuzzleGeneratorController} is the controller (MVC Design Pattern)
 * of the ui. Unlike the view, it contains business logic (in order to validate
 * things etc...) and interprets changes on the view and passes them accordingly
 * to the model (which will then update the relevant components (Observer Design
 * Pattern).
 *
 */
public class PuzzleGeneratorController {
    private PuzzleGeneratorView puzzleGeneratorView;
    private PuzzleGeneratorModel puzzleGeneratorModel;

    public PuzzleGeneratorController(PuzzleGeneratorModel puzzleGeneratorModel) {
	this.puzzleGeneratorView = new PuzzleGeneratorView();
	this.puzzleGeneratorModel = puzzleGeneratorModel;
    }

    /**
     * The objects of the view are initialized and shown. {@link Observer
     * Observers} are added if necessary.
     */
    public void initializeView() {
	puzzleGeneratorView.createAndShow();
	puzzleGeneratorModel.addObserver(puzzleGeneratorView);
    }

}
