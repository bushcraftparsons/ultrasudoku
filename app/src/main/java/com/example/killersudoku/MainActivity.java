package com.example.killersudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.sudokusquare.view.Square;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ultra_sudoku_layout);
    }

    /** Called when the user taps the Send button */
    public void onClickSquare(View view) {
        // Do something in response to button
        ((Square)view).setMainNumber(9);
    }
}