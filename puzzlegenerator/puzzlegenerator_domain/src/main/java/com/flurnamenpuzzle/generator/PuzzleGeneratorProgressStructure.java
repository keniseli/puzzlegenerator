package com.flurnamenpuzzle.generator;

public interface PuzzleGeneratorProgressStructure {

	/**
	 * @return the percentage of the current puzzle generation.
	 */
	int getPercentageGenerated();

	/**
	 * @param percentage
	 *            set the percentage of the current puzzle generation.
	 */
	void setPercentageGenerated(int percentage);

	/**
	 * @return whether the current puzzle generation is being aborted.
	 */
	boolean isAbortGeneration();

	/**
	 * @param abortGeneration
	 *            set whether the current puzzle generation should be aborted.
	 */
	void setAbortGeneration(boolean abortGeneration);
}
