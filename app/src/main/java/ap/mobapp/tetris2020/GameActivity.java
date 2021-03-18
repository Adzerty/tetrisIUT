package ap.mobapp.tetris2020;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ap.mobapp.tetris2020.gamelogic.Board;
import ap.mobapp.tetris2020.gamelogic.Carre;
import ap.mobapp.tetris2020.gamelogic.Tetris;

public class GameActivity extends AppCompatActivity
{
    private ViewBoard plateau;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.plateau = new ViewBoard(this, this);
        this.plateau.setId(R.id.idViewBoard);
        LinearLayout layout = findViewById(R.id.layoutPlateau);
        layout.addView(plateau,1);

    }

    public void goLeft(View v)
    {
        this.plateau.goLeft();
    }

    public void goRight(View v)
    {
        this.plateau.goRight();
    }

    public void exitApp()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}


