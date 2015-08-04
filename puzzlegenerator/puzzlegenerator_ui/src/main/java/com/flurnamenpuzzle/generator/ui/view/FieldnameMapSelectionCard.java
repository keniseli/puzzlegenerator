package com.flurnamenpuzzle.generator.ui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import com.flurnamenpuzzle.generator.ui.Observable;
import com.flurnamenpuzzle.generator.ui.Observer;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorConfig;
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
		initializeComponents();
		addComponentsToPanel();
		addEvents();
	}
	
	/**
	 * working with migLayout to set the components in place
	 */
	private void addComponentsToPanel() {
		add(stateDropdownLabel, "span");
		add(stateDropdown, "height :30:, pushx, growx, span, gapbottom 10");
		add(chooseFieldNameLabel, "span");
		add(fieldNamePath, "height :30:, pushx, growx");
		add(chooseFieldNameButton, "height :30:, span, wrap 10");
		add(chooseMapLabel, "span");
		add(mapPath, "height :30:, pushx, growx");
		add(chooseMapButton, "height :30:, wrap");
		add(nextButton, "right, span, gaptop 40");
	}
	
	/**
	 * initialize all components needed for the panel
	 */
	private void initializeComponents() {
		this.setLayout(new MigLayout());
		this.setBorder(new EmptyBorder(20, 200, 20, 200));
		this.setSize(new Dimension(600, 600));
		
		this.fileChooser = new JFileChooser();
		this.mapPath = new JTextField();
		this.mapPath.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		this.fieldNamePath = new JTextField();
		this.fieldNamePath.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		this.stateDropdownLabel = new JLabel("Bitte wählen Sie eine Gemeinde aus:");
		this.stateDropdownLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		this.chooseFieldNameLabel = new JLabel("Wählen Sie das Shapefile der Flurnamen:");
		this.chooseFieldNameLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		this.chooseMapLabel = new JLabel("Wählen Sie das Tiff-File mit dem Kartenmaterial:");
		this.chooseMapLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		this.chooseFieldNameButton = new JButton("Durchsuchen");
		this.chooseFieldNameButton.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		this.chooseMapButton = new JButton("Durchsuchen");
		this.chooseMapButton.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		this.nextButton = new JButton("Weiter");
		this.nextButton.setFont(PuzzleGeneratorConfig.FONT_BOLD);
		this.stateDropdown = new JComboBox<>(dropdownList);
		this.stateDropdown.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
	}
	
	/**
	 * add all actionlistener to the buttons
	 */
	private void addEvents() {
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Go to next card
			}
		});
		
		this.chooseFieldNameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(null);
				fieldNamePath.setText(fileChooser.getSelectedFile().getPath());
			}
		});
		
		this.chooseMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(null);
				mapPath.setText(fileChooser.getSelectedFile().getPath());
			}
		});
	}

	@Override
	public void update(Observable observable) {
		PuzzleGeneratorModel model = (PuzzleGeneratorModel) observable;
		//get dropdown list
	}
}
