package ap.mobapp.tetris2020.gamelogic;

import android.graphics.Color;

import androidx.annotation.NonNull;

public class Piece
{
    private PieceType pT;
    private int color;
    private int x,y,rotation;
    private Carre[] ensCarre = new Carre[4];

    public Piece ( PieceType pT)
    {
        this.pT = pT;
        this.color = pT.getColor();

    }

    public boolean[][] getRotationArray()
    {
        boolean[] rotArray = this.pT.tiles[rotation];
        boolean[][] arrayRet = new boolean[this.pT.rows][this.pT.cols];

        int indRotArray=0;
        for(int i = 0; i<arrayRet.length; i++)
        {
            for(int j = 0; j<arrayRet[i].length; j++)
            {
                arrayRet[i][j] = rotArray[indRotArray++];
            }
        }

        return arrayRet;
    }

    public int getColor()
    {
        return color;
    }

    public void fixedAllSquare(boolean b)
    {
        for(int i = 0; i<ensCarre.length; i++ )
        {
            ensCarre[i].setFixed(true);
        }
    }

    public void setEnsCarre(Carre[] ensCarre)
    {
        this.ensCarre = ensCarre;
    }

    @NonNull
    @Override
    public String toString()
    {
        String sRet = "Couleur = ";

        sRet += this.getColor() + '\n';

        boolean[][] array = getRotationArray();
        for(int i = 0; i<array.length; i++)
        {
            for(int j = 0; j<array[i].length; j++)
            {
                sRet+= array[i][j] + " | ";
            }
            sRet += '\n';
        }
        sRet += "- - -- - - - - - - - \n";

        for(int i = 0; i<ensCarre.length; i++)
        {
            sRet+= ensCarre[i] + " | ";
        }
        sRet += '\n';
        return sRet;
    }
}



