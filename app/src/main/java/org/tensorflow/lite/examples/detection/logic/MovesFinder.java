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
                    CardsMove move = new CardsMove(cardAtHand,card);
                    moves.add(move);

                }
                else{
                    System.out.println("mylog no move found");
                }
            }
        }
        return moves;
    }

    public ArrayList<ColumnsMove> findMovesForColumns(ArrayList<String> commaSeparatedColumns){

        //commaSeparatedColumns looks like this:
        // 0: 2D,3D,5D
        // 1: 2F,3D,5D

        ArrayList<ColumnsMove> moves = new ArrayList<>();

        System.out.println("mylog before loop");
        for (int i = 0; i < commaSeparatedColumns.size(); i++) {
            String commaSeparatedColumnAtHand = commaSeparatedColumns.get(i);
            String[] columnAtHamCards = commaSeparatedColumnAtHand.split(",");
            Card2 cardAtHand = new Card2(columnAtHamCards[0]);

            System.out.println("mylog card made" +columnAtHamCards);
            for (int j = 0; j < commaSeparatedColumns.size(); j++) {
                System.out.println("mylog second card");

                String commaSeparatedColumn = commaSeparatedColumns.get(j);
                String[] columnCards = commaSeparatedColumn.split(",");
                String lastCard = columnCards[columnCards.length-1];
                Card2 card = new Card2(lastCard);
                System.out.println("mylog secondcard made "+lastCard);

                if(canCardAtHandBeOnTopOfSecondCard(cardAtHand, card)){

                    // Here you have the answer: column nr i can be on top of column no j
                    System.out.println("mylog move found");
                    moves.add(new ColumnsMove(i+1, j+1));

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
