package org.tensorflow.lite.examples.detection.logic;

public class Card {

    private final Suit suit;
    private final CardType cardType;

    public Card(Suit suit, CardType cardType) {
        this.suit = suit;
        this.cardType = cardType;
    }

    public Suit getSuit() {
        return suit;
    }

    public CardType getCardType() {
        return cardType;
    }

    public boolean isRed() {
        return suit == Suit.H || suit == Suit.D;
    }

//    @Override
//    public String toString() {
//        return getCardType().printRank() +" of "+ getSuit().printSuit();
//    }
}
