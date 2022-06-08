package org.tensorflow.lite.examples.detection.logic;

public enum SuitSympol {

    Clubs("♣"),
    Hearts("♥"),
    Spades("♠"),
    Diamonds("♦");

    private String SuitSympol;

    SuitSympol(String suit) {
        this.SuitSympol = SuitSympol;
    }

    String printSuitSympol() {
        return SuitSympol;
    }
}
