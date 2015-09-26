package com.flurnamenpuzzle.generator.ui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import com.flurnamenpuzzle.generator.Observable;
import com.flurnamenpuzzle.generator.Observer;
import com.flurnamenpuzzle.generator.PuzzleGeneratorConfig;
import com.flurnamenpuzzle.generator.domain.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;

public class ConfirmCardGeneration extends JPanel implements Observer{
	private static final long serialVersionUID = 1L;

	private PuzzleGeneratorController controller;
	
	private JLabel stateLabel;
	private JLabel stateNameLabel;
	private JLabel stateShapefileLabel;
	private JLabel stateShapefilePathLabel;
	private JLabel fieldnameShapefileLabel;
	private JLabel fieldnameShapefilePathLabel;
	private JLabel cardTiffLabel;
	private JLabel cardTiffPathLabel;
	
	private JButton generateButton;
	private JButton backButton;
	
	private String stateName;
	private String stateShapefilePath;
	private String fieldnameShapefilePath;
	private String cardTiffPath;
	
	
	/**
	 * Constructs a new instance
	 * 
	 * @param controller
	 *            the instance to control this class' behaviour.
	 */
	public ConfirmCardGeneration(PuzzleGeneratorController controller) {
		this.controller = controller;
		initializeComponents();
		addComponentsToPanel();
		addEvents();
	}
	/**
	 * working with migLayout to set the components in place
	 */	
	private void addComponentsToPanel() {
		add(stateLabel, "gaptop 40");
		add(stateNameLabel, "gap :40:, gapbottom 10, wrap");
		add(stateShapefileLabel);
		add(stateShapefilePathLabel, "gap :40:, gapbottom 10, wrap");
		add(fieldnameShapefileLabel);
		add(fieldnameShapefilePathLabel, "gap :40:, gapbottom 10, wrap");
		add(cardTiffLabel);
		add(cardTiffPathLabel, "gap :40:, gapbottom 10, wrap");
		add(backButton, "left, gaptop 40");
		add(generateButton, "right, gaptop 40");
	}
	/**
	 * initialize all components needed for the panel
	 */
	private void initializeComponents() {
		setLayout(new MigLayout());
		setBorder(new EmptyBorder(20, 200, 20, 200));
		setSize(new Dimension(600, 600));
		this.setBackground(PuzzleGeneratorConfig.BACKGROUND_COLOR);
		
		stateLabel = new JLabel("Gemeinde");
		stateLabel.setFont(PuzzleGeneratorConfig.FONT_BOLD);
		
		stateNameLabel = new JLabel(stateName);
		stateNameLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		
		stateShapefileLabel = new JLabel("Gemeinde-Shapefile");
		stateShapefileLabel.setFont(PuzzleGeneratorConfig.FONT_BOLD);
		
		stateShapefilePathLabel = new JLabel(stateShapefilePath);
		stateShapefilePathLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		
		fieldnameShapefileLabel = new JLabel("Flurnamen-Shapefile");
		fieldnameShapefileLabel.setFont(PuzzleGeneratorConfig.FONT_BOLD);
		
		fieldnameShapefilePathLabel = new JLabel(fieldnameShapefilePath);
		fieldnameShapefilePathLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		
		cardTiffLabel = new JLabel("Karten-Tiff");
		cardTiffLabel.setFont(PuzzleGeneratorConfig.FONT_BOLD);
		
		cardTiffPathLabel = new JLabel(cardTiffPath);
		cardTiffPathLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		
		backButton = new JButton("Zurück");
		backButton.setFont(PuzzleGeneratorConfig.FONT_BOLD);
		
		generateButton = new JButton("Generieren");
		generateButton.setFont(PuzzleGeneratorConfig.FONT_BOLD);
	}
	/**
	 * add all action listener to the buttons
	 */	
	private void addEvents() {
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.confirmGeneration();
			}
		});
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showPreviousCard();
			}
		});
	}
	
	@Override
	public void update(Observable observable) {
		PuzzleGeneratorModel model = (PuzzleGeneratorModel) observable;
		stateName = model.getStateName();
		stateNameLabel.setText(stateName);
		stateShapefilePath = model.getStateFilePath();
		stateShapefilePathLabel.setText(stateShapefilePath);
		fieldnameShapefilePath = model.getFieldNameFilePath();
		fieldnameShapefilePathLabel.setText(fieldnameShapefilePath);
		cardTiffPath = model.getMapFilePath();
		cardTiffPathLabel.setText(cardTiffPath);
	}

}
