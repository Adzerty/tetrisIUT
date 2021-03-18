package ap.mobapp.tetris2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ap.mobapp.tetris2020.gamelogic.Piece;
import ap.mobapp.tetris2020.gamelogic.PieceType;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void startDrawing(View v)
    {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }
}
