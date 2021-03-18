package ap.mobapp.tetris2020.gamelogic;

public class Carre
{
    private int color;
    private boolean isFixed;
    private Piece motherPiece;
    public Carre(int color, Piece motherPiece)
    {
        this.color = color;
        this.isFixed = false;
        this.motherPiece = motherPiece;
    }

    public void setFixed(boolean b)
    {
        this.isFixed = b;
    }

    public void fixedAllPieceSquare(boolean b)
    {
        this.motherPiece.fixedAllSquare(b);
    }

    public boolean isFixed()
    {
        return isFixed;
    }

    public int getColor()
    {
        return color;
    }
}
