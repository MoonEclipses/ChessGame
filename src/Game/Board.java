package Game;


import Piecies.*;

import java.util.*;

import static Piecies.Position.pos;

public class Board {

    private Tile[][] tileBoard;

    private List<Piece> whitePieces;
    private List<Piece> blackPieces;


    private Set<Position> whiteThreatMap;
    private Set<Position> blackThreatMap;

    public List<Move> movingHistory;

    private static String[] standartArrangment = new String[]
            {"R", "N", "B", "Q", "K", "B", "N", "R",
                    "P", "P", "P", "P", "P", "P", "P", "P",
                    "-", "-", "-", "-", "-", "-", "-", "-",
                    "-", "-", "-", "-", "-", "-", "-", "-",
                    "-", "-", "-", "-", "-", "-", "-", "-",
                    "-", "-", "-", "-", "-", "-", "-", "-",
                    "p", "p", "p", "p", "p", "p", "p", "p",
                    "r", "n", "b", "q", "k", "b", "n", "r",};

    Map<String, Piece> signs;

    private ArrayList<ArrayList<Piece>> drawCases;

    private ArrayList<String> positionStrings;

    public Board() {
        signs = new HashMap<>();

        whiteThreatMap = new HashSet<>();
        blackThreatMap = new HashSet<>();

        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();

        movingHistory = new ArrayList<>();

        positionStrings = new ArrayList<>();

        signs.put("K", new King(null, Color.WHITE));
        signs.put("k", new King(null, Color.BLACK));
        signs.put("N", new Knight(null, Color.WHITE));
        signs.put("n", new Knight(null, Color.BLACK));
        signs.put("R", new Rook(null, Color.WHITE));
        signs.put("r", new Rook(null, Color.BLACK));
        signs.put("B", new Bishop(null, Color.WHITE));
        signs.put("b", new Bishop(null, Color.BLACK));
        signs.put("Q", new Queen(null, Color.WHITE));
        signs.put("q", new Queen(null, Color.BLACK));
        signs.put("P", new Pawn(null, Color.WHITE));
        signs.put("p", new Pawn(null, Color.BLACK));

        drawCases = new ArrayList<>();
        ArrayList<Piece> pieces = new ArrayList<>();
        pieces.add(new King(null, Color.WHITE));
        pieces.add(new King(null, Color.BLACK));
        drawCases.add(pieces);
        pieces = new ArrayList<>();
        pieces.add(new King(null, Color.WHITE));
        pieces.add(new King(null, Color.BLACK));
        pieces.add(new Knight(null, Color.WHITE));
        drawCases.add(pieces);
        pieces = new ArrayList<>();
        pieces.add(new King(null, Color.WHITE));
        pieces.add(new King(null, Color.BLACK));
        pieces.add(new Knight(null, Color.BLACK));
        drawCases.add(pieces);
        pieces = new ArrayList<>();
        pieces.add(new King(null, Color.WHITE));
        pieces.add(new King(null, Color.BLACK));
        pieces.add(new Knight(null, Color.WHITE));
        pieces.add(new Knight(null, Color.WHITE));
        drawCases.add(pieces);
        pieces = new ArrayList<>();
        pieces.add(new King(null, Color.WHITE));
        pieces.add(new King(null, Color.BLACK));
        pieces.add(new Knight(null, Color.BLACK));
        pieces.add(new Knight(null, Color.BLACK));
        drawCases.add(pieces);
        pieces = new ArrayList<>();
        pieces.add(new King(null, Color.WHITE));
        pieces.add(new King(null, Color.BLACK));
        pieces.add(new Bishop(null, Color.WHITE));
        drawCases.add(pieces);
        pieces = new ArrayList<>();
        pieces.add(new King(null, Color.WHITE));
        pieces.add(new King(null, Color.BLACK));
        pieces.add(new Bishop(null, Color.BLACK));
        drawCases.add(pieces);
    }

