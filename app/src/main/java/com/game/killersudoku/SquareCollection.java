package com.game.killersudoku;

import com.game.views.Square;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class SquareCollection {
    TreeSet<Square> squares;

    public SquareCollection() { this.squares = new TreeSet<>(); }

    public void addSquare(Square square){
        this.squares.add(square);
    }



    /**
     * Get the answer values from the squares
     * @return Returns all the values which could be used in the solution for this collection of squares
     */
    public HashSet<Integer> getValues(){
        HashSet<Integer> values = new HashSet<>();
        for(Square sq: squares){
            Integer sqAnswer = sq.getAnswer();
            if(sqAnswer!= null){
                values.add(sqAnswer);
            }
        }
        return values;
    }

    public HashSet<Integer> getCalculableAnswers(Square notThisSquare){
        HashSet<Integer> calculableAnswers = new HashSet<>();
        for(Square sq: squares){
            if(sq.getSquareIndex()==notThisSquare.getSquareIndex()){
                continue;//Don't do this square
            }
            Integer calculableAnswer = sq.getCalculableAnswer();
            if(calculableAnswer!= null){
                calculableAnswers.add(calculableAnswer);
            }
        }
        return calculableAnswers;
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
     * @param possibles the possible values for an answer from which duplicates are to be removed
     */
    public void removeDuplicates(HashSet<Integer> possibles){
        possibles.removeAll(this.getValues());
    }

    /**
     *
     * @param possibles The possible values for the solution
     */
    public void removeCalculableAnswers(HashSet<Integer> possibles, Square sq){
        possibles.removeAll(this.getCalculableAnswers(sq));
    }

    public boolean isLastSolution(Integer solution){
        int solutionCount = 0;
        for(Square sq: squares){
            if(sq.possibleSolutions().contains(solution)){
                solutionCount++;
                if(solutionCount>1){
                    return false;
                }
            }
        }
        return true;
    }

    public int getNumberSquares(){
        return this.squares.size();
    }

    public TreeSet<Square> getSquares(){
        return squares;
    }

    /**
     * Returns true if the answer is already in the squares in this collection
     * @param answer a value for the answer we are checking for
     * @return Returns true if the answer is already in the squares in this collection
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
            if(sq.getAnswer() != null && sq.getAnswer().equals(answer)){
                return true;
            }
        }
        return false;
    }

    public HashSet<Box> getBoxes(){
        HashSet<Box> boxes = new HashSet<>();
        for(Square sq: squares){
            boxes.add(sq.getBox());
        }
        return boxes;
    }

    public HashSet<Row> getRows(){
        HashSet<Row> rows = new HashSet<>();
        for(Square sq: squares){
            rows.add(sq.getRow());
        }
        return rows;
    }

    public HashSet<Col> getCols(){
        HashSet<Col> cols = new HashSet<>();
        for(Square sq: squares){
            cols.add(sq.getCol());
        }
        return cols;
    }

    public void showSolution(Integer solution){
        for(Square sq: squares){
            if(sq.getAnswer().equals(solution)){
                if(sq.isCalculable()){
                    //No need to show answer, it is calculable
                    return;
                }
                sq.showAnswer();
                return;
            }
        }
    }

}
