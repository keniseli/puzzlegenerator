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
		add(progressTitleLabel, "height :30:");
		add(progressLabel, "height :30:, w 500!, wrap");
		add(progressBar, "height :30:, span, pushx, growx, gaptop 40");
		add(stopButton, "right, span, gaptop 40");
	}

	/**
	 * initialize all components needed for the panel
	 */
	private void initializeComponents() {
		setLayout(new MigLayout());
		setBorder(new EmptyBorder(20, 200, 20, 200));
		setSize(new Dimension(600, 600));
		setBackground(PuzzleGeneratorConfig.BACKGROUND_COLOR);

		progressTitleLabel = new JLabel("Status");
		progressTitleLabel.setFont(PuzzleGeneratorConfig.FONT_BOLD);

		progressLabel = new JLabel("Die Puzzleteile werden in diesem Moment erstellt.");
		progressLabel.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		stopButton = new JButton("Abbrechen");
		stopButton.setFont(PuzzleGeneratorConfig.FONT_NORMAL);

		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		progressBar.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
	}

	/**
	 * add all actionlistener to the buttons
	 */
	private void addEvents() {
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(null, "Wollen Sie den Vorgang wirklich abbrechen?",
						"Warning", dialogButton, JOptionPane.PLAIN_MESSAGE);
				if (dialogResult == JOptionPane.YES_OPTION) {
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
		if (statusMessage != null) {
			progressLabel.setText(statusMessage);
		}
		progressBar.setValue(percentage);
		progressBar.setString(progressBar.getValue() + "%");
	}
}
