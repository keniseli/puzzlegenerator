package com.flurnamenpuzzle.generator.ui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
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

/**
 * Class for the state selection panel
 * 
 */
public class ResultCard extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;

	private PuzzleGeneratorController controller;
	private JLabel resultImage;
	private JLabel resultLabel;
	private JLabel resultList;
	private JButton finishButton;

	/**
	 * Constructs a new instance
	 * 
	 * @param controller
	 *            the instance to control this class' behaviour.
	 */
	public ResultCard(PuzzleGeneratorController controller) {
		this.controller = controller;
		initializeComponents();
		addEvents();
		layoutComponents();
	}

	/**
	 * working with gridBag to set the components in place
	 */
	private void layoutComponents() {
		add(resultList, "width 200::200, gapbottom 5");
		add(resultImage, "right, span, gapbottom 5, wrap");
		add(resultLabel, "center, span, height :30:");
		add(finishButton, "right, span, gaptop 40");
	}

	/**
	 * initialize all components needed for the panel
	 */
	private void initializeComponents() {
		setLayout(new MigLayout());
		setBorder(new EmptyBorder(20, 200, 20, 200));
		setSize(new Dimension(600, 600));
		setBackground(PuzzleGeneratorConfig.BACKGROUND_COLOR);

		resultList = new JLabel("");
		resultList.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		resultImage = new JLabel("");
		resultImage.setFont(PuzzleGeneratorConfig.FONT_NORMAL);
		resultLabel = new JLabel("");
		resultLabel.setFont(PuzzleGeneratorConfig.FONT_BOLD);
		finishButton = new JButton("Beenden");
		finishButton.setFont(PuzzleGeneratorConfig.FONT_BOLD);
	}

	/**
	 * add all actionlistener to the buttons
	 */
	private void addEvents() {

		finishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.finish();
			}
		});
	}

	@Override
	public void update(Observable observable) {
		PuzzleGeneratorModel model = (PuzzleGeneratorModel) observable;
		boolean success = model.isGenerationSuccess();
		if (success) {
			resultImage.setIcon(new ImageIcon(this.getClass().getResource(
					PuzzleGeneratorConfig.SUCCESS_IMAGE)));
			resultLabel.setText("Das Puzzle wurde erfolgreich erstellt!");
		} else {
			resultImage.setIcon(new ImageIcon(this.getClass().getResource(
					PuzzleGeneratorConfig.FAIL_IMAGE)));
			resultLabel.setText("Das Puzzle konnte nicht erstellt werden.");
			resultList.setForeground(model.getNotificationColor());
			resultList.setText(model.getNotification());
		}
	}
}
