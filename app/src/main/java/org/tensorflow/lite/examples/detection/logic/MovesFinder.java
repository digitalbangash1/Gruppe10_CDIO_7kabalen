package org.tensorflow.lite.examples.detection.logic;

public class MovesFinder {



    public String[][] FindMoves(String[] cardTitles){

        for (int i = 0; i < cardTitles.length; i++) {

            Card2 cardAtHand = new Card2(cardTitles[i]);
            for (int j = 0; j < cardTitles.length; j++) {
                Card2 card = new Card2(cardTitles[j]);

            }

        }
    }

    private Boolean canCardAtHandBeOnTopOfSecondCard(Card2 cardAtHand, Card2 card){

        if(cardAtHand.getSuit().equalsIgnoreCase(card.getSuit())){
            return false;
        }

    }

}
