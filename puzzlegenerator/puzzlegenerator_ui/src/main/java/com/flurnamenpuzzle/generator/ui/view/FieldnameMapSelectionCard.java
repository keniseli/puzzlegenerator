package com.flurnamenpuzzle.generator.ui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.flurnamenpuzzle.generator.ui.Observable;
import com.flurnamenpuzzle.generator.ui.Observer;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;
import com.flurnamenpuzzle.generator.ui.model.PuzzleGeneratorModel;

public class FieldnameMapSelectionCard extends JPanel implements Observer{
private static final long serialVersionUID = 1L;
	
	private PuzzleGeneratorController controller;
	private JFileChooser fileChooser;
	private JLabel stateDropdownLabel;
	private JLabel chooseFieldNameLabel;
	private JLabel chooseMapLabel;
	private JButton chooseFieldNameButton;
	private JButton chooseMapButton;
	private JButton nextButton;
	private JPanel componentPanel;
	private JTextField fieldNamePath;
	private JTextField mapPath;
	private JComboBox<String> stateDropdown;
	
	private String[] dropdownList = {"Bern", "Luzern", "Zürich"};
	
	/**
	 * constructor
	 * @param controller
	 */
	public FieldnameMapSelectionCard(PuzzleGeneratorController controller) {
		this.controller = controller;
		this.setLayout(new GridBagLayout());
		initComponents();
		addComponentsToPanel();
		addEvents();
	}
	
	/**
	 * working with gridBag to set the components in place
	 */
	private void addComponentsToPanel() {
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagConstraints labelConstraints = new GridBagConstraints();
		GridBagConstraints textBoxContraints = new GridBagConstraints();
		GridBagConstraints buttonContraints = new GridBagConstraints();
		
		// State dropdown
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 0, 0);
		this.add(stateDropdownLabel, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(stateDropdown, gbc);
		
		//Fieldname shape
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 0, 0, 0);
		this.add(chooseFieldNameLabel, gbc);
		
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 0, 0, 10);
		this.add(fieldNamePath, gbc);
		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		this.add(chooseFieldNameButton, gbc);
		
		//Map
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 0, 0, 0);
		this.add(chooseMapLabel, gbc);
		
		//gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 0, 0, 10);
		this.add(mapPath, gbc);
		
		//gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		this.add(chooseMapButton, gbc);
	}
	
	/**
	 * initialize all components needed for the panel
	 */
	private void initComponents() {
		fileChooser = new JFileChooser();
		mapPath = new JTextField();
		fieldNamePath = new JTextField();
		mapPath.setPreferredSize(new Dimension(200,20));
		fieldNamePath.setPreferredSize(new Dimension(200,20));
		stateDropdownLabel = new JLabel("Bitte wählen Sie eine Gemeinde aus:");
		chooseFieldNameLabel = new JLabel("Wählen Sie das Shapefile der Flurnamen:");
		chooseMapLabel = new JLabel("Wählen Sie das Tiff-File mit dem Kartenmaterial:");
		chooseFieldNameButton = new JButton("Durchsuchen");
		chooseMapButton = new JButton("Durchsuchen");
		nextButton = new JButton("Weiter");
		stateDropdown = new JComboBox<>();
	}
	
	/**
	 * add all actionlistener to the buttons
	 */
	private void addEvents() {
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
	}

	@Override
	public void update(Observable observable) {
		PuzzleGeneratorModel model = (PuzzleGeneratorModel) observable;
		//get dropdown list
	}
}
