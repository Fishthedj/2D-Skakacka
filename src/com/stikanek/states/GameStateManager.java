package com.stikanek.states;

/**
 * Interface to be implemented by classes that can get into several different states and can decide in which state they should be currently in.
 * @author Pavel
 */
public interface GameStateManager {

    /**
     * Sets the current state.
     * @param s state to which the current state should be set to
     */
    void setState(State s);
}
