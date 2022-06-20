package Piecies;

import java.util.HashSet;
import java.util.Set;

public interface SlidingPiece{
    public default Set<Position> getPositionsToDefend(Position piecePos,Position kingPos){
        Set<Position> positionsToDefend = new HashSet<>();
        Position piecePosition = piecePos;
        Position kingPosition = kingPos;
        while (!piecePosition.equals(kingPosition)){
            Position position = new Position(piecePosition.x,piecePosition.y);
            positionsToDefend.add(position);
            if (piecePosition.x> kingPosition.x){
                piecePosition.x--;
            }else if(piecePosition.x< kingPosition.x){
                piecePosition.x++;
            }
            if (piecePosition.y> kingPosition.y){
                piecePosition.y--;
            }else if(piecePosition.y< kingPosition.y){
                piecePosition.y++;
            }
        }
        return positionsToDefend;
    }
}
