package org.tensorflow.lite.examples.detection.logic;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;


public class MovesFinderTest {

    @Test
    public void findMoves() {
        ArrayList<String> allCards = new ArrayList<>();
        allCards.clear();
        allCards.add("7S");
        allCards.add("7C");
        allCards.add("5D");
        allCards.add("8D");
        allCards.add("6D");
        allCards.add("7D");
        allCards.add("QH");
        allCards.add("KC");

        MovesFinder movesFinder = new MovesFinder();

        System.out.println("print all pt moves: " + movesFinder.findMoves(allCards));

        ArrayList expectedOutput  =new ArrayList<>(Arrays.asList("7S 8D, 7C 8D, 6D 7S, 6D 7C, QH KC"));
        ArrayList<CardsMove> methodOutput= movesFinder.findMoves(allCards) ;

        assertEquals("Test",expectedOutput,methodOutput);




    }
}
