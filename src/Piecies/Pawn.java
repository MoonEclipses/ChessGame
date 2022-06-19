package Piecies;

import Game.Board;
import Game.Color;
import Game.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static Piecies.Position.pos;

public class Pawn extends Piece{
    protected Position[] attackingOffset;
    public Pawn(Position pos, Color color) {
        super(pos,color);
        movingHistory = new ArrayList<>();
        attackedPositions = new HashSet<>();
        movingOffset = new Position[]{pos(0, 1),pos(0, 2),pos(0, -1),pos(0, -2)};
        attackingOffset = new Position[]{pos(-1,1),pos(1,1),pos(-1,-1),pos(1,-1)};
    }

    @Override
    public boolean move(Position distPos, Board board) {
        if(isValidMove(distPos,board)){ //if legal moves consist this move
            Tile curTile = board.getTile(pos);
            curTile.setPiece(null);//set curTile piece null
            Tile finTile = board.getTile(distPos);
            if(finTile.isOcupied()){
                Piece capturedPiece = finTile.getPiece();
                List<Piece> pieces;
                if(capturedPiece.getColor() == Color.WHITE)  pieces = board.getWhitePieces();
                else pieces = board.getBlackPieces();
                pieces.remove(capturedPiece);//if tile is ocupied remove captured piece from the list of pieces
            }
            finTile.setPiece(this);//set finTile piece -> this piece
            pos = distPos;//update position
            attackedPositions.clear();
            attackedPositions.addAll(calculateAttackMoves(board));//update attacked position
            movingHistory.add(distPos);//add move to history
            return true;
        }
        else return false;
    }

    @Override
    public Set<Position> calculateLegalMoves(Board board) {
        Set<Position> legalMoves = new HashSet<>();
        if(this.color == Color.WHITE){
            for (int i = 0; i < 2; i++) {
            Position move = movingOffset[i];
            Position distPos = pos.sum(move);
            if(!Position.isPosible(distPos)) {//if move isn't possible(not on existing tiles)
                continue;
            }
            Tile distTile = board.getTile(distPos);
            if(distTile.isOcupied()){
                continue;
            }
            if(i==2 && movingHistory.isEmpty()) {
                legalMoves.add(distPos);
            }else legalMoves.add(distPos);
            }
            for (int i = 0; i < 2; i++) {
                Position move = attackingOffset[i];
                Position distPos = pos.sum(move);
                if(!Position.isPosible(distPos)) {//if move isn't possible(not on existing tiles)
                    continue;
                }
                Tile distTile = board.getTile(distPos);
                if(distTile.isOcupied()){//if tile ocupied check if color of piece same or not
                    Color attackedPieceColor = distTile.getPiece().getColor();
                    if (attackedPieceColor != color){
                        legalMoves.add(distPos);
                    }
                    else {
                        continue;
                    }
                }
            }
        }else {
            for (int i = 2; i < 4; i++) {
                Position move = movingOffset[i];
                Position distPos = pos.sum(move);
                if(!Position.isPosible(distPos)) {//if move isn't possible(not on existing tiles)
                    continue;
                }
                Tile distTile = board.getTile(distPos);
                if(distTile.isOcupied()){
                    continue;
                }
                if(i==2 && movingHistory.isEmpty()) {
                    legalMoves.add(distPos);
                }else legalMoves.add(distPos);
            }
            for (int i = 2; i < 4; i++) {
                Position move = attackingOffset[i];
                Position distPos = pos.sum(move);
                if(!Position.isPosible(distPos)) {//if move isn't possible(not on existing tiles)
                    continue;
                }
                Tile distTile = board.getTile(distPos);
                if(distTile.isOcupied()){//if tile ocupied check if color of piece same or not
                    Color attackedPieceColor = distTile.getPiece().getColor();
                    if (attackedPieceColor != color){
                        legalMoves.add(distPos);
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        return legalMoves;
    }

    public Set<Position> calculateAttackMoves(Board board) {
        Set<Position> attackMoves = new HashSet<>();
        if(this.color == Color.WHITE){
            for (int i = 0; i < 2; i++) {
                Position move = attackingOffset[i];
                Position distPos = pos.sum(move);
                if(!Position.isPosible(distPos)) {//if move isn't possible(not on existing tiles)
                    continue;
                }
                Tile distTile = board.getTile(distPos);
                if(distTile.isOcupied()){//if tile ocupied check if color of piece same or not
                    Color attackedPieceColor = distTile.getPiece().getColor();
                    if (attackedPieceColor != color){
                        attackMoves.add(distPos);
                    }
                    else {
                        continue;
                    }
                }
            }
        }else {
            for (int i = 2; i < 4; i++) {
                Position move = attackingOffset[i];
                Position distPos = pos.sum(move);
                if(!Position.isPosible(distPos)) {//if move isn't possible(not on existing tiles)
                    continue;
                }
                Tile distTile = board.getTile(distPos);
                if(distTile.isOcupied()){//if tile ocupied check if color of piece same or not
                    Color attackedPieceColor = distTile.getPiece().getColor();
                    if (attackedPieceColor != color){
                        attackMoves.add(distPos);
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        return attackMoves;
    }
    }

