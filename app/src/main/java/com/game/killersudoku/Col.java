package com.game.killersudoku;

public class Col extends SquareCollection{
    int colNumber;
    public Col(int colNumber) {
        super();
        this.colNumber=colNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

}
