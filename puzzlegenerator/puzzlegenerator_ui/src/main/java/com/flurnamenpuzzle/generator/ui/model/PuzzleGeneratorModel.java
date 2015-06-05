package com.flurnamenpuzzle.generator.ui.model;

import java.util.ArrayList;
import java.util.List;

import com.flurnamenpuzzle.generator.ui.Observable;
import com.flurnamenpuzzle.generator.ui.Observer;

public class PuzzleGeneratorModel implements Observable {
    private List<Observer> observers;
    
    public PuzzleGeneratorModel() {
	observers = new ArrayList<Observer>();
    }
    
    @Override
    public void addObserver(Observer observer) {
	observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
	observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
	observers.forEach(observer -> observer.update(this));
    }

}
