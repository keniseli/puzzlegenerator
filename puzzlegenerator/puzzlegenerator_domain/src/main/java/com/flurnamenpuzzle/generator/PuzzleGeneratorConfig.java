package com.flurnamenpuzzle.generator;

import java.awt.Color;
import java.awt.Font;

/**
 * 
 * @author Team Puzzlegenerator
 * This interface is the config file of the project and contains all global constants that are used in different places the source code.
 *
 */

public final class PuzzleGeneratorConfig {
	
	private PuzzleGeneratorConfig() {
		// restrict instantiation
	}
	
	public static final Font FONT_NORMAL = new Font("Helvetica", Font.PLAIN, 16);
	public static final Font FONT_BOLD = new Font("Helvetica", Font.BOLD, 16);
	public static final Font FONT_TITLE = new Font("Helvetica", Font.BOLD, 20);
	
	public static final String STEPS_1_IMAGE = "/Step1.png";
	public static final String STEPS_2_IMAGE = "/Step2.png";
	public static final String STEPS_3_IMAGE = "/Step3.png";
	public static final String STEPS_4_IMAGE = "/Step4.png";
	public static final String STEPS_5_IMAGE = "/Step5.png";
	
	public static final String SUCCESS_IMAGE = "/success.png";
	public static final String FAIL_IMAGE = "/failed.png";
	
	public static final Color BACKGROUND_COLOR = Color.WHITE;
	public static final Color SUCCESS_COLOR = new Color(161, 184, 56);
	public static final Color FAIL_COLOR = new Color(212, 56, 61);
}
