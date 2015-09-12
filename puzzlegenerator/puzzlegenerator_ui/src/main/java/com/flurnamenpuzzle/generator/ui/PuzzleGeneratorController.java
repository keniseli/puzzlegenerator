package com.flurnamenpuzzle.generator.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.flurnamenpuzzle.generator.Observer;
import com.flurnamenpuzzle.generator.Steps;
import com.flurnamenpuzzle.generator.domain.Puzzle;
import com.flurnamenpuzzle.generator.domain.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.service.PuzzleGeneratorService;
import com.flurnamenpuzzle.generator.service.ShapeService;
import com.flurnamenpuzzle.generator.ui.view.ConfirmCardGeneration;
import com.flurnamenpuzzle.generator.ui.view.FieldNameMapSelectionCard;
import com.flurnamenpuzzle.generator.ui.view.ProgressCard;
import com.flurnamenpuzzle.generator.ui.view.PuzzleGeneratorView;
import com.flurnamenpuzzle.generator.ui.view.StateSelectionCard;
import com.flurnamenpuzzle.generator.ui.view.TargetPathSelectionCard;

/**
 * The {@link PuzzleGeneratorController} is the controller (MVC Design Pattern)
 * of the ui. Unlike the view, it contains business logic (in order to validate
 * things etc...) and interprets changes on the view and passes them accordingly
 * to the model (which will then update the relevant components (Observer Design
 * Pattern).
 *
 */
public class PuzzleGeneratorController {
	protected static final String TEMPORARY_DIRECTORY_NAME = "temporaryPuzzleGeneratorDirectory";
	private PuzzleGeneratorView puzzleGeneratorView;
	private PuzzleGeneratorModel puzzleGeneratorModel;
	private ShapeService shapeService;
	private PuzzleGeneratorService puzzleGeneratorService;

	public PuzzleGeneratorController(PuzzleGeneratorModel puzzleGeneratorModel) {
		puzzleGeneratorView = new PuzzleGeneratorView();
		this.puzzleGeneratorModel = puzzleGeneratorModel;
		puzzleGeneratorModel.setCurrentStep(Steps.STEP_3);
		shapeService = new ShapeService();
		puzzleGeneratorService = new PuzzleGeneratorService(puzzleGeneratorModel);
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
		String idOfStep5 = Steps.STEP_5.getId();
		TargetPathSelectionCard targetSelectionCard = new TargetPathSelectionCard(this);
		cardMap.put(idOfStep5, targetSelectionCard);
		puzzleGeneratorModel.addObserver(targetSelectionCard);
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

	public void saveFieldNameFilePathAndCardMaterialFilePath(String fieldNameFilePath, String mapFilePath,
			String stateName) {
		puzzleGeneratorModel.setFieldNameFilePath(fieldNameFilePath);
		puzzleGeneratorModel.setMapFilePath(mapFilePath);
		puzzleGeneratorModel.setStateName(stateName);
		puzzleGeneratorModel.setCurrentStep(Steps.STEP_3);
	}

	public void confirmGeneration() {
		puzzleGeneratorModel.setAbortGeneration(false);
		File temporaryDirectory = createTemporaryDirectory();
		if (temporaryDirectory != null) {
			puzzleGeneratorModel.setTemporaryDirectory(temporaryDirectory);
			Thread puzzleGenerationThread = initializePuzzleGenerationThread();
			puzzleGeneratorModel.setCurrentStep(Steps.STEP_4);
			puzzleGenerationThread.start();
		}
	}

	public void abortGenerationProcess() {
		puzzleGeneratorModel.setAbortGeneration(true);
		puzzleGeneratorModel.setCurrentStep(Steps.STEP_3);
	}

	/**
	 * This method will be called from the puzzle generation thread when the
	 * generation is complete.
	 */
	public void generationComplete() {
		Puzzle puzzle = puzzleGeneratorModel.getPuzzle();
		if (puzzle != null) {
			puzzleGeneratorModel.setCurrentStep(Steps.STEP_5);
		} else {
			puzzleGeneratorModel.setCurrentStep(Steps.STEP_3);
		}
	}

	public void setTargetAndSavePuzzle(String targetPath) {
		puzzleGeneratorModel.setTargetFolderPath(targetPath);
		File targetDirectory = new File(targetPath);
		File temporaryDirectory = puzzleGeneratorModel.getTemporaryDirectory();
		for (File puzzleFile : temporaryDirectory.listFiles()) {
			String simpleFileName = puzzleFile.getName();
			String newPathToFile = String.format("%s%s%s", targetDirectory, File.separatorChar, simpleFileName);
			File newFile = new File(newPathToFile);
			puzzleFile.renameTo(newFile);
		}
	}

	private File createTemporaryDirectory() {
		File temporaryDirectory = null;
		try {
			FileAttribute<?>[] fileAttributes = {};
			temporaryDirectory = Files.createTempDirectory(TEMPORARY_DIRECTORY_NAME, fileAttributes).toFile();
		} catch (IOException e) {
			puzzleGeneratorModel
					.setNotification("Ein temporäres Verzeichnis zum Zwischenspeichern konnte nicht erstellt werden. Es kann nicht fortgefahren werden.");
		}
		return temporaryDirectory;
	}

	private Thread initializePuzzleGenerationThread() {
		Thread puzzleGenerationThread = new Thread(new Runnable() {

			@Override
			public void run() {
				String stateFilePath = puzzleGeneratorModel.getStateFilePath();
				String stateName = puzzleGeneratorModel.getStateName();
				String fieldNameFilePath = puzzleGeneratorModel.getFieldNameFilePath();
				String mapFilePath = puzzleGeneratorModel.getMapFilePath();
				String pathToTemporaryDirectory = puzzleGeneratorModel.getTemporaryDirectory().getAbsolutePath();
				Puzzle puzzle = puzzleGeneratorService.generatePuzzle(stateFilePath, stateName, fieldNameFilePath,
						mapFilePath, pathToTemporaryDirectory);
				puzzleGeneratorModel.setPuzzle(puzzle);
				generationComplete();
			}
		});
		return puzzleGenerationThread;
	}

}
