package ap.mobapp.tetris2020.gamelogic;

public class Carre
{
    private int color;
    private boolean isFixed;
    private boolean isBlockedLeft;
    private boolean isBlockedRight;

    private int x, y;

    private Piece motherPiece;

    public Carre(int color, Piece motherPiece)
    {
        this.color = color;
        this.isFixed = false;
        this.isBlockedLeft = false;
        this.isBlockedRight = false;
        this.motherPiece = motherPiece;
    }

    public void setCoord(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Piece getMotherPiece()
    {
        return motherPiece;
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

    public void setBlockedLeft(boolean b)
    {
        this.isBlockedLeft = b;
    }

    public void blockedAllPieceSquareLeft(boolean b)
    {
        this.motherPiece.blockedAllSquareLeft(b);
    }

    public boolean isBlockedLeft()
    {
        return isBlockedLeft;
    }

    public void setBlockedRight(boolean b)
    {
        this.isBlockedRight = b;
    }

    public void blockedAllPieceSquareRight(boolean b)
    {
        this.motherPiece.blockedAllSquareRight(b);
    }

    public boolean isBlockedRight()
    {
        return isBlockedRight;
    }

    public int getColor()
    {
        return color;
    }
}
