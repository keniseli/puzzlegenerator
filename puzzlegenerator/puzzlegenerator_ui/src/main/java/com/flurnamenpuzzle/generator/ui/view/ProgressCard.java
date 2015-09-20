package com.flurnamenpuzzle.generator.ui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import com.flurnamenpuzzle.generator.Observable;
import com.flurnamenpuzzle.generator.Observer;
import com.flurnamenpuzzle.generator.PuzzleGeneratorConfig;
import com.flurnamenpuzzle.generator.domain.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.ui.PuzzleGeneratorController;

/**
 * Class for the state selection panel
 * 
 */
public class ProgressCard extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	private PuzzleGeneratorController controller;
	private JLabel progressLabel;
	private JLabel progressTitleLabel;
	private JButton stopButton;
	private JProgressBar progressBar;

	/**
	 * Constructs a new instance
	 * 
	 * @param controller
	 *            the instance to control this class' behaviour.
	 */
	public ProgressCard(PuzzleGeneratorController controller) {
		this.controller = controller;
		initializeComponents();
		addEvents();
		layoutComponents();
	}

	/**
	 * working with gridBag to set the components in place
	 */
	private void layoutComponents() {
		add(this.progressTitleLabel, "height :30:");
		add(this.progressLabel, "height :30:, w 500!, wrap");
		add(this.progressBar, "height :30:, span, pushx, growx, gaptop 40");
		add(this.stopButton, "right, span, gaptop 40");
	}

	/**
	 * initialize all components needed for the panel
	 */
	private void initializeComponents() {
		this.setLayout(new MigLayout());
		this.setBorder(new EmptyBorder(20, 200, 20, 200));
		this.setSize(new Dimension(600, 600));
		this.setBackground(PuzzleGeneratorConfig.BACKGROUND_COLOR);
		
		this.progressLabel = new JLabel("Die Puzzleteile werden in diesem Moment erstellt.");
		this.progressLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		
		this.progressTitleLabel = new JLabel("Status");
		this.progressTitleLabel.setFont(PuzzleGeneratorConfig.FONT_BOLD);
		
		this.stopButton = new JButton("Abbrechen");
		this.stopButton.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		
		this.progressBar = new JProgressBar(0, 100);
		this.progressBar.setStringPainted(true);
		this.progressBar.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
	}

	/**
	 * add all actionlistener to the buttons
	 */
	private void addEvents() {
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null,
						"Wollen Sie den Vorgang wirklich abbrechen?",
						"Warning",
						dialogButton,
						JOptionPane.PLAIN_MESSAGE);
				
				if(dialogResult == JOptionPane.YES_OPTION){
					controller.abortGenerationProcess();
				}
			}
		});
	}

	@Override
	public void update(Observable observable) {
		PuzzleGeneratorModel model = (PuzzleGeneratorModel) observable;
		int percentage = model.getPercentageGenerated();
		String statusMessage = model.getStatusMessage();
		this.progressLabel.setText(statusMessage);
		
		this.progressBar.setValue(percentage);
		this.progressBar.setString(progressBar.getValue()+"%");
	}
}
