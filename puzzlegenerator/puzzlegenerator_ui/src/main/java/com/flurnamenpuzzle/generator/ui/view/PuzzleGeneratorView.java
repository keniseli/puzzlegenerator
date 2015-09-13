package com.flurnamenpuzzle.generator.ui.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.flurnamenpuzzle.generator.Observable;
import com.flurnamenpuzzle.generator.Observer;
import com.flurnamenpuzzle.generator.PuzzleGeneratorConfig;
import com.flurnamenpuzzle.generator.Steps;
import com.flurnamenpuzzle.generator.domain.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;

public class PuzzleGeneratorView extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	private static final String PUZZLE_GENERATOR_VIEW_TITLE = "Puzzle Generator";
	private static final Dimension PREF_SIZE = new Dimension(1024, 768);
	private static final String FOOTER_TEXT = "\u00A9 2015 Daniel Mohler-Schmid | Puzzlegenerator - "
			+ "Ein Projekt der Fachhochschule Nordwestschweiz für Technik, Windisch AG";
	private static final String LOGO_FHNW = "/logoFHNW.png";
	private static final String LOGO_PUZZLE = "/logoPuzzle.png";

	private CardLayout cardLayout;
	private JPanel cards;
	private JPanel headerPanel;
	private JPanel footerPanel;
	private JLabel steps;

	private PuzzleGeneratorController controller;

	/**
	 * This method will create and show the ui. The view builds its components
	 * and shows everything accordingly.
	 * @param cardMap TODO
	 */
	public void createAndShow(PuzzleGeneratorController controller, Map<String, JPanel> cardMap) {
		this.controller = controller;
		initializeComponents(cardMap);
		addEvents();
		layoutComponents();

		setTitle(PUZZLE_GENERATOR_VIEW_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setPreferredSize(PREF_SIZE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initializeComponents(Map<String, JPanel> cardMap) {
		headerPanel = createHeaderPanel();
		footerPanel = createFooterPanel();
		cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);
		Set<Entry<String, JPanel>> cardAndIdEntries = cardMap.entrySet();
		for (Entry<String, JPanel> entry : cardAndIdEntries) {
			String idOfCard = entry.getKey();
			JPanel card = entry.getValue();
			cards.add(card, idOfCard);
		}
	}

	private void addEvents() {
		// TODO add all events from buttons, text fields etc. here. In most
		// cases call the controller.
	}

	private void layoutComponents() {
		Container contentPane = getContentPane();
		contentPane.setBackground(PuzzleGeneratorConfig.BACKGROUND_COLOR);
		setLayout(new BorderLayout());
		add(cards, BorderLayout.CENTER);
		add(headerPanel, BorderLayout.NORTH);
		add(footerPanel, BorderLayout.SOUTH);
	}

	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.setLayout(new BorderLayout());
		JLabel logoFHNW = new JLabel();
		JLabel logoPuzzle = new JLabel();
		steps = new JLabel();
		logoFHNW.setIcon(new ImageIcon(this.getClass().getResource(LOGO_FHNW)));
		logoPuzzle.setIcon(new ImageIcon(this.getClass().getResource(LOGO_PUZZLE)));
		headerPanel.add(logoFHNW, BorderLayout.WEST);
		headerPanel.add(steps, BorderLayout.CENTER);
		headerPanel.add(logoPuzzle, BorderLayout.EAST);
		return headerPanel;
	}

	private JPanel createFooterPanel() {
		JPanel footerPanel = new JPanel();
		footerPanel.setOpaque(false);
		JLabel footerText = new JLabel();
		footerText.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		footerText.setText(FOOTER_TEXT);
		footerPanel.add(footerText);
		return footerPanel;
	}

	@Override
	public void update(Observable observable) {
		PuzzleGeneratorModel model = (PuzzleGeneratorModel) observable;		
		Steps currentStep = model.getCurrentStep();
		String currentStepId = currentStep.getId();
		String imagePath = currentStep.getImagePath();
		this.steps.setIcon(new ImageIcon(this.getClass().getResource(imagePath)));
		cardLayout.show(cards, currentStepId);
	}

}
