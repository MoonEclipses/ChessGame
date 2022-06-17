package Piecies;

import Game.Board;
import Game.Color;
import Game.Tile;

import java.util.Set;

import static Piecies.Position.pos;

public class King extends Piece {

    public King(Position pos, Color color) {
        super(pos, color);
        movingOffset = new Position[]{pos(1, 1),pos(1, 0),pos(1,-1),pos(0, -1),pos(-1, -1),pos(-1,0),pos(-1, 1),pos(0, 1)};
    }

    @Override
    public boolean move(Position finPos,Board board) {
        if(isValidMove(finPos,board)){
            Tile curTile = board.getTile(pos);
            curTile.setPiece(null);
            Tile finTile = board.getTile(finPos);
            finTile.setPiece(this);
            return true;
        }
        else return false;
    }


    @Override
    public Set<Position> calculateLegalMoves(Board board) {
        return null;
    }

    public boolean isChecked() {
        return false;
    }
}
