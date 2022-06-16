package org.tensorflow.lite.examples.detection.logic;

public class CardColumn {

    int amountOfCards;
    int columnId;
    Card2 card;

    public CardColumn(int amountOfCards, int columnId, Card2 card) {
        this.amountOfCards = amountOfCards;
        this.columnId = columnId;
        this.card = card;
    }

    public CardColumn(int columnId, Card2 card) {
        this.columnId = columnId;
        this.card = card;
    }

    public CardColumn() {

    }

    public Card2 getCard() {
        return card;
    }

    public void setCard(Card2 card) {
        this.card = card;
    }

    public int getAmountOfCards() {
        return amountOfCards;
    }

    public void setAmountOfCards(int amountOfCards) {
        this.amountOfCards = amountOfCards;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    @Override
    public String toString() {
        return "CardColumn{" +
                "amountOfCards=" + amountOfCards +
                ", columnId=" + columnId +
                ", card=" + card +
                '}';
    }
}
