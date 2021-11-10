package com.game.killersudoku;

import com.game.views.Square;

import java.util.HashSet;
import java.util.PrimitiveIterator;
import java.util.Random;

public class GameMaker {
    private final Square[] squares;
    private final Row[] rows;
    private final Col[] cols;
    private final Box[] boxes;
    private final PrimitiveIterator.OfDouble randomDoublesIterator;

    public GameMaker(Square[] squares, Row[] rows, Col[] cols, Box[] boxes) {
        this.squares = squares;
        this.boxes = boxes;
        this.cols = cols;
        this.rows = rows;
        Random rndm = new Random();
        randomDoublesIterator = rndm.doubles().iterator();
    }

    /**
     * Squares which can't be calculated from the board so far
     * @return
     */
    private HashSet<Square> getIncalculables(){
        HashSet<Square> incalculables = new HashSet<>();
        for(Square sq: squares){
            if(!sq.isCalculable()){
                incalculables.add(sq);
            }
        }
        return incalculables;
    }

    /**
     * This is the only square in the row, col or box with this answer
     * @param sq
     */
    private void makeHiddenSingle(Square sq){
        //Make it the only square in the row and col with the answer
        HashSet<Box> boxesInRow = sq.getRow().getBoxes();
        HashSet<Box> boxesInCol = sq.getCol().getBoxes();
        //Remove the box with this square
        boxesInRow.remove(sq.getBox());
        boxesInCol.remove(sq.getBox());
        //Now show the solution in the remaining boxes
        for(Box bx: boxesInRow){
            bx.showSolution(sq.getAnswer());
        }
        for(Box bx: boxesInCol){
            bx.showSolution(sq.getAnswer());
        }
        sq.setCalculable(true);
    }

    /**
     * Make an easy level game with only naked and hidden singles
     */
    public void makeEasy(){
        HashSet<Square> incalculables = getIncalculables();
        while(incalculables.size()>0){
            //Pick a random square from the set of incalculables.
            Square random = Utils.getRandomItemFromSet(incalculables);
            //Just making hidden singles, naked singles occur naturally
            makeHiddenSingle(random);
            incalculables = getIncalculables();
        }

    }
}
