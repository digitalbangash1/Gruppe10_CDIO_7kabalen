package org.tensorflow.lite.examples.detection.logic;

import java.util.ArrayList;

public class CardsMove {

    private final ArrayList<Card2> cards = new ArrayList<>();

    public CardsMove(Card2... cards){
        for (int i = 0; i < cards.length; i++) {
            this.cards.add(cards[i]);
        }
    }

    public void addCard(Card2 card){
        cards.add(card);
    }

}
