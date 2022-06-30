package Piecies;

import Game.Board;
import Game.Color;

import java.util.*;

public abstract class Piece implements Cloneable{
    protected Position pos;//position on board
    protected Color color;//Color
    protected Position[] movingOffset;//How can piece move from current position
    protected List<Position> movingHistory;
    protected Set<Position> attackedPositions;//Positions that piece attack

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

    public abstract boolean move(Position distPos, Board board);

    public boolean isValidMove(Position distPos, Board board){
        Set<Position> legalMoves = calculateLegalMoves(board);
        return legalMoves.contains(distPos);
    }

    public abstract Set<Position> calculateLegalMoves(Board board);

    public abstract Set<Position> calculateAttackMoves(Board board);

    public abstract String getType();

    public void calculateStartingAttackedPositions(Board board){
        attackedPositions = calculateAttackMoves(board);
    }



    public List<Position> getMovingHistory() {
        return movingHistory;
    }

    public Set<Position> getAttackedPositions() {
        return attackedPositions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return color == piece.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Piece newPiece = (Piece) super.clone();
        newPiece.movingHistory = new ArrayList<>();
        return newPiece;
    }

}
