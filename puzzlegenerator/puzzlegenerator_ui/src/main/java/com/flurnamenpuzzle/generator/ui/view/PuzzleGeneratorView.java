package com.flurnamenpuzzle.generator.ui.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.flurnamenpuzzle.generator.ui.Observable;
import com.flurnamenpuzzle.generator.ui.Observer;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorConfig;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;

public class PuzzleGeneratorView extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	
	private static final Dimension PREF_SIZE = new Dimension(1024, 768);
	private static final String FOOTER_TEXT = "\u00A9 2015 Daniel Mohler-Schmid | Puzzlegenerator - "
			+ "Ein Projekt der Fachhochschule Nordwestschweiz für Technik, Windisch AG";
	
	private static final String LOGO_FHNW = "../images/logoFHNW.png";
	private static final String LOGO_PUZZLE = "../images/logoPuzzle.png";
	private static final String STEPS_IMAGE = "../images/steps.png";

	private CardLayout cardLayout;
	private JPanel cards;
	private JPanel headerPanel;
	private JPanel footerPanel;
	
	private StateSelectionCard stateSel;
	private FieldnameMapSelectionCard fieldNameSel;
	private PuzzleGeneratorController controller;

	/**
	 * This method will create and show the ui. The view builds its components
	 * and shows everything accordingly.
	 */
	public void createAndShow(PuzzleGeneratorController controller) {
		this.controller = controller;
		initializeComponents();
		addEvents();

		add(fieldNameSel);

		getContentPane().setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		cards.add(this.stateSel);
		cardLayout.show(cards, "1");
		add(cards, BorderLayout.CENTER);
		add(headerPanel, BorderLayout.NORTH);
		add(footerPanel, BorderLayout.SOUTH);
		setTitle("Puzzle Generator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setPreferredSize(PREF_SIZE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initializeComponents() {
		this.headerPanel = createHeaderPanel();
		this.footerPanel = createFooterPanel();
		this.stateSel = new StateSelectionCard(this.controller);
		this.fieldNameSel = new FieldnameMapSelectionCard(this.controller);
		this.cardLayout = new CardLayout();
		this.cards = new JPanel(cardLayout);
	}

	private void addEvents() {
		// TODO add all events from buttons, text fields etc. here. In most
		// cases call the controller.
	}
	
	private JPanel createHeaderPanel(){
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout());
		JLabel logoFHNW = new JLabel();
		JLabel logoPuzzle = new JLabel();
		JLabel steps = new JLabel();
		logoFHNW.setIcon(new ImageIcon(this.getClass().getResource(LOGO_FHNW)));
		logoPuzzle.setIcon(new ImageIcon(this.getClass().getResource(LOGO_PUZZLE)));
		steps.setIcon(new ImageIcon(this.getClass().getResource(STEPS_IMAGE)));
		headerPanel.add(logoFHNW, BorderLayout.WEST);
		headerPanel.add(steps, BorderLayout.CENTER);
		headerPanel.add(logoPuzzle, BorderLayout.EAST);
		return headerPanel;
	}
	
	private JPanel createFooterPanel(){
		JPanel footerPanel = new JPanel();
		JLabel footerText = new JLabel();
		footerText.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		footerText.setText(FOOTER_TEXT);
		footerPanel.add(footerText);
		return footerPanel;
	}

	@Override
	public void update(Observable observable) {
		// TODO: what happens when the model has changed?
	}

}
