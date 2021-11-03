package com.game.killersudoku;

import android.content.Context;
import android.content.res.Resources;

import com.example.killersudoku.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

public final class Utils {

    private Utils () { // private constructor
    }

    public static <T> T getRandomItemFromSet(HashSet<T> mySet){
        int maxIndex = mySet.size()-1;
        long randomIndex = Math.round(Math.random() * maxIndex);
        int i = 0;
        for(T obj : mySet)
        {
            if (i == randomIndex)
                return obj;
            i++;
        }
        return null;
    }

    public static String printHashSet(HashSet mySet){
        String result = "{";
        for(Object o: mySet){
            result = result + o.toString() + ",";
        }
        result = result + "}";
        return result;
    }

    public static void persistGameState(GameState gs, Resources res, File dir) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File gameStateFile = new File(dir, res.getString(R.string.game_state_file));
            gameStateFile.createNewFile(); // if file already exists will do nothing
            objectMapper.writeValue(gameStateFile, gs);
        } catch (IOException e) {
            System.out.println("Error trying to write to file: " + e.toString());
        }
    }

    public static GameState retrieveGameState(Resources res, File dir) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File gameStateFile = new File(dir, res.getString(R.string.game_state_file));
            gameStateFile.createNewFile(); // if file already exists will do nothing
            return objectMapper.readValue(gameStateFile, GameState.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
