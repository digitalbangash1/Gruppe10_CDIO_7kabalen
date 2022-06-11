package org.tensorflow.lite.examples.detection.logic;

import java.util.Locale;

public class Card2 {

    private int rank;
    private Suit suit;
    private SuitColor suitColor;

    public Card2(String title){
        findCardFromTitle(title);
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public SuitColor getSuitColor() {
        return suitColor;
    }

    public boolean isRed() {
        return suit == Suit.H || suit == Suit.D;
    }

    public boolean isBlack() {
        return !isRed();
    }

    private void findCardFromTitle(String title){
        String suitValue = title.substring(title.length()-1);
        String rankValue = title.replace(suitValue, "");
        suit = Suit.valueOf(suitValue);
        rank = getRankFromStringValue(rankValue);
        suitColor = isRed() ? SuitColor.RED : SuitColor.BLACK;
    }

    private int getRankFromStringValue(String value){
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            switch (value.toUpperCase(Locale.ROOT)) {
                case "A":
                    return 1;
                case "J":
                    return 11;
                case "Q":
                    return 12;
                case "K":
                    return 13;
                default:
                    return 0;
            }
        }
    }


}
