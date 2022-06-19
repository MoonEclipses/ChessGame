package Piecies;

import Game.Board;
import Game.Color;
import Game.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static Piecies.Position.pos;

public class King extends Piece {

    public King(Position pos, Color color) {
        super(pos, color);
        movingHistory = new ArrayList<>();
        attackedPositions = new HashSet<>();
        movingOffset = new Position[]{pos(1, 1), pos(1, 0), pos(1, -1), pos(0, -1), pos(-1, -1), pos(-1, 0), pos(-1, 1), pos(0, 1)};
    }

    @Override
    public boolean move(Position distPos, Board board) {
        if (isValidMove(distPos, board)) { //legal moves consist this move
            Tile curTile = board.getTile(pos);
            curTile.setPiece(null);
            Tile finTile = board.getTile(distPos);
            if (finTile.isOcupied()) {
                Piece capturedPiece = finTile.getPiece();
                List<Piece> pieces;
                if (capturedPiece.getColor() == Color.WHITE) pieces = board.getWhitePieces();
                else pieces = board.getBlackPieces();
                pieces.remove(capturedPiece);
            }
            finTile.setPiece(this);
            pos = distPos;
            attackedPositions.clear();
            attackedPositions.addAll(calculateLegalMoves(board));
            movingHistory.add(distPos);
            return true;
        } else return false;
    }


    @Override
    public Set<Position> calculateLegalMoves(Board board) {
        Set<Position> legalMoves = new HashSet<>();
        for (Position move : movingOffset) {
            Position distPos = pos.sum(move);
            if (!Position.isPosible(distPos)) {
                continue;
            }
            Set<Position> threatMap;
            if (color == Color.WHITE) {
                threatMap = board.getBlackThreatMap();
            } else {
                threatMap = board.getWhiteThreatMap();
            }
            if (threatMap.contains(distPos)) {
                continue;
            }
            Tile distTile = board.getTile(distPos);
            if (distTile.isOcupied()) {
                Color attackedPieceColor = distTile.getPiece().getColor();
                if (attackedPieceColor != color) {
                    legalMoves.add(distPos);
                } else {
                    continue;
                }
            } else {
                legalMoves.add(distPos);
            }
        }
        return legalMoves;
    }


    public boolean isChecked(Board board) {
        if (color == Color.WHITE) {
            return board.getBlackThreatMap().contains(pos);
        }else {
            return board.getWhiteThreatMap().contains(pos);
        }
    }
    public List<Piece> getCheckers(Board board){
        List<Piece> pieces = new ArrayList<>();
        if (color == Color.WHITE) {
            for(Piece piece:board.getBlackPieces()){
                if(piece.getAttackedPositions().contains(pos)){
                    pieces.add(piece);
                }
            }
        }else {
            for(Piece piece:board.getWhitePieces()){
                if(piece.getAttackedPositions().contains(pos)){
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }
}
