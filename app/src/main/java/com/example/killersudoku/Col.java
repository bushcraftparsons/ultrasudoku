package com.example.killersudoku;

import com.example.views.Square;

import java.util.HashSet;

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
