package com.flurnamenpuzzle.generator;

/**
 * This interface is to be implemented by classes whose objects should be
 * observable: changes on objects are relevant to other objects which do not
 * necessarily know the implementing class.
 */
public interface Observable {

    /**
     * This method adds an {@link Observer}. The added {@link Observer} will be
     * notified (using {@link Observable#notifyObservers}) whenever a relevant change occurs.
     * 
     * @param observer
     *            The {@link Observer} to observe this object.
     */
    void addObserver(Observer observer);

    /**
     * This method removes an {@link Observer}. The given {@link Observer} will
     * not be notified anymore on any change.
     * 
     * @param observer
     *            The {@link Observer} observing this object.
     */
    void removeObserver(Observer observer);

    /**
     * This method should be called in order to notify all {@link Observer
     * Observers} of this object.
     */
    void notifyObservers();

}
