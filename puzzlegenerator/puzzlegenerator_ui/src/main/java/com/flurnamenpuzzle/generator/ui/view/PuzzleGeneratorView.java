package com.flurnamenpuzzle.generator.ui.view;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.flurnamenpuzzle.generator.ui.Observable;
import com.flurnamenpuzzle.generator.ui.Observer;

public class PuzzleGeneratorView extends JFrame implements Observer {
    private static final long serialVersionUID = 1L;

    /**
     * This method will create and show the ui. The view builds its components
     * and shows everything accordingly.
     */
    public void createAndShow() {
	initializeComponents();
	JPanel content = layoutComponents();
	addEvents();

	add(content);

	pack();
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
	setVisible(true);
    }

    private void initializeComponents() {
	// TODO initialize all components here
    }

    private JPanel layoutComponents() {
	// TODO layout all the components here on a JPanel and return
	// it.
	JPanel content = new JPanel();
	content.setLayout(new CardLayout());
	// TODO: add a card (eg. the fileChooserPanel, progressPanel,
	// resultPanel...) here to the content. Note that the Panel itself
	// should be initialized in the #initializeComponents Method.
	return content;
    }

    private void addEvents() {
	// TODO add all events from buttons, text fields etc. here. In most
	// cases call the controller.
    }

    @Override
    public void update(Observable observable) {
	// TODO: what happens when the model has changed?
    }

}
