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

public class Pawn extends Piece {
    protected Position[] attackingOffset;

    public Pawn(Position pos, Color color) {
        super(pos, color);
        movingHistory = new ArrayList<>();
        attackedPositions = new HashSet<>();
        movingOffset = new Position[]{pos(0, 1), pos(0, 2), pos(0, -1), pos(0, -2)};
        attackingOffset = new Position[]{pos(-1, 1), pos(1, 1), pos(-1, -1), pos(1, -1)};
    }

    @Override
    public boolean move(Position distPos, Board board) {
        if (isValidMove(distPos, board)) { //if legal moves consist this move
            Tile curTile = board.getTile(pos);
            curTile.setPiece(null);//set curTile piece null
            Tile finTile = board.getTile(distPos);
            if (!finTile.isOcupied() && (Math.abs(pos.x - distPos.x) == Math.abs(pos.y - distPos.y))) {
                enPassant(distPos, board);
                return true;
            }
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

    private void enPassant(Position distPos, Board board) {
        Tile finTile = board.getTile(distPos);
        Tile capturedPawnTile = board.getTile(pos(distPos.x, pos.y));
        Piece capturedPawn = capturedPawnTile.getPiece();
        if (capturedPawn.getColor() == Color.WHITE) {
            board.getWhitePieces().remove(capturedPawn);
        } else {
            board.getBlackPieces().remove(capturedPawn);
        }
        capturedPawnTile.setPiece(null);
        finTile.setPiece(this);
        board.addToMovingHistory(pos, distPos, capturedPawn);
        board.getMovingHistory().get(board.getMovingHistory().size() - 1).setEnPassant(true);
        this.pos = distPos;
        board.recalculateAttackingPositions();//update attacked position
        movingHistory.add(distPos);//add move to history
    }

    @Override
    public Set<Position> calculateLegalMoves(Board board) {
        Set<Position> legalMoves = new HashSet<>();
        List<Piece> pieces;
        King king;
        int a, b;
        if (color == Color.WHITE) {
            king = (King) board.getWhiteKing();
            pieces = board.getBlackPieces();
            a = 0;
            b = 2;
        } else {
            king = (King) board.getBlackKing();
            pieces = board.getWhitePieces();
            a = 2;
            b = 4;
        }
        for (int i = a; i < b; i++) {
            Position move = movingOffset[i];
            Position distPos = pos.sum(move);
            if (king.isChecked(board) && king.getCheckers(board).size() < 2) {//if king checked by one piece
                Piece checker = king.getCheckers(board).get(0);
                if (checker instanceof SlidingPiece) {//if piece is sliding
                    Set<Position> positions = ((SlidingPiece) checker).getPositionsToDefend(checker.getPos(), king.getPos());
                    positions.remove(checker.getPos());
                    if (positions.contains(distPos)) {
                        legalMoves.add(distPos);
                    } else {
                        continue;
                    }
                } else {//pawn and knight
                    break;
                }
            } else if (king.isChecked(board) && king.getCheckers(board).size() >= 2) {//if king checked by more then one piece
                break;
            }

            if (!Position.isPosible(distPos)) {//if move isn't possible(not on existing tiles)
                continue;
            }
            Tile distTile = board.getTile(distPos);
            if (distTile.isOcupied()) {
                continue;
            }
            if (i == a+1 && movingHistory.isEmpty()) {
                legalMoves.add(distPos);
            } else if (i == a) {
                legalMoves.add(distPos);
            }
        }
        for (int i = a; i < b; i++) {
            Position move = attackingOffset[i];
            Position distPos = pos.sum(move);
            if (king.isChecked(board) && king.getCheckers(board).size() < 2) {//if king checked by one piece
                Piece checker = king.getCheckers(board).get(0);
                if (checker.getPos().equals(distPos)) {
                    legalMoves.add(distPos);
                } else continue;
            } else if (king.isChecked(board) && king.getCheckers(board).size() >= 2) {//if king checked by more then one piece
                break;
            }

            if (!Position.isPosible(distPos)) {//if move isn't possible(not on existing tiles)
                continue;
            }
            Tile distTile = board.getTile(distPos);
            if (distTile.isOcupied()) {//if tile ocupied check if color of piece same or not
                Color attackedPieceColor = distTile.getPiece().getColor();
                if (attackedPieceColor != color) {
                    legalMoves.add(distPos);
                } else continue;
            }
            Tile tile = board.getTile(pos(distPos.x, this.pos.y));
            if ((tile.isOcupied() && (tile.getPiece() instanceof Pawn) && (tile.getPiece().getColor() != this.color))) {
                Move lastMove = board.getMovingHistory().get(board.getMovingHistory().size() - 1);
                if (lastMove.getSecondPosition().equals(tile.getPiece().pos)) {
                    if (Math.abs(lastMove.getSecondPosition().y - lastMove.getFirstPosition().y) == 2) {
                        legalMoves.add(distPos);
                    }
                }
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
        Set<Position> attackMoves = new HashSet<>();
        if (this.color == Color.WHITE) {
            for (int i = 0; i < 2; i++) {
                Position move = attackingOffset[i];
                Position distPos = pos.sum(move);
                if (!Position.isPosible(distPos)) {//if move isn't possible(not on existing tiles)
                    continue;
                }
                attackMoves.add(distPos);
            }
        } else {
            for (int i = 2; i < 4; i++) {
                Position move = attackingOffset[i];
                Position distPos = pos.sum(move);
                if (!Position.isPosible(distPos)) {//if move isn't possible(not on existing tiles)
                    continue;
                }
                attackMoves.add(distPos);
            }
        }
        return attackMoves;
    }

    public boolean canBePromoted() {
        if (this.color == Color.WHITE) {
            return this.pos.y == 7;
        } else {
            return this.pos.y == 0;
        }
    }

    public void promote(Piece piece, Board board) {
        List<Piece> pieces;
        if (this.color == Color.WHITE) {
            pieces = board.getWhitePieces();
        } else {
            pieces = board.getBlackPieces();
        }
        pieces.remove(this);
        pieces.add(piece);
        piece.setPos(this.pos);
        board.getTile(this.pos).setPiece(piece);
    }

    @Override
    public String getType() {
        return "Pawn";
    }
}

