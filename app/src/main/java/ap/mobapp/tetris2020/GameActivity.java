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
import android.widget.TextView;
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
    private TextView tvScore;
    private int score = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.plateau = new ViewBoard(this, this);
        this.plateau.setId(R.id.idViewBoard);

        this.tvScore = (TextView) findViewById(R.id.tvScore);
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
    public void goDown(View v)
    {
        this.plateau.goDown();
    }

    public void rotate(View v)
    {
        this.plateau.rotate();
    }

    public void setScore(final int score)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                tvScore.setText(score + " points ");
            }
        });

    }

    public void exitApp()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}


