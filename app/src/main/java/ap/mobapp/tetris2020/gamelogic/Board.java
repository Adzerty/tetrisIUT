package ap.mobapp.tetris2020.gamelogic;

import android.util.Log;

import androidx.constraintlayout.solver.widgets.analyzer.Direct;

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
    public final int COLS = 12;
    public int HEIGHT;
    public int WIDTH;

    //INDICES D'APPARITION DES PIECES
    private int spawnX;
    private int spawnY = 0;

    //TABLEAUX DE PIECES TYPES
    private PieceType[] tabPieceType = new PieceType[]{PieceType.TypeI,PieceType.TypeJ,PieceType.TypeL,PieceType.TypeO,PieceType.TypeS,PieceType.TypeT,PieceType.TypeZ};

    private Piece movingPiece;
    private boolean pieceCanGo;

    //Booleen qui sert a verrouiller le deplacement gauche/droite en cas de calcul de ligne a retirer
    private boolean verrouiller = false;

    public Board(int width, int height, ViewBoard vB)
    {
        this.vB = vB;
        //Calcul de la taille en fonction de la taille de l'écran de l'utilisateur
        boolean optimalSizeFound = false;
        int tailleCube = 20; //On part d'une taille de cube initiale à 20
        while(! optimalSizeFound)
        {
            if(width/tailleCube > COLS && (height-220)/tailleCube > ROWS)
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

        movingPiece = p;
        boolean gameLost =  addPieceToBoard(p);
        return false;
    }

    public boolean addPieceToBoard(Piece piece)
    {

        boolean gameLost = false;
        boolean[][] arrBooleanPiece = piece.getRotationArray();

        int trueSpawnX = spawnX - arrBooleanPiece[0].length/2;
        piece.setX(trueSpawnX);
        piece.setY(0);

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
                        carreTmp.setCoord(trueSpawnX + j,i);
                        ensCarreTmp[indCarreTmp++] = carreTmp;
                    }else
                    {
                        gameLost = true;
                    }
                }
            }
        }

        piece.setEnsCarre(ensCarreTmp.clone());
        verrouiller = false;
        return gameLost;
    }

    public boolean actualiserTableau(Directions direction)
    {
        //Methode qui permet l'actualisation du tableau de Carre "grille"
        boolean isActualizing = false;

        if(direction == Directions.DOWN)
        {
            for (int i = grille.length-1; i > 0; i--)
            {
                for (int j = grille[i].length-1; j >= 0; j--)//On parcours le tableau à l'envers
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

            for (int i = grille.length-1; i >= 1; i--)
            {
                for (int j = grille[i].length - 1; j >= 0; j--)//On parcours le tableau à l'envers
                {
                    //On recupere le carre cible
                    Carre carreTmp = grille[i][j];

                    //On regarde si le carre du dessus est pas null et on descend
                    if (grille[i - 1][j] != null)
                    {
                        //Si le carre cible est vide et que le carre du dessus n'est pas fixe
                        if (carreTmp == null && !this.grille[i - 1][j].isFixed())
                        {
                            if(! movingPiece.hasMoved())
                            {
                                movingPiece.setHasMoved(true);
                                movingPiece.setY(movingPiece.getY()+1);
                            }
                            this.grille[i][j] = this.grille[i - 1][j];
                            this.grille[i][j].setCoord(j,i);
                            this.grille[i - 1][j] = null;
                        }
                        else //Sinon
                        {
                            if(j>0 && carreTmp != null && !carreTmp.isFixed())
                            {
                                carreTmp.blockedAllPieceSquareLeft(grille[i][j-1] != null && grille[i][j-1].isFixed());
                            }
                            if(j<grille.length-1 && carreTmp != null && !carreTmp.isFixed())
                            {
                                carreTmp.blockedAllPieceSquareRight(grille[i][j+1] != null && grille[i][j+1].isFixed());
                            }
                        }
                    }
                }
            }
        }

        if(direction == Directions.LEFT)
        {
            if(! verrouiller)
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
                            //La premiere piece bouge mais est bloque, donc le reste ne peux plus.
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


        }

        if(direction == Directions.RIGHT)
        {
            if(! verrouiller)
            {
                Carre toBlock = null;
                for (int i = 0; i < grille.length; i++)
                {
                    for (int j = grille[i].length-2; j >= 0; j--)
                    {
                        //On recupere le carre cible
                        Carre carreTmp = grille[i][j];

                        if(carreTmp != null && !carreTmp.isFixed() && !carreTmp.isBlockedRight() && pieceCanGoRight(carreTmp))
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

        }


        if(direction == Directions.ROTATE)
        {
            if(! verrouiller)
            {
                //On recupere le nouveau tableau de rotation
                movingPiece.setRotation( (movingPiece.getRotation()+1)%4 );
                boolean[][] rotArrayTmp = movingPiece.getRotationArray();

                boolean canRotate = true;

                //On verifie dabord que la piece puisse tourner
                for(int i = 0; i<rotArrayTmp.length;i++)
                {
                    for(int j = 0; j<rotArrayTmp[i].length; j++)
                    {
                        if(rotArrayTmp[i][j]) //Si il doit y avoir un carre ici (suite à la rotation)
                        {
                            int pieceY = movingPiece.getY();
                            int pieceX = movingPiece.getX();
                            if( (i+pieceY<grille.length-1) && (j+pieceX<grille[i+pieceY].length-1) && i+pieceY>=0 && j+pieceX>=0 ) //Si la nouvelle piece ne sort pas de la grille
                            {
                                //Si la nouvelle piece est place sur une case vide OU sur une case avec un ancien carre de la piece
                                if(! (grille[i+pieceY][j+pieceX] == null || (grille[i+pieceY][j+pieceX] != null && !grille[i+pieceY][j+pieceX].isFixed())))
                                {
                                    canRotate = false;
                                }
                            }
                            else
                            {
                                canRotate = false;
                            }
                        }
                    }
                }
                //Si la piece peut tourner on le fait
                if(canRotate)
                {
                    int indCarreTmp = 0;
                    Carre[] ensCarreTmp = new Carre[4];

                    for(int i = 0; i<rotArrayTmp.length;i++)
                    {
                        for (int j = 0; j < rotArrayTmp[i].length; j++)
                        {
                            int pieceY = movingPiece.getY();
                            int pieceX = movingPiece.getX();
                            if(grille[i+pieceY][j+pieceX] != null && grille[i+pieceY][j+pieceX].getMotherPiece() == movingPiece)
                            {
                                grille[i+pieceY][j+pieceX] = null;
                            }

                            if(rotArrayTmp[i][j])
                            {
                                Carre carreTmp = new Carre(movingPiece.getColor(), movingPiece);
                                grille[i+pieceY][j+pieceX] = carreTmp;
                                carreTmp.setCoord(j+pieceX,i+pieceY);
                                ensCarreTmp[indCarreTmp++] = carreTmp;
                            }

                        }
                    }

                    movingPiece.setEnsCarre(ensCarreTmp.clone());
                    //On bloque ou non la piece sur les cot
                    for(Carre c : ensCarreTmp)
                    {
                        if(c.getX() == 0)
                        {
                            c.blockedAllPieceSquareLeft(true);
                        }
                        if(c.getX() == grille[0].length-1)
                        {
                            c.blockedAllPieceSquareRight(true);
                        }
                    }
                }
                else
                {
                    movingPiece.setRotation((movingPiece.getRotation()-1)%4);
                }
            }

        }

        movingPiece.setHasMoved(false);

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

    public int checkForCompleteLines()
    {
        verrouiller = true;
        int scoreToReturn=0;
        for (int i = grille.length-1; i>=0; i--)
        {
            int nbOfCarres=grille[i].length;
            for (int j=grille[i].length-1; j>=0;j--)
            {
                if (grille[i][j] != null)
                {
                    nbOfCarres--;

                }
            }
            if(nbOfCarres==0)
            {
                scoreToReturn=scoreToReturn*2+100;
                removeLine(i);
                i++;
            }
        }
        return scoreToReturn;
    }

    private void removeLine(int lineToRemove)
    {

        for(int j=0; j<grille[lineToRemove].length;j++)
        {
            grille[lineToRemove][j]=null;
        }


        if(lineToRemove>0)
        {
            for(int i = lineToRemove-1; i>=0; i--)
            {
                for(int j = grille[i].length-1; j>=0; j--)
                {
                    if(grille[i][j] != null)
                        grille[i][j].setFixed(false);
                }
            }
        }

        this.actualiserTableau(Directions.DOWN);
        for(int i = lineToRemove-1; i>=0; i--)
        {
            for(int j = grille[i].length-1; j>=0; j--)
            {
                if(grille[i][j] != null)
                    grille[i][j].setFixed(true);
            }
        }
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
        if(pieceOfSquare.hasMoved())return true;
        Carre[] ensCarre = pieceOfSquare.getEnsCarre();

        pieceCanGo = true;
        for(Carre carreTmp : ensCarre)
        {
            int i = carreTmp.getY();
            int j = carreTmp.getX();

            if(j>0 && grille[i][j-1] != null && grille[i][j-1].getMotherPiece() != pieceOfSquare)
                pieceCanGo = false;
        }
        if(pieceCanGo)
        {
            pieceOfSquare.setX(pieceOfSquare.getX()-1);
            pieceOfSquare.setHasMoved(true);
        }
        return pieceCanGo;
    }

    private boolean pieceCanGoRight(Carre carre)
    {
        Piece pieceOfSquare = carre.getMotherPiece();
        if(pieceOfSquare.hasMoved())return true;
        Carre[] ensCarre = pieceOfSquare.getEnsCarre();

            pieceCanGo = true;
            for (Carre carreTmp : ensCarre)
            {
                int i = carreTmp.getY();
                int j = carreTmp.getX();

                if (j < grille[i].length - 1 && grille[i][j + 1] != null && grille[i][j + 1].getMotherPiece() != pieceOfSquare)
                    pieceCanGo = false;
            }
        if(pieceCanGo)
        {
            pieceOfSquare.setX(pieceOfSquare.getX()+1);
            pieceOfSquare.setHasMoved(true);
        }
        return pieceCanGo;
    }

    public boolean isVerrouiller()
    {
        return verrouiller;
    }
}
