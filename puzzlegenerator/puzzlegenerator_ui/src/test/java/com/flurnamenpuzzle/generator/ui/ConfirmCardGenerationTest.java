package com.flurnamenpuzzle.generator.ui;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.flurnamenpuzzle.generator.ui.view.ConfirmCardGeneration;

public class ConfirmCardGenerationTest {
	ConfirmCardGeneration card;

	@Before
	public void setUp() {
		card = new ConfirmCardGeneration(null);
	}

	@Test
	public void testShortPathWithShortFilenameLinuxAndMac() {
		String shortenedFilePath = card.shortenFilePath("/Users/ordner/file.shp");
		Assert.assertEquals("/Users/ordner/file.shp", shortenedFilePath);
	}

	@Test
	public void testShortPathWithShortFilenameWindows() {
		String shortenedFilePath = card.shortenFilePath("C:/Users/ordner/file.shp");
		Assert.assertEquals("C:/Users/ordner/file.shp", shortenedFilePath);
	}

	@Test
	public void testLongPathWithShortFilenameLinuxAndMac() {
		String shortenedFilePath = card
				.shortenFilePath("/Users/ordner/nomoleine/undnomoleine/dahetssogarnomoleine/bla/bli/blu/file.shp");
		Assert.assertEquals("/Users/.../file.shp", shortenedFilePath);
	}

	@Test
	public void testLongPathWithShortFilenameWindows() {
		String shortenedFilePath = card
				.shortenFilePath("C:/Users/ordner/nomoleine/undnomoleine/dahetssogarnomoleine/bla/bli/blu/file.shp");
		Assert.assertEquals("C:/Users/.../file.shp", shortenedFilePath);
	}

	@Test
	public void testShortPathWithLongFilenameLinuxAndMac() {
		String shortenedFilePath = card.shortenFilePath("/Users/ordner/shapefilemegalang.shp");
		Assert.assertEquals("/Users/ordner/shapefilem...shp", shortenedFilePath);
	}

	@Test
	public void testShortPathWithLongFilenameWindows() {
		String shortenedFilePath = card.shortenFilePath("C:/Users/shapefilemegalang.shp");
		Assert.assertEquals("C:/Users/shapefilem...shp", shortenedFilePath);
	}

	@Test
	public void testLongPathWithLongFilenameLinuxAndMac() {
		String shortenedFilePath = card
				.shortenFilePath("/Users/ordner/nomoleine/undnomoleine/dahetssogarnomoleine/bla/bli/blu/shapefilemegalang.shp");
		Assert.assertEquals("/Users/.../shapefilem...shp", shortenedFilePath);
	}

	@Test
	public void testLongPathWithLongFilenameWindows() {
		String shortenedFilePath = card
				.shortenFilePath("C:/Users/ordner/nomoleine/undnomoleine/dahetssogarnomoleine/bla/bli/blu/shapefilemegalang.shp");
		Assert.assertEquals("C:/Users/.../shapefilem...shp", shortenedFilePath);
	}
	
	@Test
	public void testShortPathWithShortFilenameWithMultipleFolders() {
		String shortenedFilePath = card
				.shortenFilePath("C:/a/b/c/d/e/f/g/h/i/j/file.shp");
		Assert.assertEquals("C:/a/.../file.shp", shortenedFilePath);
	}

}
