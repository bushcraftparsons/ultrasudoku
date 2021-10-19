package com.example.killersudoku;

import com.example.views.Square;

import java.util.HashSet;
import java.util.TreeSet;

public class Row extends SquareCollection{
    int rowNumber;
    Row (int rowNumber){
        super();
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

}
