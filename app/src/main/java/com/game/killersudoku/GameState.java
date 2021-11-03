package com.game.killersudoku;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class GameState {
    int[] solution;

    @JsonGetter
    public int[] getSolution() {
        return solution;
    }

    @JsonSetter
    public void setSolution(int[] solution) {
        this.solution = solution;
    }
}
