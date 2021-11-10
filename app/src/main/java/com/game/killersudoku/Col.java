package com.game.killersudoku;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Col extends SquareCollection{
    int colNumber;
    public Col(int colNumber) {
        super();
        this.colNumber=colNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(colNumber);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj.getClass().equals(this.getClass()) && ((Col)obj).getColNumber()==this.getColNumber());
    }
}
