package com.game.killersudoku;

import com.game.views.Square;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class SquareCollection {
    TreeSet<Square> squares;

    public SquareCollection() { this.squares = new TreeSet<Square>(); }

    public void addSquare(Square square){
        this.squares.add(square);
    }



    /**
     * Get the answer values from the squares
     * @return
     */
    public HashSet<Integer> getValues(){
        HashSet<Integer> values = new HashSet<Integer>();
        for(Square sq: squares){
            Integer sqAnswer = sq.getAnswer();
            if(sqAnswer!= null){
                values.add(sqAnswer);
            }
        }
        return values;
    }

    public HashSet<Integer> getPossibleAnswers(){
        HashSet<Integer> result = new HashSet<>();
        for(Square square: squares){
            if(square.getAnswer()==null){
                result.addAll(square.possibleAnswers());
            }
        }
        return result;
    }

    /**
     * Checks values and removes values already contained from the set
     * @param possibles
     */
    public void removeDuplicates(HashSet<Integer> possibles){
        possibles.removeAll(this.getValues());
    }

    public int getNumberSquares(){
        return this.squares.size();
    }

    public TreeSet<Square> getSquares(){
        return squares;
    }

    /**
     * Returns true if the answer is already in the squares in this collection
     * @param answer
     * @return
     */
    public boolean answerInCollection(int answer){
        for(Square sq: this.squares){
            if(!(sq.getAnswer() == null) && sq.getAnswer()==answer){
                return true;
            }
        }
        return false;
    }

    public SortedSet<Square> getHeadSet(Square toSquare){
        return squares.headSet(toSquare);
    }

    public SortedSet<Square> getTailSetInclusive(Square fromSquare){
        return squares.tailSet(fromSquare);
    }

    public SortedSet<Square> getTailSetExclusive(Square notThisOne){
        TreeSet<Square> result = new TreeSet<>();
        Square higherSquare = squares.higher(notThisOne);
        if(higherSquare==null){
            return result;
        }
        return squares.tailSet(higherSquare);
    }

    public SortedSet<Square> getRestOfSet(Square notThisOne) {
        SortedSet<Square> results = new TreeSet<>();
        results.addAll(getHeadSet(notThisOne));
        results.addAll(getTailSetExclusive(notThisOne));
        return results;
    }

    public TreeSet<Square> getSquaresWithNoAnswer(){
        TreeSet<Square> result = new TreeSet<>();
        for(Square sq: squares){
            if(sq.getAnswer() == null){
                result.add(sq);
            }
        }
        return result;
    }

    public TreeSet<Square> getSquaresWithOneAnswer(){
        TreeSet<Square> result = new TreeSet<>();
        for(Square sq: squares){
            if(sq.getAnswer()==null && sq.possibleAnswers().size()==1){
                result.add(sq);
            }
        }
        return result;
    }

    public boolean containsAnswer(Integer answer){
        for(Square sq: squares){
            if(sq.getAnswer() != null && sq.getAnswer()==answer){
                return true;
            }
        }
        return false;
    }

}
