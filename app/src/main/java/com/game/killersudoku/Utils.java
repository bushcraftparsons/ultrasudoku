package com.game.killersudoku;

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
}
