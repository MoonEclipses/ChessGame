package Game;

import Piecies.Piece;

public class Tile {
    private Color color;
    private Piece piece;

    public Tile(Color color, Piece piece) {
        this.color = color;
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
