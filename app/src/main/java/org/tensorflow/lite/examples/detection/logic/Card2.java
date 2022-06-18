package org.tensorflow.lite.examples.detection.logic;

import java.util.Locale;

public class Card2 {

    private String title;
    private int rank;
    private Suit suit;
    private SuitColor suitColor;

    public Card2(String title){
        this.title = title;
        findCardFromTitle(title);
    }

    public Card2() {

    }

    public String getTitle() {
        return title;
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

    public String getDescription(){
        if(rank >= 2 && rank <= 10){
            return Integer.toString(rank);
        }
        switch (rank) {
            case 1:
                return "A";
            case 11:
                return "J";
            case 12:
                return "Q";
            case 13:
                return "K";
            default:
                return "???";
        }
    }


    private void findCardFromTitle(String title){
        try{
            System.out.println(" mylog title: --------------------" + title);
            title = title.trim();
            String suitValue = title.substring(title.length()-1);
            System.out.println("mylog suit value: " + suitValue);
            String rankValue = title.replace(suitValue, "");
            System.out.println("mylog rank value: " + rankValue);
            suit = Suit.valueOf(suitValue);
            System.out.println("mylog suit: " + suit);
            rank = getRankFromStringValue(rankValue);
            System.out.println("mylog rank : " + rank);
            suitColor = isRed() ? SuitColor.RED : SuitColor.BLACK;
            System.out.println("mylog ----------------------------------");
        }
        catch(Exception e){
            Integer a = 0;
            System.out.println("mylog exceptionnnnnnnnnnnnnnnn "+e.getMessage() );
        }
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
