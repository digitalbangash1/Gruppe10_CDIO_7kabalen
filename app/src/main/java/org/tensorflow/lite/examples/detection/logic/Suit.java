package org.tensorflow.lite.examples.detection.logic;

public enum Suit {

    C("Clubs"),
    H("Hearts"),
    S("Spades"),
    D("Diamonds");

    private String suit;
    Suit(String suit) {
        this.suit = suit;
    }
}

