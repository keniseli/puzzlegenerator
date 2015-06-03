package com.flurnamenpuzzle.generator.ui;

/**
 * This interface should be implemented by classes whose objects are dependent
 * on an {@link Observable} object which they do not know directly. The
 * {@link Observable} object will notify every {@link Observable} whenever it
 * changes relevantly.
 */
public interface Observer {

    /**
     * This method will be called by the {@link Observable} whenever it changes
     * relevantly.
     * 
     * @param observable
     * 
     * @param observable
     *            the {@link Observable} that changed.
     */
    void update(Observable observable);
}
