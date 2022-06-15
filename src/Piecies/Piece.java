package Piecies;

public abstract class Piece {
    private Position pos;
    abstract public void move(Position startPos, Position finPos);
    abstract public boolean isValidMove(Position startPos, Position finPos);
}
