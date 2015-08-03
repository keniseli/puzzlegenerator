package com.flurnamenpuzzle.generator.ui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.flurnamenpuzzle.generator.ui.Observable;
import com.flurnamenpuzzle.generator.ui.Observer;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;

/**
 * Class for the state selection panel
 * @author P. Lustenberger
 *
 */
public class StateSelectionCard extends JPanel implements Observer{
	private static final long serialVersionUID = 1L;
	
	private PuzzleGeneratorController controller;
	private JFileChooser fileChooser;
	private JLabel label;
	private JButton searchButton;
	private JButton nextButton;
	private JPanel componentPanel;
	private JTextField pathField;
	
	/**
	 * constructor
	 * @param controller
	 */
	public StateSelectionCard(PuzzleGeneratorController controller) {
		this.controller = controller;
		initComponents();
		addComponentsToPanel();
		addEvents();
	}
	
	/**
	 * working with gridBag to set the components in place
	 */
	private void addComponentsToPanel() {
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		componentPanel.add(label, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		componentPanel.add(pathField, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		componentPanel.add(searchButton, gbc);
		
		this.add(componentPanel, BorderLayout.CENTER);
	}
	
	/**
	 * initialize all components needed for the panel
	 */
	private void initComponents() {
		fileChooser = new JFileChooser();
		pathField = new JTextField();
		pathField.setPreferredSize(new Dimension(200,20));
		label = new JLabel("Bitte wählen Sie ein GemeindeShape aus");		
		searchButton = new JButton("Durchsuchen");
		nextButton = new JButton("Weiter");
		componentPanel = new JPanel();
		
		componentPanel.setLayout(new GridBagLayout());
		BorderLayout border = new BorderLayout();
		this.setLayout(border);
		this.setSize(new Dimension(600, 600));
	}
	
	/**
	 * add all actionlistener to the buttons
	 */
	private void addEvents() {
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(componentPanel);
				// check if user has selected a file
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					pathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File stateFile = new File(pathField.getText());
				controller.setStateFilePath(stateFile);
			}
		});
	}

	@Override
	public void update(Observable observable) {}
}
