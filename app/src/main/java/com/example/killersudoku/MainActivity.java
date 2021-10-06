package com.example.killersudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.example.views.RoundButtonView;
import com.example.views.Square;

import java.lang.reflect.Array;

//TODO setting the theme
//https://developer.android.com/reference/androidx/appcompat/app/AppCompatDelegate#setDefaultNightMode(int)
public class MainActivity extends AppCompatActivity {

    private Square[] squares = new Square[81];
    private Square selectedSquare;

    private RoundButtonView[] numberButtons = new RoundButtonView[9];

    private View.OnClickListener squareOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v instanceof Square){
                for(Square sq: squares){
                    if(sq.getId()==v.getId()){
                        sq.toggleSelected();
                        selectedSquare = sq;
                    }else{
                        sq.unselect();
                    }
                }
            }
        }
    };

    private View.OnClickListener numberButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v instanceof RoundButtonView){
                RoundButtonView button = (RoundButtonView) v;
                selectedSquare.setMainNumber(button.getButtonNumber());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ultra_sudoku_layout);

        Resources res = getResources();
        //Populate number buttons array
        int buttonIndex;
        for(buttonIndex = 0; buttonIndex<9; buttonIndex++){
            String buttonName = "roundButtonView" + (buttonIndex + 2);
            int buttonId = res.getIdentifier(buttonName,"id",getApplicationContext().getPackageName());
            RoundButtonView foundButton = (RoundButtonView)findViewById(buttonId);
            //Set click listener
            foundButton.setOnClickListener(numberButtonOnClickListener);
            numberButtons[buttonIndex] = (RoundButtonView)findViewById(buttonId);
        }
        //Populate squares array
        int index;
        for(index=0; index<81; index++){
            String squareName = "square" + (index + 11);
            int squareId = res.getIdentifier(squareName,"id",getApplicationContext().getPackageName());
            Square foundSquare = (Square)findViewById(squareId);
            //Set click listener
            foundSquare.setOnClickListener(squareOnClickListener);
            squares[index] = (Square)findViewById(squareId);
        }
    }

}