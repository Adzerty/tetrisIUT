package ap.mobapp.tetris2020;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import ap.mobapp.tetris2020.gamelogic.Board;
import ap.mobapp.tetris2020.gamelogic.Carre;
import ap.mobapp.tetris2020.gamelogic.Directions;
import ap.mobapp.tetris2020.gamelogic.Tetris;

public class ViewBoard extends View
{
    private GameActivity act;

    Board b;
    Tetris tetris;
    float x,y;


    public ViewBoard(Context context, GameActivity act)
    {
        super(context);
        this.act = act;

        //Récupération de la taille de l'écran
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        this.b = new Board(width,height, this);

        this.tetris = Tetris.getTetrisSingleton();
        this.tetris.setAct(act);
        this.tetris.setBoard(b);

        setFocusable(true);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(b.WIDTH,b.HEIGHT);
        params.gravity = Gravity.CENTER;
        this.setLayoutParams(params);

        this.tetris.startGame();

    }



    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.BLACK);

        Carre[][] boardGrille = b.getGrille();
        for(int i = 0; i<boardGrille.length; i++)
        {
            for(int j = 0; j<boardGrille[i].length; j++)
            {
                Carre carreTmp = boardGrille[i][j];
                if(carreTmp != null)
                {
                    Paint carrePaint = new Paint();
                    carrePaint.setStyle(Paint.Style.FILL);
                    carrePaint.setColor(carreTmp.getColor());
                    canvas.drawRect(j*b.getCUBE_SIZE(),i*b.getCUBE_SIZE(), j*b.getCUBE_SIZE() + b.getCUBE_SIZE(), i*b.getCUBE_SIZE()+b.getCUBE_SIZE(), carrePaint);

                    carrePaint.setStyle(Paint.Style.STROKE);
                    carrePaint.setColor(Color.WHITE);
                    carrePaint.setStrokeWidth(8);
                    canvas.drawRect(j*b.getCUBE_SIZE(),i*b.getCUBE_SIZE(), j*b.getCUBE_SIZE() + b.getCUBE_SIZE(), i*b.getCUBE_SIZE()+b.getCUBE_SIZE(), carrePaint);

                    //L T R B

                }

            }
        }

    }

    public void goLeft()
    {
        b.actualiserTableau(Directions.LEFT);
    }

    public void goRight()
    {
        b.actualiserTableau(Directions.RIGHT);
    }

    public void refreshCanvas()
    {
        this.invalidate();
    }

    public GameActivity getAct()
    {
        return act;
    }
}
