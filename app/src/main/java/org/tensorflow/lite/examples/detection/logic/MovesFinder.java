package org.tensorflow.lite.examples.detection.logic;

import java.util.ArrayList;

public class MovesFinder {

    public ArrayList<CardsMove> findMoves(ArrayList<String> cardTitles){
        ArrayList<CardsMove> moves = new ArrayList<>();
        System.out.println("mylog before loop");
        for (int i = 0; i < cardTitles.size(); i++) {
            System.out.println("mylog inside loop");
            Card2 cardAtHand = new Card2(cardTitles.get(i));
            System.out.println("mylog card made");
            for (int j = 0; j < cardTitles.size(); j++) {
                System.out.println("mylog second card");
                Card2 card = new Card2(cardTitles.get(j));
                if(canCardAtHandBeOnTopOfSecondCard(cardAtHand, card)){
                    System.out.println("mylog move found");
                    CardsMove move = new CardsMove(card, cardAtHand);
                    moves.add(move);
                }
                else{
                    System.out.println("mylog no move found");
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
