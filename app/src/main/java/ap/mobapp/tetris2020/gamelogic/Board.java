package ap.mobapp.tetris2020.gamelogic;

import android.util.Log;

import java.util.Random;

import ap.mobapp.tetris2020.ViewBoard;


public class Board
{
    private ViewBoard vB;
    //Tableau 2D de Carre qui représente les couleurs à afficher, -1 si vide
    private Carre[][] grille;

    //TAILLE
    public int CUBE_SIZE;
    public final int ROWS = 20;
    public final int COLS = 15;
    public int HEIGHT;
    public int WIDTH;

    //INDICES D'APPARITION DES PIECES
    private int spawnX;
    private int spawnY = 0;

    //TABLEAUX DE PIECES TYPES
    private PieceType[] tabPieceType = new PieceType[]{PieceType.TypeI,PieceType.TypeJ,PieceType.TypeL,PieceType.TypeO,PieceType.TypeS,PieceType.TypeT,PieceType.TypeZ};

    public Board(int width, int height, ViewBoard vB)
    {
        this.vB = vB;
        //Calcul de la taille en fonction de la taille de l'écran de l'utilisateur
        boolean optimalSizeFound = false;
        int tailleCube = 20; //On part d'une taille de cube initiale à 20
        while(! optimalSizeFound)
        {
            if(width/tailleCube > COLS && (height-100)/tailleCube > ROWS)
            {
                tailleCube += 10;
            }else
            {
                this.CUBE_SIZE = tailleCube;
                optimalSizeFound = true;
            }
        }

        this.HEIGHT = ROWS * CUBE_SIZE;
        this.WIDTH  = COLS * CUBE_SIZE;

        this.spawnX = COLS/2;

        this.grille = new Carre[ROWS][COLS];
        for(int i = 0; i<ROWS; i++)
        {
            for(int j = 0; j<COLS; j++)
            {
                this.grille[i][j] = null;
            }
        }
    }

    public boolean generatePiece()
    {
        Random random = new Random();
        int nb;
        nb = random.nextInt(this.tabPieceType.length);
        Piece p = new Piece(this.tabPieceType[nb]);




        boolean gameLost =  addPieceToBoard(p);
        return false;
    }

    public boolean addPieceToBoard(Piece piece)
    {

        boolean gameLost = false;
        boolean[][] arrBooleanPiece = piece.getRotationArray();

        int trueSpawnX = spawnX - arrBooleanPiece[0].length/2;

        int indCarreTmp = 0;
        Carre[] ensCarreTmp = new Carre[4];

        for(int i = 0; i<arrBooleanPiece.length; i++)
        {
            for(int j = 0; j<arrBooleanPiece.length; j++)
            {
                if(arrBooleanPiece[i][j])
                {

                    if(grille[i][trueSpawnX + j] == null)
                    {
                        Carre carreTmp = new Carre(piece.getColor(), piece);
                        grille[i][trueSpawnX + j] = carreTmp;
                        carreTmp.setCoord(j,i);
                        ensCarreTmp[indCarreTmp++] = carreTmp;
                    }else
                    {
                        gameLost = true;
                    }
                }
            }
        }

        piece.setEnsCarre(ensCarreTmp.clone());
        return gameLost;
    }

