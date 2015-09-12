package com.flurnamenpuzzle.generator.ui.view;

import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.flurnamenpuzzle.generator.Observer;
import com.flurnamenpuzzle.generator.domain.PuzzleGeneratorModel;

@RunWith(JMockit.class)
public class PuzzleGeneratorModelTest {

    private PuzzleGeneratorModel puzzleGeneratorModel;

    @Test
    public void testObserverWithNoObserver(@Mocked final Observer observer) {
	puzzleGeneratorModel = new PuzzleGeneratorModel();
	puzzleGeneratorModel.notifyObservers();

	new Verifications() {{
		observer.update(puzzleGeneratorModel);
		times = 0;
	    }};
    }

    @Test
    public void testObserverWithSingleObserver(@Mocked final Observer observer) {
	puzzleGeneratorModel = new PuzzleGeneratorModel();

	puzzleGeneratorModel.addObserver(observer);
	puzzleGeneratorModel.notifyObservers();

	new Verifications() {{
		observer.update(puzzleGeneratorModel);
		times = 1;
	    }};
    }
    
    @Test
    public void testObserverWithSingleAddedAndRemovedObserver(@Mocked final Observer observer) {
	puzzleGeneratorModel = new PuzzleGeneratorModel();
	puzzleGeneratorModel.addObserver(observer);

	puzzleGeneratorModel.removeObserver(observer);
	puzzleGeneratorModel.notifyObservers();

	new Verifications() {{
		observer.update(puzzleGeneratorModel);
		times = 0;
	    }};
    }
    
    @Test
    public void testObserverWithMultipleObservers(@Mocked final Observer observer, @Mocked final Observer secondObserver) {
	puzzleGeneratorModel = new PuzzleGeneratorModel();
	puzzleGeneratorModel.addObserver(observer);
	puzzleGeneratorModel.addObserver(secondObserver);

	puzzleGeneratorModel.notifyObservers();

	new Verifications() {{
		observer.update(puzzleGeneratorModel);
		times = 1;
		
		secondObserver.update(puzzleGeneratorModel);
		times = 1;
	    }};
    }
    
    @Test
    public void testObserverWithSingleObserverAndMultipleUpdates(@Mocked final Observer observer) {
	puzzleGeneratorModel = new PuzzleGeneratorModel();
	puzzleGeneratorModel.addObserver(observer);

	puzzleGeneratorModel.notifyObservers();
	puzzleGeneratorModel.notifyObservers();
	puzzleGeneratorModel.notifyObservers();
	puzzleGeneratorModel.notifyObservers();
	puzzleGeneratorModel.notifyObservers();
	
	
	new Verifications() {{
		observer.update(puzzleGeneratorModel);
		times = 5;
	    }};
    }
    
}
