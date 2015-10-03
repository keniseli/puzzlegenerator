package com.flurnamenpuzzle.generator.ui;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.flurnamenpuzzle.generator.ui.view.ConfirmGenerationCard;

public class ConfirmGenerationCardTest {
	private ConfirmGenerationCard card;

	@Before
	public void setUp() {
		card = new ConfirmGenerationCard(null);
	}

	@Test
	public void testShortPathWithShortFilenameLinuxAndMac() {
		String shortenedFilePath = card.shortenFilePath(File.separator + "Users" + File.separator + "ordner" + File.separator + "file.shp");
		Assert.assertEquals("" + File.separator + "Users" + File.separator + "ordner" + File.separator + "file.shp", shortenedFilePath);
	}

	@Test
	public void testShortPathWithShortFilenameWindows() {
		String shortenedFilePath = card.shortenFilePath("C:" + File.separator + "Users" + File.separator + "ordner" + File.separator + "file.shp");
		Assert.assertEquals("C:" + File.separator + "Users" + File.separator + "ordner" + File.separator + "file.shp", shortenedFilePath);
	}

	@Test
	public void testLongPathWithShortFilenameLinuxAndMac() {
		String shortenedFilePath = card
				.shortenFilePath("" + File.separator + "Users" + File.separator + "ordner" + File.separator + "nomoleine" + File.separator + "undnomoleine" + File.separator + "dahetssogarnomoleine" + File.separator + "bla" + File.separator + "bli" + File.separator + "blu" + File.separator + "file.shp");
		Assert.assertEquals("" + File.separator + "Users" + File.separator + "..." + File.separator + "file.shp", shortenedFilePath);
	}

	@Test
	public void testLongPathWithShortFilenameWindows() {
		String shortenedFilePath = card
				.shortenFilePath("C:" + File.separator + "Users" + File.separator + "ordner" + File.separator + "nomoleine" + File.separator + "undnomoleine" + File.separator + "dahetssogarnomoleine" + File.separator + "bla" + File.separator + "bli" + File.separator + "blu" + File.separator + "file.shp");
		Assert.assertEquals("C:" + File.separator + "Users" + File.separator + "..." + File.separator + "file.shp", shortenedFilePath);
	}

	@Test
	public void testShortPathWithLongFilenameLinuxAndMac() {
		String shortenedFilePath = card.shortenFilePath("" + File.separator + "Users" + File.separator + "ordner" + File.separator + "shapefilemegalang.shp");
		Assert.assertEquals("" + File.separator + "Users" + File.separator + "ordner" + File.separator + "shapefilem...shp", shortenedFilePath);
	}

	@Test
	public void testShortPathWithLongFilenameWindows() {
		String shortenedFilePath = card.shortenFilePath("C:" + File.separator + "Users" + File.separator + "shapefilemegalang.shp");
		Assert.assertEquals("C:" + File.separator + "Users" + File.separator + "shapefilem...shp", shortenedFilePath);
	}

	@Test
	public void testLongPathWithLongFilenameLinuxAndMac() {
		String shortenedFilePath = card
				.shortenFilePath("" + File.separator + "Users" + File.separator + "ordner" + File.separator + "nomoleine" + File.separator + "undnomoleine" + File.separator + "dahetssogarnomoleine" + File.separator + "bla" + File.separator + "bli" + File.separator + "blu" + File.separator + "shapefilemegalang.shp");
		Assert.assertEquals("" + File.separator + "Users" + File.separator + "..." + File.separator + "shapefilem...shp", shortenedFilePath);
	}

	@Test
	public void testLongPathWithLongFilenameWindows() {
		String shortenedFilePath = card
				.shortenFilePath("C:" + File.separator + "Users" + File.separator + "ordner" + File.separator + "nomoleine" + File.separator + "undnomoleine" + File.separator + "dahetssogarnomoleine" + File.separator + "bla" + File.separator + "bli" + File.separator + "blu" + File.separator + "shapefilemegalang.shp");
		Assert.assertEquals("C:" + File.separator + "Users" + File.separator + "..." + File.separator + "shapefilem...shp", shortenedFilePath);
	}

	@Test
	public void testShortPathWithShortFilenameWithMultipleFolders() {
		String shortenedFilePath = card.shortenFilePath("C:" + File.separator + "a" + File.separator + "b" + File.separator + "c" + File.separator + "d" + File.separator + "e" + File.separator + "f" + File.separator + "g" + File.separator + "h" + File.separator + "i" + File.separator + "j" + File.separator + "file.shp");
		Assert.assertEquals("C:" + File.separator + "a" + File.separator + "..." + File.separator + "file.shp", shortenedFilePath);
	}

}
