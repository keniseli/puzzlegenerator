package com.flurnamenpuzzle.generator.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.List;

import mockit.integration.junit4.JMockit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.flurnamenpuzzle.generator.domain.Puzzle;

@RunWith(JMockit.class)
public class PuzzleGeneratorServiceTest {
	private PuzzleGeneratorService puzzleGeneratorService;

	@Before
	public void setUp() {
		puzzleGeneratorService = new PuzzleGeneratorService();
		new ShapeService();
	}

	@Test
	public void testPuzzleGenerationWithValidData() throws IOException {
		String stateShapeFilePath = getFilePathFromResource("/ExportPerimeter.shp");
		String stateName = "Thun";
		String fieldShapeFilePath = getFilePathFromResource("/No_Flurname_A.shp");
		String tifFilePath = getFilePathFromResource("/SRM25_LV03_KREL_10L_Mosaic_2015.tif");
		Path temporaryDirectoryPath = Files.createTempDirectory("testDirectoryOfThePuzzleGenerationTest",
				new FileAttribute<?>[0]);
		File temporaryDirectory = temporaryDirectoryPath.toFile();
		String temporaryDirectoryName = temporaryDirectory.getAbsolutePath();
		Puzzle puzzle = puzzleGeneratorService.generatePuzzle(stateShapeFilePath, stateName, fieldShapeFilePath,
				tifFilePath, temporaryDirectoryName);

		List<File> images = puzzle.getImages();
		int numberOfShapesInPuzzle = images.size();
		Assert.assertEquals(301, numberOfShapesInPuzzle);
		
		tearDown(puzzle.getImages());
		temporaryDirectory.deleteOnExit();
	}

	private void tearDown(List<File> files) {
		for (File puzzlePiece : files) {
			puzzlePiece.delete();
		}
	}

	@Test(expected = ServiceException.class)
	public void testPuzzleGenerationWithNotOverlappingStateShapeFile() {
		String stateShapeFilePath = getFilePathFromResource("/stateShapeNotOverlapping.shp");
		String stateName = "Thun";
		String fieldShapeFilePath = getFilePathFromResource("/No_Flurname_A.shp");
		String tifFilePath = getFilePathFromResource("/SRM25_LV03_KREL_10L_Mosaic_2015.tif");
		puzzleGeneratorService.generatePuzzle(stateShapeFilePath, stateName, fieldShapeFilePath, tifFilePath,
				"C://Temp//schnitzel");
		Assert.fail("ServiceException expected but not thrown");
	}

	@Test(expected = ServiceException.class)
	public void testPuzzleGenerationWithNotOverlappingFieldShapeFile() {
		String stateShapeFilePath = getFilePathFromResource("/ExportPerimeter.shp");
		String stateName = "Thun";
		String fieldShapeFilePath = getFilePathFromResource("/fieldShapeNotOverlapping.shp");
		String tifFilePath = getFilePathFromResource("/SRM25_LV03_KREL_10L_Mosaic_2015.tif");
		puzzleGeneratorService.generatePuzzle(stateShapeFilePath, stateName, fieldShapeFilePath, tifFilePath,
				"C://Temp//schnitzel");
		Assert.fail("ServiceException expected but not thrown");
	}

	@Test(expected = ServiceException.class)
	@Ignore
	public void testPuzzleGenerationWithInvalidFieldShapeFile() {

	}

	@Test(expected = ServiceException.class)
	public void testPuzzleGenerationWithInvalidTiffFile() {
		String stateShapeFilePath = getFilePathFromResource("/ExportPerimeter.shp");
		String stateName = "Thun";
		String fieldShapeFilePath = getFilePathFromResource("/No_Flurname_A.shp");
		String tifFilePath = getFilePathFromResource("/tifFileWithoutGeoRefs.tif");
		Puzzle puzzle = puzzleGeneratorService.generatePuzzle(stateShapeFilePath, stateName, fieldShapeFilePath,
				tifFilePath, "C://Temp//schnitzel");

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
