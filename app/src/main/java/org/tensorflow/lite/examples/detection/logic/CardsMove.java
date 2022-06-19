package org.tensorflow.lite.examples.detection.logic;

import java.util.ArrayList;
import java.util.List;

public class CardsMove {

    private final List<Card2> cards = new ArrayList<>();

    public CardsMove(Card2... cards){
        for (int i = 0; i < cards.length; i++) {
            this.cards.add(cards[i]);
        }
    }

    public void addCard(Card2 card){
        cards.add(card);
    }

    public String getTitlesAsCommaSeparatedStringList(){
        String titles = "";
        for (int i = 0; i < cards.size(); i++) {
            Card2 card = cards.get(i);
            titles += card.getTitle() + ",";
        }
        titles = titles.trim();
        if(titles.endsWith(",")){
            titles = titles.substring(0, titles.length()-1);
        }
        return titles;
    }

    @Override
    public String toString() {
        String info = "";
        for (int i = 0; i < cards.size(); i++) {
            Card2 card = cards.get(i);
            info += card.getDescription()+card.getSuit().toString() + " ";
        }
        return info.trim();
    }
}