    public boolean actualiserTableau(Directions direction)
    {
        //Methode qui permet l'actualisation du tableau de Carre "grille"
        boolean isActualizing = false;

        if(direction == Directions.DOWN)
        {
            for (int i = grille.length-1; i >= 1; i--)
            {
                for (int j = grille[i].length - 1; j >= 0; j--)//On parcours le tableau à l'envers
                {
                    //On recupere le carre cible
                    Carre carreTmp = grille[i][j];

                    //On regarde si le carre du dessus est null et on descend
                    if (grille[i - 1][j] != null)
                    {
                        if (this.grille[i][j] == null && !this.grille[i - 1][j].isFixed())
                        {
                            this.grille[i][j] = this.grille[i - 1][j];
                            this.grille[i][j].setCoord(j,i);
                            this.grille[i - 1][j] = null;
                        }
                        else
                        {
                            if(j>0 && carreTmp != null)
                            {
                                carreTmp.blockedAllPieceSquareLeft(grille[i][j-1] != null && grille[i][j-1].isFixed());
                            }
                            if(j<grille.length-1 && carreTmp != null)
                            {
                                carreTmp.blockedAllPieceSquareRight(grille[i][j+1] != null && grille[i][j+1].isFixed());
                            }
                        }
                    }
                }
            }

            for (int i = grille.length-1; i > 0; i--)
            {
                for (int j = grille[i].length-1; j > 0; j--)//On parcours le tableau à l'envers
                {
                    if(this.grille[i][j] != null && !this.grille[i][j].isFixed())
                    {
                        if (i == grille.length - 1)//Si on est en bas de la grille
                        {
                            this.grille[i][j].fixedAllPieceSquare(true);
                        }
                        else //Si le carre du dessous n'est pas null on fixe
                        {
                            if (this.grille[i+1][j] != null && this.grille[i+1][j].isFixed())
                            {
                                this.grille[i][j].fixedAllPieceSquare(true);
                            }
                        }
                    }
                }
            }
        }

        if(direction == Directions.LEFT)
        {
            Carre toBlock = null;
            for (int i = 0; i < grille.length; i++)
            {
                for (int j = 1; j < grille[i].length; j++)
                {
                    //On recupere le carre cible
                    Carre carreTmp = grille[i][j];

                    if(carreTmp != null && !carreTmp.isFixed() && !carreTmp.isBlockedLeft() && pieceCanGoLeft(carreTmp))
                    {
                        carreTmp.blockedAllPieceSquareRight(false);
                        if(grille[i][j-1] == null)
                        {
                            grille[i][j-1] = carreTmp;
                            this.grille[i][j-1].setCoord(j-1,i);
                            grille[i][j] = null;

                            if(j-1 == 0)
                            {
                                toBlock = grille[i][j-1];
                            }
                        }
                    }
                }
            }
            if(toBlock != null)
            {
                toBlock.blockedAllPieceSquareLeft(true);
                toBlock = null;
            }
        }

        if(direction == Directions.RIGHT)
        {
            Carre toBlock = null;
            for (int i = 0; i < grille.length; i++)
            {
                for (int j = grille[i].length-2; j >= 0; j--)
                {
                    //On recupere le carre cible
                    Carre carreTmp = grille[i][j];

                    if(carreTmp != null && !carreTmp.isFixed() && pieceCanGoRight(carreTmp))
                    {
                        carreTmp.blockedAllPieceSquareLeft(false);
                        if(grille[i][j+1] == null)
                        {
                            grille[i][j+1] = carreTmp;
                            this.grille[i][j+1].setCoord(j+1,i);
                            grille[i][j] = null;
                            if(j+1 == grille[i].length-1)
                            {
                                toBlock = grille[i][j+1];
                            }
                        }
                    }

                }
            }
            if(toBlock != null)
            {
                toBlock.blockedAllPieceSquareRight(true);
                toBlock = null;
            }
        }

        //On reparcours une fois le tableau et on vérifie que tout est fixe
        for(int i = 0; i<grille.length; i++)
        {
            for (int j = 0; j < grille[i].length; j++)
            {
                if(grille[i][j] != null)
                {
                    if(! grille[i][j].isFixed()) isActualizing = true;
                }
            }
        }

        this.vB.refreshCanvas();
        return isActualizing;
    }

    public Carre[][] getGrille()
    {
        return grille;
    }

    public int getCUBE_SIZE()
    {
        return CUBE_SIZE;
    }

    private boolean pieceCanGoLeft(Carre carre)
    {
        Piece pieceOfSquare = carre.getMotherPiece();
        Carre[] ensCarre = pieceOfSquare.getEnsCarre();

        return  !ensCarre[0].isBlockedLeft() &&
                !ensCarre[1].isBlockedLeft() &&
                !ensCarre[2].isBlockedLeft() &&
                !ensCarre[3].isBlockedLeft();
    }

    private boolean pieceCanGoRight(Carre carre)
    {
        Piece pieceOfSquare = carre.getMotherPiece();
        Carre[] ensCarre = pieceOfSquare.getEnsCarre();

        return  !ensCarre[0].isBlockedRight() &&
                !ensCarre[1].isBlockedRight() &&
                !ensCarre[2].isBlockedRight() &&
                !ensCarre[3].isBlockedRight();
    }
}