    public Tile[][] getStandartBoard() {
        if (tileBoard == null) {
            tileBoard = new Tile[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((i + 1 + j + 1) % 2 == 0) {
                        tileBoard[i][j] = new Tile(Color.BLACK, null);
                    } else {
                        tileBoard[i][j] = new Tile(Color.WHITE, null);
                    }
                }
            }
        }
        return tileBoard;
    }

    public void revertMove(Player player) {
        if (movingHistory.isEmpty()) return;
        Move lastMove = movingHistory.get(movingHistory.size() - 1);
        Tile curTile = getTile(lastMove.getSecondPosition());
        Piece pieceToRevert = curTile.getPiece();
        Tile distTile = getTile(lastMove.getFirstPosition());
        if (lastMove.isCastle()) {
            curTile.setPiece(null);
            distTile.setPiece(pieceToRevert);
            Tile rTile = getTile(pieceToRevert.getPos().sum(pos(1, 0)));
            Piece rook;
            Position position;
            if (rTile.getPiece() != null && rTile.getPiece() instanceof Rook) {
                rook = rTile.getPiece();
                rTile.setPiece(null);
                position = pos(rook.getPos().x - 3, rook.getPos().y);

            } else {
                Tile lTile = getTile(pieceToRevert.getPos().sum(pos(-1, 0)));
                rook = lTile.getPiece();
                lTile.setPiece(null);
                position = pos(rook.getPos().x + 2, rook.getPos().y);
            }
            getTile(position).setPiece(rook);
            rook.setPos(position);
            try {
                pieceToRevert.setPos((Position) lastMove.getFirstPosition().clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else if (lastMove.isEnPassant()) {
            curTile.setPiece(null);
            distTile.setPiece(pieceToRevert);
            Piece piece = lastMove.getCapturedPiece();
            if (piece.getColor() == Color.WHITE) {
                whitePieces.add(piece);
            } else {
                blackPieces.add(piece);
            }
            getTile(piece.getPos()).setPiece(piece);
            try {
                pieceToRevert.setPos((Position) lastMove.getFirstPosition().clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else if (lastMove.getCapturedPiece() != null) {
            Piece capturedPiece = lastMove.getCapturedPiece();
            if (capturedPiece.getColor() == Color.WHITE) {
                whitePieces.add(capturedPiece);
            } else {
                blackPieces.add(capturedPiece);
            }
            curTile.setPiece(capturedPiece);
            distTile.setPiece(pieceToRevert);
            try {
                pieceToRevert.setPos((Position) lastMove.getFirstPosition().clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else {
            curTile.setPiece(null);
            distTile.setPiece(pieceToRevert);
            try {
                pieceToRevert.setPos((Position) lastMove.getFirstPosition().clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        player.setCurrentMove(!player.isCurrentMove());
        movingHistory.remove(movingHistory.size() - 1);
        pieceToRevert.getMovingHistory().remove(pieceToRevert.getMovingHistory().size() - 1);
        recalculateAttackingPositions();
    }

    public void arrangePieces(Player player) {
        for (int i = 0; i < standartArrangment.length; i++) {
            String str = standartArrangment[i];
            Piece signedPiece = signs.get(str);
            if (signedPiece != null) {
                Piece newPiece = null;
                try {
                    newPiece = (Piece) signedPiece.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                newPiece.setPos(pos(i % 8, i / 8));
                Tile tile = getTile(newPiece.getPos());
                tile.setPiece(newPiece);
                if (newPiece.getColor() == Color.WHITE) {
                    whitePieces.add(newPiece);
                } else {
                    blackPieces.add(newPiece);
                }
            }
        }
        recalculateAttackingPositions();
    }

    public void recalculateAttackingPositions() {
        List<Piece> pieces = new ArrayList<>();
        pieces.addAll(whitePieces);
        pieces.addAll(blackPieces);
        for (Piece piece : pieces) {
            piece.calculateStartingAttackedPositions(this);
        }
        calculateThreatMaps();
    }

    public Tile getTile(Position pos) {
        return tileBoard[pos.x][pos.y];
    }

    public boolean isCheckMate() {
        King king = (King) getWhiteKing();
        if (king.isChecked(this)) {
            for (Piece piece : whitePieces) {
                if (!piece.calculateLegalMoves(this).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        king = (King) getBlackKing();
        if (king.isChecked(this) && king.calculateLegalMoves(this).isEmpty()) {
            for (Piece piece : blackPieces) {
                if (!piece.calculateLegalMoves(this).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isDraw() {
        ArrayList<Piece> allPieces = new ArrayList<>();
        allPieces.addAll(whitePieces);
        allPieces.addAll(blackPieces);
        for (ArrayList<Piece> pieces : drawCases) {
            if (pieces.equals(allPieces)) {
                return true;
            }
        }
        int counter = 0;
        Collections.sort(positionStrings);
        String lastPosition = null;
        for (String position : positionStrings) {
            if (lastPosition != null && lastPosition == position) {
                counter++;
            }
            lastPosition = position;
            if (counter == 3) break;
        }
        if (counter == 3) {
            return true;
        }
        for (Piece piece : whitePieces) {
            if (!piece.calculateLegalMoves(this).isEmpty()) {
                return false;
            }
        }
        for (Piece piece : blackPieces) {
            if (!piece.calculateLegalMoves(this).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void calculateThreatMaps() {//fix bug with threat maps
        positionStrings.add(positionToString());
        whiteThreatMap.clear();
        for (Piece piece : whitePieces) {
            Set<Position> attackedPositions = piece.getAttackedPositions();
            whiteThreatMap.addAll(attackedPositions);
        }
        blackThreatMap.clear();
        for (Piece piece : blackPieces) {
            Set<Position> attackedPositions = piece.getAttackedPositions();
            blackThreatMap.addAll(attackedPositions);
        }
    }

    public void visualizeConsoleBoard() {
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                if (getTile(pos(j, i)).isOcupied()) {
                    for (Map.Entry<String, Piece> entry : signs.entrySet()) {
                        if (entry.getValue().equals(getTile(pos(j, i)).getPiece())) {
                            System.out.print(entry.getKey());
                        }
                    }
                } else {
                    System.out.print('_');
                }
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public void visualizeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tileBoard[j][i].isOcupied()) {
                    for (Map.Entry<String, Piece> entry : signs.entrySet()) {
                        if (entry.getValue().equals(tileBoard[j][i].getPiece())) {
                            System.out.print(entry.getKey());
                        }
                    }
                } else {
                    System.out.print("-");
                }
            }
            System.out.println();
        }
    }

    public List<Tile> getTileList(Player player) {
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles.add(tileBoard[j][i]);
            }
        }
        return tiles;
    }

    public String positionToString() {
        String position = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boolean added = false;
                for (Map.Entry<String, Piece> entry : signs.entrySet()) {
                    if (entry.getValue().equals(getTile(pos(j, i)).getPiece())) {
                        position += entry.getKey();
                        added = true;
                    }
                }
                if (!added) {
                    position += "-";
                }
            }
        }
        return position;
    }

    public Piece getWhiteKing() {
        for (Piece piece : whitePieces) {
            if (piece instanceof King) {
                return piece;
            }
        }
        return null;
    }

    public Piece getBlackKing() {
        for (Piece piece : blackPieces) {
            if (piece instanceof King) {
                return piece;
            }
        }
        return null;
    }

    public Set<Position> getWhiteThreatMap() {
        return whiteThreatMap;
    }

    public Set<Position> getBlackThreatMap() {
        return blackThreatMap;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    public List<Move> getMovingHistory() {
        return movingHistory;
    }

    public void addToMovingHistory(Position firstPosition, Position secondPosition, Piece capturedPiece) {
        movingHistory.add(new Move(firstPosition, secondPosition, capturedPiece));
    }

    public Tile[][] getTileBoard() {
        return tileBoard;
    }
}

