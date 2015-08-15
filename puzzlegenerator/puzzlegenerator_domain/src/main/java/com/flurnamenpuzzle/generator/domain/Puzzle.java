package com.flurnamenpuzzle.generator.domain;

import java.io.File;
import java.util.List;

/**
 * This class represents the puzzle. That means it contains various images (png
 * Files), the xml file necessary for the puzzle game (www.flurnamenpuzzle.com)
 * and various other information like the name of the processed state.
 *
 */
public class Puzzle {
	private String stateName;
	private List<File> images;

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<File> getImages() {
		return images;
	}

	public void setImages(List<File> images) {
		this.images = images;
	}

}
