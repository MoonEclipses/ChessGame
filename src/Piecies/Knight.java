package Piecies;

import Game.Board;
import Game.Color;
import Game.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static Piecies.Position.pos;

public class Knight extends Piece {

    public Knight(Position pos, Color color) {
        super(pos, color);
        movingHistory = new ArrayList<>();
        attackedPositions = new HashSet<>();
        movingOffset = new Position[]{pos(-1, 2), pos(1, 2), pos(2, 1), pos(2, -1), pos(1, -2), pos(-1, -2), pos(-2, -1), pos(-2, 1)};
    }

    @Override
    public boolean move(Position distPos, Board board) {
        if (isValidMove(distPos, board)) { //if legal moves consist this move
            Tile curTile = board.getTile(pos);
            curTile.setPiece(null);//set curTile piece null
            Tile finTile = board.getTile(distPos);
            if (finTile.isOcupied()) {
                Piece capturedPiece = finTile.getPiece();
                List<Piece> pieces;
                if (capturedPiece.getColor() == Color.WHITE) pieces = board.getWhitePieces();
                else pieces = board.getBlackPieces();
                pieces.remove(capturedPiece);//if tile is ocupied remove captured piece from the list of pieces
            }
            finTile.setPiece(this);//set finTile piece -> this piece
            pos = distPos;//update position
            attackedPositions.clear();
            attackedPositions.addAll(calculateAttackMoves(board));//update attacked position
            movingHistory.add(distPos);//add move to history
            board.calculateThreatMaps();
            return true;
        } else return false;
    }

    @Override
    public Set<Position> calculateLegalMoves(Board board) {
        Set<Position> legalMoves = new HashSet<>();
        List<Piece> pieces;
        King king;
        if (color == Color.WHITE) {
            king = (King) board.getWhiteKing();
            pieces = board.getBlackPieces();
        } else {
            king = (King) board.getBlackKing();
            pieces = board.getWhitePieces();
        }
        for (Position move : movingOffset) {
            Position distPos = pos.sum(move);
            if (king.isChecked(board) && king.getCheckers(board).size() < 2) {//if king checked by one piece
                Piece checker = king.getCheckers(board).get(0);
                if(checker instanceof SlidingPiece){//if piece is sliding
                    if(((SlidingPiece) checker).getPositionsToDefend(checker.getPos(),king.getPos()).contains(distPos)){
                        legalMoves.add(distPos);
                    } else {
                        break;
                    }
                }else {//pawn and knight
                    if(checker.getPos().equals(distPos)){
                        legalMoves.add(distPos);
                    } else {
                        break;
                    }
                }
            } else if (king.isChecked(board) && king.getCheckers(board).size() >= 2) {//if king checked by more then one piece
                break;
            }

            if (!Position.isPosible(distPos)) {//if move isn't possible(not on existing tiles)
                continue;
            }
            Tile distTile = board.getTile(distPos);
            if (distTile.isOcupied()) {//if tile ocupied check if color of king same or not
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
        for (Piece piece : pieces) {
            if (!(piece instanceof SlidingPiece)) {
                continue;
            }
            Set<Position> positions = ((SlidingPiece) piece).getPositionsToDefend(piece.getPos(), king.getPos());
            if (positions == null) {
                continue;
            }
            if (positions.contains(this.pos) && piece.getAttackedPositions().contains(this.pos)) {
                legalMoves.clear();
            }
        }
        return legalMoves;
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
}
