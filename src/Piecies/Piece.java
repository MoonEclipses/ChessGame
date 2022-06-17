package Piecies;

import Game.Board;
import Game.Color;

import java.util.Set;

public abstract class Piece {
    protected Position pos;
    protected Color color;
    protected Position[] movingOffset;

    public Piece(Position pos, Color color) {
        this.pos = pos;
        this.color = color;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public abstract boolean move(Position finPos, Board board);

    public boolean isValidMove(Position finPos, Board board){
        Set<Position> legalMoves = calculateLegalMoves(board);
        return legalMoves.contains(finPos);
    }

    public abstract Set<Position> calculateLegalMoves(Board board);
}
