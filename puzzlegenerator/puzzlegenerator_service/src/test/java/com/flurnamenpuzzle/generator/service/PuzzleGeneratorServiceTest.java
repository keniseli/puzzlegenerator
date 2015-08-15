package com.flurnamenpuzzle.generator.service;

import java.io.File;
import java.net.URL;
import java.util.List;

import mockit.integration.junit4.JMockit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.flurnamenpuzzle.generator.domain.Puzzle;

@RunWith(JMockit.class)
public class PuzzleGeneratorServiceTest {
	private PuzzleGeneratorService puzzleGeneratorService;
	private ShapeService shapeService;
	
	@Before
	public void setUp() {
		puzzleGeneratorService = new PuzzleGeneratorService();
		shapeService = new ShapeService();
	}

	@Test
	public void testPuzzleGenerationWithValidData() {
		String stateShapeFilePath = getFilePathFromResource("/ExportFootprint.shp");
		String stateName = "H1444233";
		String fieldShapeFilePath = getFilePathFromResource("/No_Flurname_A.shp");
		List<String> namesOfShapeFile = shapeService.getNamesOfShapeFile(new File(fieldShapeFilePath));
		String tifFilePath = getFilePathFromResource("/SRM25_LV03_KREL_10L_Mosaic_2015.tif");
		Puzzle puzzle = puzzleGeneratorService.generatePuzzle(stateShapeFilePath, stateName, fieldShapeFilePath,
				tifFilePath, "C://Temp//schnitzel2");

	}

	@Test(expected = ServiceException.class)
	public void testPuzzleGenerationWithInvalidStateShapeFile() {

	}

	@Test(expected = ServiceException.class)
	public void testPuzzleGenerationWithInvalidFieldShapeFile() {

	}

	@Test(expected = ServiceException.class)
	public void testPuzzleGenerationWithInvalidTiffFile() {

	}

	@Test(expected = ServiceException.class)
	public void testPuzzleGenerationWithMissingStateName() {

	}

	private String getFilePathFromResource(String resource) {
		URL tifFileUrl = PuzzleGeneratorServiceTest.class.getResource(resource);
		String tifFilePath = tifFileUrl.getPath();
		return tifFilePath;
	}
}

