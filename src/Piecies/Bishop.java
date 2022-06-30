package Piecies;

import Game.Board;
import Game.Color;
import Game.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static Piecies.Position.pos;

public class Bishop extends SlidingPiece {
    public Bishop(Position pos, Color color) {
        super(pos, color);
        movingHistory = new ArrayList<>();
        attackedPositions = new HashSet<>();
        movingOffset = new Position[]{pos(1, 1), pos(1, -1), pos(-1, -1), pos(-1, 1)};
    }


    @Override
    public String getType() {
        return "Bishop";
    }

    @Override
    public Set<Position> getPositionsToDefend(Position piecePos, Position kingPos) {
        if(Math.abs( piecePos.x-kingPos.x) == Math.abs(piecePos.y - kingPos.y)) {
            return super.getPositionsToDefend(piecePos, kingPos);
        }else {
            return null;
        }
    }
}
