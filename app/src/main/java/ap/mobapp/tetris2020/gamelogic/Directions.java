package ap.mobapp.tetris2020.gamelogic;

public enum Directions
{
    DOWN    (0),
    LEFT    (1),
    RIGHT   (2),
    ROTATE  (3);

    private int value;
    Directions(int value)
    {
        this.value = value;
    }
}
