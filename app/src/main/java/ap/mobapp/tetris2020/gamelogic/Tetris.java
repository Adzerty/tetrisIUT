package ap.mobapp.tetris2020.gamelogic;

import android.util.Log;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import ap.mobapp.tetris2020.GameActivity;

public class Tetris
{
    private static final Tetris tetrisSingleton = new Tetris();
    private GameActivity act;

    private Board board;
    private int actualScore;

    private boolean isGameActive;
    private boolean isPieceFalling = false;
    private boolean gameLost;

    private Tetris()
    {}

    public static Tetris getTetrisSingleton()
    {
        return tetrisSingleton;
    }

    public void setAct(GameActivity act)
    {
        this.act = act;
    }

    public void setBoard(Board board)
    {
        this.board = board;
    }

    ///TEST
    public void startGame()
    {
        this.isGameActive = true;


        new Timer().scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                Log.d("STATE","Refreh : " + gameLost);
                if(!gameLost)
                {

                    if(isGameActive)
                    {

                        //JEU NON EN PAUSE

                        /* TRAITEMENT GERE PAR CETTE CLASSE */
                        //On genere une piece au hasard qui tombe
                        // Si la piece ne peut meme pas etre generee on ne fait pas tous les autres calculs

                        //On verifie que la partie n'est pas perdue (le bloc n'est pas affiche completement)
                        //On verifie qu'on a pas deja ajouté une piece
                        if( !tetrisSingleton.isPieceFalling  && !tetrisSingleton.generatePiece())
                        {
                            Log.d("STATE","Une piece est générée");
                            tetrisSingleton.isPieceFalling = true;
                            //gameLost = true;
                            //On fait tomber la piece

                        }

                        if(! tetrisSingleton.board.actualiserTableau(Directions.DOWN))
                        {
                            Log.d("DEBUG", "MARQUEUR 1");
                            tetrisSingleton.isPieceFalling = false;
                        }


                        //tetrisSingleton.isPieceFalling = true;
                        //Une fois quelle est tombée on verifie si des lignes sont pleines
                        //on calcul les points selon le nombre de lignes detruites
                        /*
                            1 ligne         = 100pts
                            2 lignes        = 300pts
                            3 lignes        = 700pts
                            4 lignes        = 1500pts
                         */
                    }
                    else
                    {
                        //JEU EN PAUSE
                    }
                }
                //JEU PERDU
            }
        },100,750);
    }

    private boolean generatePiece()
    {
        return this.board.generatePiece();
    }
}
