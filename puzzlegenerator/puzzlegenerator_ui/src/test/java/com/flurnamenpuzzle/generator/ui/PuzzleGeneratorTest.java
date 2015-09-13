package com.flurnamenpuzzle.generator.ui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

import javax.swing.SwingUtilities;

import org.junit.Test;

import com.flurnamenpuzzle.generator.domain.PuzzleGeneratorModel;

public class PuzzleGeneratorTest {

	@Test
	public void testPuzzleGenerator() throws IOException {
		PuzzleGeneratorModel puzzleGeneratorModel = new PuzzleGeneratorModel();
		PuzzleGeneratorController puzzleGeneratorController = new PuzzleGeneratorController(puzzleGeneratorModel);
		SwingUtilities.invokeLater(puzzleGeneratorController::initializeView);

		// TODO assertions

		String stateShapeFilePath = getResourcePath("/Gemeinde.shp");
		puzzleGeneratorController.saveStateFilePath(stateShapeFilePath);

		// TODO assertions

		String cardMaterialPath = getResourcePath("/Karte.tif");
		String fieldShapeFilePath = getResourcePath("/Flure.shp");
		puzzleGeneratorController.saveFieldNameFilePathAndCardMaterialFilePath(fieldShapeFilePath, cardMaterialPath,
				"Thun");
		
		// TODO assertions
		
		puzzleGeneratorController.confirmGeneration();
		
		// TODO assertions
		
		FileAttribute<?>[] attrs = {};
		Path createTempDirectory = Files.createTempDirectory("puzzleGeneratorTestTemporaryDirectory", attrs );
		String pathToTargetDirectory = createTempDirectory.toFile().getAbsolutePath();
		puzzleGeneratorController.setTargetAndSavePuzzle(pathToTargetDirectory);
	}

	private String getResourcePath(String name) {
		URL stateResource = PuzzleGeneratorTest.class.getResource(name);
		String path = stateResource.getPath();
		return path;
	}
}
