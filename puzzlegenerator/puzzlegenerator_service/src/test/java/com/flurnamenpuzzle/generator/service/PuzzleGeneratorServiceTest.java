package com.flurnamenpuzzle.generator.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.List;

import mockit.integration.junit4.JMockit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.flurnamenpuzzle.generator.domain.Puzzle;
import com.flurnamenpuzzle.generator.domain.PuzzleGeneratorModel;

@RunWith(JMockit.class)
public class PuzzleGeneratorServiceTest {
	private PuzzleGeneratorService puzzleGeneratorService;
	private File temporaryDirectory;

	@Before
	public void setUp() throws IOException {
		PuzzleGeneratorModel puzzleGeneratorModel = new PuzzleGeneratorModel();
		puzzleGeneratorModel.setAbortGeneration(false);
		puzzleGeneratorService = new PuzzleGeneratorService(puzzleGeneratorModel);
		Path temporaryDirectoryPath = Files.createTempDirectory("testDirectoryOfThePuzzleGenerationTest",
				new FileAttribute<?>[0]);
		temporaryDirectory = temporaryDirectoryPath.toFile();
	}

	@After
	public void breakDown() {
		for (File file : temporaryDirectory.listFiles()) {
			file.deleteOnExit();
		}
		temporaryDirectory.deleteOnExit();
	}
	
	@Test
	public void testPuzzleGenerationWithValidData() throws IOException {
		String stateShapeFilePath = getFilePathFromResource("/ExportPerimeter.shp");
		String stateName = "Thun";
		String fieldShapeFilePath = getFilePathFromResource("/No_Flurname_A.shp");
		String tifFilePath = getFilePathFromResource("/SRM25_LV03_KREL_10L_Mosaic_2015.tif");
		String temporaryDirectoryName = temporaryDirectory.getAbsolutePath();
		Puzzle puzzle = puzzleGeneratorService.generatePuzzle(stateShapeFilePath, stateName, fieldShapeFilePath,
				tifFilePath, temporaryDirectoryName);

		List<File> images = puzzle.getImages();
		int numberOfShapesInPuzzle = images.size();
		Assert.assertEquals(301, numberOfShapesInPuzzle);
	}

	@Test(expected = ServiceException.class)
	public void testPuzzleGenerationWithNotOverlappingStateShapeFile() {
		String stateShapeFilePath = getFilePathFromResource("/stateShapeNotOverlapping.shp");
		String stateName = "Thun";
		String fieldShapeFilePath = getFilePathFromResource("/No_Flurname_A.shp");
		String tifFilePath = getFilePathFromResource("/SRM25_LV03_KREL_10L_Mosaic_2015.tif");
		String temporaryDirectoryPath = temporaryDirectory.getAbsolutePath();
		puzzleGeneratorService.generatePuzzle(stateShapeFilePath, stateName, fieldShapeFilePath, tifFilePath,
				temporaryDirectoryPath);
		Assert.fail("ServiceException expected but not thrown");
	}

	@Test(expected = ServiceException.class)
	public void testPuzzleGenerationWithNotOverlappingFieldShapeFile() {
		String stateShapeFilePath = getFilePathFromResource("/ExportPerimeter.shp");
		String stateName = "Thun";
		String fieldShapeFilePath = getFilePathFromResource("/fieldShapeNotOverlapping.shp");
		String tifFilePath = getFilePathFromResource("/SRM25_LV03_KREL_10L_Mosaic_2015.tif");
		String temporaryDirectoryPath = temporaryDirectory.getAbsolutePath();
		puzzleGeneratorService.generatePuzzle(stateShapeFilePath, stateName, fieldShapeFilePath, tifFilePath,
				temporaryDirectoryPath);
		Assert.fail("ServiceException expected but not thrown");
	}

	@Test(expected = ServiceException.class)
	public void testPuzzleGenerationWithInvalidTiffFile() {
		String stateShapeFilePath = getFilePathFromResource("/ExportPerimeter.shp");
		String stateName = "Thun";
		String fieldShapeFilePath = getFilePathFromResource("/No_Flurname_A.shp");
		String tifFilePath = getFilePathFromResource("/tifFileWithoutGeoRefs.tif");
		String temporaryDirectoryPath = temporaryDirectory.getAbsolutePath();
		Puzzle puzzle = puzzleGeneratorService.generatePuzzle(stateShapeFilePath, stateName, fieldShapeFilePath,
				tifFilePath, temporaryDirectoryPath);

		List<File> images = puzzle.getImages();
		int numberOfShapesInPuzzle = images.size();
		Assert.assertEquals(301, numberOfShapesInPuzzle);
	}

	@Test(expected = ServiceException.class)
	@Ignore
	public void testPuzzleGenerationWithMissingStateName() {

	}

	private String getFilePathFromResource(String resource) {
		URL tifFileUrl = PuzzleGeneratorServiceTest.class.getResource(resource);
		String tifFilePath = tifFileUrl.getPath();
		return tifFilePath;
	}
}
