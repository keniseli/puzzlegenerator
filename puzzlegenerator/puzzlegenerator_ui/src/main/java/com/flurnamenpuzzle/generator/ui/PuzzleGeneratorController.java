package com.flurnamenpuzzle.generator.ui;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.flurnamenpuzzle.generator.service.ShapeService;
import com.flurnamenpuzzle.generator.ui.model.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.ui.view.ConfirmCardGeneration;
import com.flurnamenpuzzle.generator.ui.view.FieldNameMapSelectionCard;
import com.flurnamenpuzzle.generator.ui.view.ProgressCard;
import com.flurnamenpuzzle.generator.ui.view.PuzzleGeneratorView;
import com.flurnamenpuzzle.generator.ui.view.StateSelectionCard;

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
	private ShapeService shapeService;

	public PuzzleGeneratorController(PuzzleGeneratorModel puzzleGeneratorModel) {
		puzzleGeneratorView = new PuzzleGeneratorView();
		this.puzzleGeneratorModel = puzzleGeneratorModel;
		puzzleGeneratorModel.setCurrentStep(Steps.STEP_1);
		shapeService = new ShapeService();
	}

	/**
	 * The objects of the view are initialized and shown. {@link Observer
	 * Observers} are added if necessary.
	 */
	public void initializeView() {
		Map<String, JPanel> cardMap = new HashMap<>();
		String idOfStep1 = Steps.STEP_1.getId();
		StateSelectionCard stateSelectionCard = new StateSelectionCard(this);
		cardMap.put(idOfStep1, stateSelectionCard);
		puzzleGeneratorModel.addObserver(stateSelectionCard);
		String idOfStep2 = Steps.STEP_2.getId();
		FieldNameMapSelectionCard fieldNameMapSelectionCard = new FieldNameMapSelectionCard(this);
		cardMap.put(idOfStep2, fieldNameMapSelectionCard);
		puzzleGeneratorModel.addObserver(fieldNameMapSelectionCard);
		String idOfStep3 = Steps.STEP_3.getId();
		ConfirmCardGeneration confirmCardGeneration = new ConfirmCardGeneration(this);
		cardMap.put(idOfStep3, confirmCardGeneration);
		puzzleGeneratorModel.addObserver(confirmCardGeneration);
		String idOfStep4 = Steps.STEP_4.getId();
		ProgressCard progressCard = new ProgressCard(this);
		cardMap.put(idOfStep4, progressCard);
		puzzleGeneratorModel.addObserver(progressCard);
		puzzleGeneratorModel.addObserver(puzzleGeneratorView);
		puzzleGeneratorView.createAndShow(this, cardMap);
		puzzleGeneratorModel.notifyObservers();
	}

	public void saveStateFilePath(String stateFilePath) {
		puzzleGeneratorModel.setStateFilePath(stateFilePath);
		File stateFile = new File(stateFilePath);
		List<String> namesOfShapesInFile = shapeService.getNamesOfShapeFile(stateFile);
		int numberOfStates = namesOfShapesInFile.size();
		String[] states = new String[numberOfStates];
		states = namesOfShapesInFile.toArray(states);
		puzzleGeneratorModel.setStates(states);
		puzzleGeneratorModel.setCurrentStep(Steps.STEP_2);
	}

	public void saveFieldNameFilePathAndCardMaterialFilePath(String fieldNameFilePath, String mapFilePath) {
		puzzleGeneratorModel.setFieldNameFilePath(fieldNameFilePath);
		puzzleGeneratorModel.setMapFilePath(mapFilePath);
		puzzleGeneratorModel.setCurrentStep(Steps.STEP_3);
	}
	
	public void confirmCardGeneration() {
	}

}