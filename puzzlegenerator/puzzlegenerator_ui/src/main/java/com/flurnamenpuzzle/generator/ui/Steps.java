package com.flurnamenpuzzle.generator.ui;

/**
 * This class holds all steps that can occur in the application. Every step
 * provides an id so that it can be identified and differentiated.
 * 
 */
public enum Steps {
	STEP_1("stateSelection"), STEP_2("fieldNameMapSelection"), STEP_3("verificationAndDecision"), STEP_4("chooseTargetAndSave");

	private String id;

	private Steps(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
