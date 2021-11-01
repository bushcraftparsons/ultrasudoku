package com.game.killersudoku;

public class Row extends SquareCollection {
    int rowNumber;
    Row (int rowNumber){
        super();
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

}
