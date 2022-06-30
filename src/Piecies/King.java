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

public class King extends Piece {
    Position[] castleOffset;

    public King(Position pos, Color color) {
        super(pos, color);
        movingHistory = new ArrayList<>();
        attackedPositions = new HashSet<>();
        movingOffset = new Position[]{pos(1, 1), pos(1, 0), pos(1, -1), pos(0, -1), pos(-1, -1), pos(-1, 0), pos(-1, 1), pos(0, 1)};
        castleOffset = new Position[]{pos(2, 0), pos(-2, 0)};
    }

    @Override
    public boolean move(Position distPos, Board board) {
        if (isValidMove(distPos, board)) { //legal moves consist this move
            if(distPos.equals(pos.sum(castleOffset[0])) || distPos.equals(pos.sum(castleOffset[1]))){
                castle(distPos,board);
                return true;
            }
            Tile curTile = board.getTile(pos);
            curTile.setPiece(null);
            Tile finTile = board.getTile(distPos);
            Piece capturedPiece = null;
            if (finTile.isOcupied()) {
                capturedPiece = finTile.getPiece();
                List<Piece> pieces;
                if (capturedPiece.getColor() == Color.WHITE) pieces = board.getWhitePieces();
                else pieces = board.getBlackPieces();
                pieces.remove(capturedPiece);
            }
            finTile.setPiece(this);
            board.addToMovingHistory(pos,distPos,capturedPiece);
            pos = distPos;
            board.recalculateAttackingPositions();
            movingHistory.add(distPos);
            return true;
        } else return false;
    }


    @Override
    public Set<Position> calculateLegalMoves(Board board) {
        Set<Position> legalMoves = new HashSet<>();
        Set<Position> threatMap;
        List<Piece> pieces;
        List<Piece> rooks;
        if (color == Color.WHITE) {
            threatMap = board.getBlackThreatMap();
            pieces = board.getWhitePieces();
        } else {
            threatMap = board.getWhiteThreatMap();
            pieces = board.getBlackPieces();
        }
        rooks = pieces.stream().filter(c -> c instanceof Rook)
                .collect(Collectors.toList());
        for (Position move : movingOffset) {
            Position distPos = pos.sum(move);
            if (!Position.isPosible(distPos)) {
                continue;
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
        for(Piece piece: rooks){
            if(!(piece.movingHistory.isEmpty() && this.movingHistory.isEmpty())) continue;
            if(!(piece instanceof Rook)) continue;
            Set<Position> positions = ((Rook) piece).getPositionsToDefend(piece.getPos(), this.getPos());
            positions.remove(piece.getPos());
            positions.add(this.getPos());
            if(!positions.stream().filter(c->threatMap.contains(c)).collect(Collectors.toSet()).isEmpty()) continue;
            positions.remove(this.getPos());
            Set<Position> occupied = positions.stream().filter(c->board.getTile(c).isOcupied()).collect(Collectors.toSet());
            if(!occupied.isEmpty()) continue;
            for (Position move : castleOffset){
                Position distPos = pos.sum(move);
                if(positions.contains(distPos)){
                    legalMoves.add(distPos);
                }
            }
        }

        return legalMoves;
    }


    public boolean isChecked(Board board) {
        if (color == Color.WHITE) {
            return board.getBlackThreatMap().contains(pos);
        } else {
            return board.getWhiteThreatMap().contains(pos);
        }
    }

    public List<Piece> getCheckers(Board board) {
        List<Piece> pieces = new ArrayList<>();
        if (color == Color.WHITE) {
            for (Piece piece : board.getBlackPieces()) {
                if (piece.getAttackedPositions().contains(pos)) {
                    pieces.add(piece);
                }
            }
        } else {
            for (Piece piece : board.getWhitePieces()) {
                if (piece.getAttackedPositions().contains(pos)) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }
    public void castle(Position distPos,Board board){
        Tile curTile = board.getTile(pos);
        curTile.setPiece(null);
        Tile finTile = board.getTile(distPos);
        finTile.setPiece(this);
        board.addToMovingHistory(pos,distPos,null);
        board.getMovingHistory().get(board.getMovingHistory().size()-1).setCastle(true);
        this.pos = distPos;
        movingHistory.add(distPos);
        Piece rook;
        if(distPos.x==6){
            rook = board.getTile(pos(distPos.x+1,distPos.y)).getPiece();
            distPos = pos(distPos.x-1,distPos.y);
        }else {
            rook = board.getTile(pos(distPos.x-2,distPos.y)).getPiece();
            distPos = pos(distPos.x+1,distPos.y);
        }
        board.getTile(rook.pos).setPiece(null);
        finTile = board.getTile(distPos);
        finTile.setPiece(rook);
        rook.setPos(distPos);
        board.recalculateAttackingPositions();
    }

    @Override
    public Set<Position> calculateAttackMoves(Board board) {
        Set<Position> legalMoves = new HashSet<>();
        for (Position move : movingOffset) {
            Position distPos = pos.sum(move);
            if (!Position.isPosible(distPos)) {
                continue;
            }
            Tile distTile = board.getTile(distPos);
            legalMoves.add(distPos);
        }
        return legalMoves;
    }

    @Override
    public String getType() {
        return "King";
    }
}
