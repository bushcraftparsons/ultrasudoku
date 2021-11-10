package com.game.killersudoku;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class GameState {
    int[] solution;
    int[] shownSquares;
    String level;

    @JsonGetter
    public int[] getSolution() {
        return solution;
    }

    @JsonSetter
    public void setSolution(int[] solution) {
        this.solution = solution;
    }

    @JsonGetter
    public int[] getShownSquares() {
        return shownSquares;
    }

    @JsonGetter
    public void setShownSquares(int[] shownSquares) {
        this.shownSquares = shownSquares;
    }

    @JsonGetter
    public String getLevel() {
        return level;
    }

    @JsonGetter
    public void setLevel(String level) {
        this.level = level;
    }
}
