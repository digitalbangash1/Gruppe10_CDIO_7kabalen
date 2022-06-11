package org.tensorflow.lite.examples.detection.logic;

public enum CardType {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13);

    private final int value;

    CardType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
