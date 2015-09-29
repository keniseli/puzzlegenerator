package com.flurnamenpuzzle.generator.ui;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import javax.swing.JPanel;

import com.flurnamenpuzzle.generator.PuzzleGeneratorConfig;
import com.flurnamenpuzzle.generator.Steps;
import com.flurnamenpuzzle.generator.domain.Puzzle;
import com.flurnamenpuzzle.generator.domain.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.service.PuzzleGeneratorService;
import com.flurnamenpuzzle.generator.service.ServiceException;
import com.flurnamenpuzzle.generator.service.ShapeService;
import com.flurnamenpuzzle.generator.ui.view.ConfirmGenerationCard;
import com.flurnamenpuzzle.generator.ui.view.FieldNameMapSelectionCard;
import com.flurnamenpuzzle.generator.ui.view.ProgressCard;
import com.flurnamenpuzzle.generator.ui.view.PuzzleGeneratorView;
import com.flurnamenpuzzle.generator.ui.view.ResultCard;
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
		puzzleGeneratorModel.setCurrentStep(Steps.STEP_1);
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
		ConfirmGenerationCard confirmCardGeneration = new ConfirmGenerationCard(this);
		cardMap.put(idOfStep3, confirmCardGeneration);
		puzzleGeneratorModel.addObserver(confirmCardGeneration);
		String idOfStep4 = Steps.STEP_4.getId();
		ProgressCard progressCard = new ProgressCard(this);
		cardMap.put(idOfStep4, progressCard);
		String idOfStep5 = Steps.STEP_5.getId();
		TargetPathSelectionCard targetSelectionCard = new TargetPathSelectionCard(this);
		cardMap.put(idOfStep5, targetSelectionCard);
		String idOfStep6 = Steps.STEP_6.getId();
		ResultCard resultCard = new ResultCard(this);
		cardMap.put(idOfStep6, resultCard);
		puzzleGeneratorModel.addObserver(targetSelectionCard);
		puzzleGeneratorModel.addObserver(progressCard);
		puzzleGeneratorModel.addObserver(resultCard);
		puzzleGeneratorModel.addObserver(puzzleGeneratorView);
		puzzleGeneratorView.createAndShow(this, cardMap);
		puzzleGeneratorModel.notifyObservers();
	}

	public void saveStateFilePath(String stateFilePath) {
		File stateFile = null;
		List<String> namesOfShapesInFile;
		try {
			stateFile = shapeService.getMainShapeFile(stateFilePath);
		} catch (ServiceException e) {
			puzzleGeneratorModel.setNotification(e.getMessage(), PuzzleGeneratorConfig.FAIL_COLOR);
			return;
		}
		puzzleGeneratorModel.setStateFilePath(stateFile.getAbsolutePath());
		namesOfShapesInFile = shapeService.getNamesOfShapeFile(stateFile);

		int numberOfStates = namesOfShapesInFile.size();
		String[] states = new String[numberOfStates];
		states = namesOfShapesInFile.toArray(states);
		puzzleGeneratorModel.setStates(states);
		proceed(Steps.STEP_2);
	}

	public void saveFieldNameFilePathAndCardMaterialFilePath(String fieldNameFilePath, String mapFilePath,
			String stateName) {
		try {
			File fieldNameFile = shapeService.getMainShapeFile(fieldNameFilePath);
			puzzleGeneratorModel.setFieldNameFilePath(fieldNameFile.getAbsolutePath());
		} catch (ServiceException e) {
			puzzleGeneratorModel.setNotification(e.getMessage(), PuzzleGeneratorConfig.FAIL_COLOR);
			return;
		}

		puzzleGeneratorModel.setMapFilePath(mapFilePath);
		puzzleGeneratorModel.setStateName(stateName);
		proceed(Steps.STEP_3);
	}

	public void confirmGeneration() {
		puzzleGeneratorModel.setAbortGeneration(false);
		File temporaryDirectory = createTemporaryDirectory();
		if (temporaryDirectory != null) {
			puzzleGeneratorModel.setTemporaryDirectory(temporaryDirectory);
			Thread puzzleGenerationThread = initializePuzzleGenerationThread();
			proceed(Steps.STEP_4);
			puzzleGenerationThread.start();
		}
	}

	public void abortGenerationProcess() {
		puzzleGeneratorModel.setAbortGeneration(true);
		proceed(Steps.STEP_2);
	}

	/**
	 * This method will be called from the puzzle generation thread when the
	 * generation is complete.
	 */
	public void generationComplete() {
		Puzzle puzzle = puzzleGeneratorModel.getPuzzle();
		if (puzzle != null) {
			proceed(Steps.STEP_5);
		} else {
			proceed(Steps.STEP_2);
		}
	}

	public void setTargetAndSavePuzzle(String targetPath) {
		File targetDirectory = new File(targetPath);
		if (targetDirectory.exists() && targetDirectory.isDirectory()) {
			puzzleGeneratorModel.setTargetFolderPath(targetPath);
			try {
				Puzzle puzzle = puzzleGeneratorModel.getPuzzle();
				puzzleGeneratorModel.setGenerationSuccess(true);
				for (File puzzleFile : puzzle.getImages()) {
					moveFile(puzzleFile, targetDirectory);
				}
				File xmlFile = puzzle.getXmlFile();
				moveFile(xmlFile, targetDirectory);
				proceed(Steps.STEP_6);
			} catch (Exception e) {
				puzzleGeneratorModel.setNotification("Keine gültige Pfadangabe.", PuzzleGeneratorConfig.FAIL_COLOR);
				return;
			}
		} else {
			puzzleGeneratorModel.setNotification("Bitte geben Sie einen gültigen Ordner an.",
					PuzzleGeneratorConfig.FAIL_COLOR);
		}
	}

	public void finish() {
		puzzleGeneratorView.dispose();
	}

	private void moveFile(File file, File targetDestination) {
		try {
			String simpleFileName = file.getName();
			String newPathToFile = String.format("%s%s%s", targetDestination, File.separatorChar, simpleFileName);
			File newFile = new File(newPathToFile);
			file.renameTo(newFile);
		} catch (Exception e) {
			puzzleGeneratorModel.setGenerationSuccess(false);
			puzzleGeneratorModel.setNotification("Files konnten nicht verschoben werden.",
					PuzzleGeneratorConfig.FAIL_COLOR);
		}
	}

	private File createTemporaryDirectory() {
		File temporaryDirectory = null;
		try {
			FileAttribute<?>[] fileAttributes = {};
			temporaryDirectory = Files.createTempDirectory(TEMPORARY_DIRECTORY_NAME, fileAttributes).toFile();
		} catch (IOException e) {
			puzzleGeneratorModel
					.setNotification(
							"Ein temporäres Verzeichnis zum Zwischenspeichern konnte nicht erstellt werden. Es kann nicht fortgefahren werden.",
							PuzzleGeneratorConfig.FAIL_COLOR);
			puzzleGeneratorModel.setCurrentStep(Steps.STEP_6);
		}
		return temporaryDirectory;
	}

	private Thread initializePuzzleGenerationThread() {
		Thread puzzleGenerationThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Puzzle puzzle = null;
				String stateFilePath = puzzleGeneratorModel.getStateFilePath();
				String stateName = puzzleGeneratorModel.getStateName();
				String fieldNameFilePath = puzzleGeneratorModel.getFieldNameFilePath();
				String mapFilePath = puzzleGeneratorModel.getMapFilePath();
				String pathToTemporaryDirectory = puzzleGeneratorModel.getTemporaryDirectory().getAbsolutePath();
				try {
					puzzle = puzzleGeneratorService.generatePuzzle(stateFilePath, stateName, fieldNameFilePath,
							mapFilePath, pathToTemporaryDirectory);
				} catch (Exception e) {
					puzzleGeneratorModel.setNotification(
							"Beim Generieren der Puzzle-Teile ist ein Fehler aufgetreten.",
							PuzzleGeneratorConfig.FAIL_COLOR);
					puzzleGeneratorModel.setGenerationSuccess(false);
					puzzleGeneratorModel.setCurrentStep(Steps.STEP_6);
					return;
				}

				puzzleGeneratorModel.setPuzzle(puzzle);
				generationComplete();
			}
		});
		return puzzleGenerationThread;
	}

	public void showPreviousCard() {
		Steps currentStep = puzzleGeneratorModel.getCurrentStep();
		Steps previousStep = getPreviousStep(currentStep);
		puzzleGeneratorModel.setNotification("");
		puzzleGeneratorModel.setCurrentStep(previousStep);
	}

	private Steps getPreviousStep(Steps currentStep) {
		Steps previousStep = null;
		Steps[] steps = Steps.values();
		for (int i = 0; i < steps.length; i++) {
			if (steps[i].equals(currentStep)) {
				previousStep = steps[i - 1];
				break;
			}
		}
		return previousStep;
	}

	private void proceed(Steps step) {
		puzzleGeneratorModel.setNotification("");
		puzzleGeneratorModel.setCurrentStep(step);
	}

	public void setNotification(String notification, Color notificationColor) {
		puzzleGeneratorModel.setNotification(notification, notificationColor);
	}

}
