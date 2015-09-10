package com.flurnamenpuzzle.generator.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.flurnamenpuzzle.generator.service.ShapeService;
import com.flurnamenpuzzle.generator.ui.model.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.ui.view.ConfirmCardGeneration;
import com.flurnamenpuzzle.generator.ui.view.FieldNameMapSelectionCard;
import com.flurnamenpuzzle.generator.ui.view.PuzzleGeneratorView;
import com.flurnamenpuzzle.generator.ui.view.TargetPathSelectionCard;

@RunWith(JMockit.class)
public class PuzzleGeneratorControllerTest {

	@Test
	public void testInitialization(@Mocked PuzzleGeneratorView puzzleGeneratorViewMock,
			@Injectable PuzzleGeneratorModel puzzleGeneratorModelMock) {
		PuzzleGeneratorController controller = new PuzzleGeneratorController(puzzleGeneratorModelMock);
		controller.initializeView();

		new Verifications() {
			{
				new PuzzleGeneratorView();
				times = 1;

				new TargetPathSelectionCard(controller);
				times = 1;

				new FieldNameMapSelectionCard(controller);
				times = 1;

				new ConfirmCardGeneration(controller);
				times = 1;

				puzzleGeneratorModelMock.addObserver((Observer) any);
				times = 4;
			}
		};
	}

	@Test
	public void testSaveStateFilePath(@Mocked ShapeService shapeService,
			@Injectable PuzzleGeneratorModel puzzleGeneratorModelMock) {
		String pathname = "path/to/shape/file.shp";
		List<String> returnedNames = new ArrayList<>();
		returnedNames.add("first shape name");
		returnedNames.add("second shape name");
		returnedNames.add("third shape name");
		File shapeFile = new File(pathname);
		new Expectations() {
			{
				shapeService.getNamesOfShapeFile(shapeFile);
				returns(returnedNames);
				times = 1;
			}
			
		};
		
		PuzzleGeneratorController controller = new PuzzleGeneratorController(puzzleGeneratorModelMock);
		controller.saveStateFilePath(pathname);
		
		String[] returnedNamesArray = { "first shape name", "second shape name", "third shape name" };
		new Verifications() {
			{
				puzzleGeneratorModelMock.setStateFilePath(pathname);
				times = 1;
				
				puzzleGeneratorModelMock.setStates(returnedNamesArray);
				times = 1;
				
				puzzleGeneratorModelMock.setCurrentStep(Steps.STEP_2);
				times = 1;
			}
		};
	}
}
