package org.tensorflow.lite.examples.detection.logic;

public enum CardType {
    ACE(1, "Ace"),
    TWO(2, "Two"),
    THREE(3, "Three"),
    FOUR(4, "Four"),
    FIVE(5, "Five"),
    SIX(6, "Six"),
    SEVEN(7, "Seven"),
    EIGHT(8, "Eight"),
    NINE(9, "Nine"),
    TEN(10, "Ten"),
    JACK(11, "Jack"),
    QUEEN(12, "Queen"),
    KING(13, "King");


    private final int value;
    private final String rank;

    CardType(int value, String rank) {
        this.value = value;
        this.rank = rank;
    }


    public int getValue() {
        return value;
    }

    public String getRank() {
        return rank;
    }

    public String printRank() {
        return rank;
    }


}
