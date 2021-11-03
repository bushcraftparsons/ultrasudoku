package com.game.killersudoku;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.killersudoku.R;

public class NewGame extends AppCompatActivity {
    public static final String GAME_LEVEL = "com.game.ultrasudoku.LEVEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
    }

    public void selectLevel(View view) {
        if(view instanceof Button){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(GAME_LEVEL, ((Button)view).getText());
            startActivity(intent);
        }
    }
}