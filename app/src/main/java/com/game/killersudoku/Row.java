package com.game.killersudoku;

import androidx.annotation.Nullable;

import com.game.views.Square;

import java.util.Objects;

public class Row extends SquareCollection {
    int rowNumber;
    Row (int rowNumber){
        super();
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNumber);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj.getClass().equals(this.getClass()) && ((Row)obj).getRowNumber()==this.getRowNumber());
    }
}
