package com.flurnamenpuzzle.generator.ui;

import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.flurnamenpuzzle.generator.ui.model.PuzzleGeneratorModel;
import com.flurnamenpuzzle.generator.ui.view.PuzzleGeneratorView;

@RunWith(JMockit.class)
public class PuzzleGeneratorControllerTest {

    @Test
    public void testInitialization(@Mocked PuzzleGeneratorView puzzleGeneratorViewMock,
	    @Injectable PuzzleGeneratorModel puzzleGeneratorModelMock) {
	PuzzleGeneratorController controller = new PuzzleGeneratorController(puzzleGeneratorModelMock);
	controller.initializeView();

	new Verifications() {
	    {
		new PuzzleGeneratorView();
		times = 1;

		puzzleGeneratorModelMock.addObserver((Observer) any);
		times = 1;
	    }
	};
    }
}
