package com.flurnamenpuzzle.generator;

/**
 * This class holds all steps that can occur in the application. Every step
 * provides an id so that it can be identified and differentiated.
 * 
 */
public enum Steps {
	STEP_1("stateSelection", PuzzleGeneratorConfig.STEPS_1_IMAGE), STEP_2(
			"fieldNameMapSelection", PuzzleGeneratorConfig.STEPS_2_IMAGE), STEP_3(
			"verificationAndDecision", PuzzleGeneratorConfig.STEPS_3_IMAGE), STEP_4(
			"progressPanel", PuzzleGeneratorConfig.STEPS_4_IMAGE), STEP_5(
			"targetPathPanel", PuzzleGeneratorConfig.STEPS_5_IMAGE), STEP_6(
			"resultPanel", PuzzleGeneratorConfig.STEPS_5_IMAGE);

	private String id;

	private String imagePath;

	private Steps(String id, String imagePath) {
		this.id = id;
		this.imagePath = imagePath;
	}

	public String getId() {
		return id;
	}

	public String getImagePath() {
		return imagePath;
	}
}
