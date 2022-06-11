package org.tensorflow.lite.examples.detection.logic;

import java.util.ArrayList;

public class MovesFinder {

    public ArrayList<CardsMove> FindMoves(String[] cardTitles){
        ArrayList<CardsMove> moves = new ArrayList<>();
        for (int i = 0; i < cardTitles.length; i++) {
            Card2 cardAtHand = new Card2(cardTitles[i]);
            for (int j = 0; j < cardTitles.length; j++) {
                Card2 card = new Card2(cardTitles[j]);
                if(canCardAtHandBeOnTopOfSecondCard(cardAtHand, card)){
                    CardsMove move = new CardsMove(card, cardAtHand);
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    private Boolean canCardAtHandBeOnTopOfSecondCard(Card2 cardAtHand, Card2 card){
        // Fail fast
        if(cardAtHand.getSuitColor() == card.getSuitColor()){
            return false;
        }
        if(cardAtHand.getRank()+1 != card.getRank()){
            return false;
        }
        return true;
    }

}
