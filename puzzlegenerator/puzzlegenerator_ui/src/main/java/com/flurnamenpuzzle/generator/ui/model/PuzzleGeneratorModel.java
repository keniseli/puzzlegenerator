package com.flurnamenpuzzle.generator.ui.model;

import java.util.ArrayList;
import java.util.List;

import com.flurnamenpuzzle.generator.ui.Observable;
import com.flurnamenpuzzle.generator.ui.Observer;
import com.flurnamenpuzzle.generator.ui.Steps;

public class PuzzleGeneratorModel implements Observable {
	private List<Observer> observers;

	private String notification;
	private Steps currentStep;
	private String[] states;
	private String fieldNameFilePath;
	private String mapFilePath;
	private String stateFilePath;
	private boolean generationSuccess = true;

	private String targetFolderPath;
	private int percentageGenerated;
	private String statusMessage;

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

	public Steps getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(Steps currentStep) {
		this.currentStep = currentStep;
		notifyObservers();
	}

	public String[] getStates() {
		return states;
	}

	public void setStates(String[] states) {
		this.states = states;
		notifyObservers();
	}

	public String getFieldNameFilePath() {
		return fieldNameFilePath;
	}

	public void setFieldNameFilePath(String fieldNameFilePath) {
		this.fieldNameFilePath = fieldNameFilePath;
		notifyObservers();
	}

	public String getMapFilePath() {
		return mapFilePath;
	}

	public void setMapFilePath(String mapFilePath) {
		this.mapFilePath = mapFilePath;
		notifyObservers();
	}
	
	public void setStateFilePath(String stateFilePath) {
		this.stateFilePath = stateFilePath;
		notifyObservers();
	}
	
	public String getStateFilePath() {
		return stateFilePath;
	}

	public void setTargetFolderPath(String targetPath) {
		this.targetFolderPath = targetPath;
		notifyObservers();
	}
	
	public String getTargetFolderPath() {
		return this.targetFolderPath;
	}
	
	public void setPercentageGenerated(int percentageValue) {
		this.percentageGenerated = percentageValue;
		notifyObservers();
	}
	
	public int getPercentageGenerated() {
		return percentageGenerated;
	}
	
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		notifyObservers();
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}

	public boolean getGenerationSuccess() {
		return generationSuccess;
	}

	public void setGenerationSuccess(boolean generationSuccess) {
		this.generationSuccess = generationSuccess;
	}
}