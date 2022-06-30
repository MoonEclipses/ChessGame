package Piecies;

public class Move {
    public Position firstPosition;
    public Position secondPosition;
    private Piece capturedPiece;
    private boolean isCastle = false;
    private boolean isEnPassant = false;

    public Move(Position firstPosition, Position secondPosition,Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
        try {
            this.firstPosition = (Position) firstPosition.clone();
            this.secondPosition = (Position) secondPosition.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public boolean isCastle() {
        return isCastle;
    }

    public void setCastle(boolean castle) {
        isCastle = castle;
    }

    public boolean isEnPassant() {
        return isEnPassant;
    }

    public void setEnPassant(boolean enPassant) {
        isEnPassant = enPassant;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public Position getFirstPosition() {
        return firstPosition;
    }

    public void setFirstPosition(Position firstPosition) {
        this.firstPosition = firstPosition;
    }

    public Position getSecondPosition() {
        return secondPosition;
    }

    public void setSecondPosition(Position secondPosition) {
        this.secondPosition = secondPosition;
    }
}
