package com.flurnamenpuzzle.generator.ui.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.flurnamenpuzzle.generator.ui.Observable;
import com.flurnamenpuzzle.generator.ui.Observer;

public class PuzzleGeneratorModel implements Observable {
	private List<Observer> observers;

	private String notification;
	private File stateFile;


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

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
		notifyObservers();
	}

	public File getStateFile() {
		return stateFile;
	}
	
	public void setStateFile(File stateFile) {
		this.stateFile = stateFile;
		notifyObservers();
	}
}