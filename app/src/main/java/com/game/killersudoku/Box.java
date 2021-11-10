package com.game.killersudoku;

import androidx.annotation.Nullable;

import com.game.views.Square;

import java.util.HashSet;
import java.util.Objects;
import java.util.TreeSet;

public class Box extends SquareCollection{
    int boxNumber;
    public Box(int boxNumber) {
        super();
        this.boxNumber = boxNumber;
    }

    public int getBoxNumber() {
        return boxNumber;
    }

    public boolean allSquaresHavePossibleAnswer(){
        for(Square sq: squares){
            if(sq.getAnswer()==null){
                if(sq.possibleAnswers().size()==0){
                    return false;//Fail
                }
            }
        }
        return true;
    }

    public TreeSet<Square> getSquaresInRow(int rowNumber){
        TreeSet<Square> results = new TreeSet<>();
        for(Square sq: squares){
            if(sq.getRow().getRowNumber()==rowNumber){
                results.add(sq);
            }
        }
        return results;
    }

    public TreeSet<Square> getSquaresInRowWithNoAnswer(int rowNumber){
        TreeSet<Square> results = new TreeSet<>();
        for(Square sq: squares){
            if(sq.getRow().getRowNumber()==rowNumber && sq.getAnswer()==null){
                results.add(sq);
            }
        }
        return results;
    }

    public HashSet<Integer> getPossibleAnswersForRow(int rowNumber){
        HashSet<Integer> result = new HashSet<>();
        for(Square sq: squares){
            if(sq.getRow().getRowNumber()==rowNumber && sq.getAnswer()==null){
                result.addAll(sq.possibleAnswers());
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxNumber);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj.getClass().equals(this.getClass()) && ((Box)obj).getBoxNumber()==this.getBoxNumber());
    }
}
