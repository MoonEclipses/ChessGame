package Piecies;

import Game.Board;
import Game.Color;
import Game.Tile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SlidingPiece extends Piece {

    public SlidingPiece(Position pos, Color color) {
        super(pos, color);
    }

    @Override
    public boolean move(Position distPos, Board board) {
        if (isValidMove(distPos, board)) { //if legal moves consist this move
            Tile curTile = board.getTile(pos);
            curTile.setPiece(null);//set curTile piece null
            Tile finTile = board.getTile(distPos);
            Piece capturedPiece = null;
            if (finTile.isOcupied()) {
                capturedPiece = finTile.getPiece();
                List<Piece> pieces;
                if (capturedPiece.getColor() == Color.WHITE) pieces = board.getWhitePieces();
                else pieces = board.getBlackPieces();
                pieces.remove(capturedPiece);//if tile is ocupied remove captured piece from the list of pieces
            }
            finTile.setPiece(this);//set finTile piece -> this piece
            board.addToMovingHistory(pos, distPos, capturedPiece);
            pos = distPos;//update position
            board.recalculateAttackingPositions();//update attacked position
            movingHistory.add(distPos);//add move to history
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
            Position distPos = pos;
            distPos = distPos.sum(move);
            while (Position.isPosible(distPos)) {
                if (king.isChecked(board) && king.getCheckers(board).size() < 2) {//if king checked by one piece
                    Tile distTile = board.getTile(distPos);
                    if (distTile.isOcupied()) {//if tile ocupied check if color of piece same or not
                        break;
                    }
                    Piece checker = king.getCheckers(board).get(0);
                    if (checker instanceof SlidingPiece) {//if piece is sliding
                        if (((SlidingPiece) checker).getPositionsToDefend(checker.getPos(), king.getPos()).contains(distPos)) {
                            legalMoves.add(distPos);
                        } else {
                            distPos = distPos.sum(move);
                            continue;
                        }
                    } else {//pawn and knight
                        if (checker.getPos().equals(distPos)) {
                            legalMoves.add(distPos);
                        } else {
                            distPos = distPos.sum(move);
                            continue;
                        }
                    }
                } else if (king.isChecked(board) && king.getCheckers(board).size() >= 2) {//if king checked by more then one piece
                    break;
                }
                Tile distTile = board.getTile(distPos);
                if (distTile.isOcupied()) {//if tile ocupied check if color of piece same or not
                    Color attackedPieceColor = distTile.getPiece().getColor();
                    if (attackedPieceColor != color) {
                        legalMoves.add(distPos);
                        break;
                    } else {
                        break;
                    }
                } else {
                    legalMoves.add(distPos);

                }
                distPos = distPos.sum(move);
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
                legalMoves = legalMoves
                        .stream()
                        .filter(c -> positions.contains(c))
                        .collect(Collectors.toSet());
            }
        }
        return legalMoves;
    }

    @Override
    public Set<Position> calculateAttackMoves(Board board) {
        Set<Position> legalMoves = new HashSet<>();
        for (Position move : movingOffset) {
            Position distPos = pos;
            distPos = distPos.sum(move);
            while (Position.isPosible(distPos)) {
                Tile distTile = board.getTile(distPos);
                if (distTile.isOcupied()) {//if tile ocupied check if color of piece same or not
                    legalMoves.add(distPos);
                    if (distTile.getPiece() instanceof King && distTile.getPiece().getColor() != this.color) {
                        distPos = distPos.sum(move);
                        continue;
                    }
                    break;
                } else {
                    legalMoves.add(distPos);

                }
                distPos = distPos.sum(move);
            }
        }
        return legalMoves;
    }

    @Override
    public String getType() {
        return null;
    }

    public Set<Position> getPositionsToDefend(Position piecePos, Position kingPos) {
        Set<Position> positionsToDefend = new HashSet<>();
        Position piecePosition = null;
        Position kingPosition = null;
        try {
            piecePosition = (Position) piecePos.clone();
            kingPosition = (Position) kingPos.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        while (!piecePosition.equals(kingPosition)) {
            Position position = new Position(piecePosition.x, piecePosition.y);
            positionsToDefend.add(position);
            if (piecePosition.x > kingPosition.x) {
                piecePosition.x--;
            } else if (piecePosition.x < kingPosition.x) {
                piecePosition.x++;
            }
            if (piecePosition.y > kingPosition.y) {
                piecePosition.y--;
            } else if (piecePosition.y < kingPosition.y) {
                piecePosition.y++;
            }
        }
        return positionsToDefend;
    }
}
