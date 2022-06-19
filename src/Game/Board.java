package Game;


import Piecies.*;

import java.util.*;

import static Piecies.Position.pos;

public class Board {

    private Tile[][] board;

    private List<Piece> whitePieces;
    private List<Piece> blackPieces;


    private Set<Position> whiteThreatMap;
    private Set<Position> blackThreatMap;

    private static String[] standartArrangment = new String[]{"R", "N", "B", "Q", "K", "B", "N", "R",
            "P", "P", "P", "P", "P", "P", "P", "P",
            "-", "-", "-", "-", "-", "-", "-", "-",
            "-", "-", "-", "-", "-", "-", "-", "-",
            "-", "-", "-", "-", "-", "-", "-", "-",
            "-", "-", "-", "-", "-", "-", "-", "-",
            "p", "p", "p", "p", "p", "p", "p", "p",
            "r", "n", "b", "q", "k", "b", "n", "r",};
    Map<String, Piece> signs;

    public Board() {
        signs = new HashMap<>();

        whiteThreatMap = new HashSet<>();
        blackThreatMap = new HashSet<>();

        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();

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
    }

    public Tile[][] getStandartBoard() {
        if (board == null) {
            board = new Tile[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((i + 1 + j + 1) % 2 == 0) {
                        board[i][j] = new Tile(Color.BLACK, null);
                    } else {
                        board[i][j] = new Tile(Color.WHITE, null);
                    }
                }
            }
        }
        return board;
    }

    public void arrangePieces() {
        for (int i = 0; i < standartArrangment.length; i++) {
            String str = standartArrangment[i];
            Piece newPiece = signs.get(str);
            if (newPiece != null) {
                newPiece.setPos(pos(i % 8, i / 8));
                getTile(newPiece.getPos()).setPiece(newPiece);
                if (newPiece.getColor() == Color.WHITE) {
                    whitePieces.add(newPiece);
                } else {
                    blackPieces.add(newPiece);
                }
            }
        }
        List<Piece> pieces = new ArrayList<>();
        pieces.addAll(whitePieces);
        pieces.addAll(blackPieces);
        for (Piece piece : pieces) {
            piece.calculateStartingAttackedPositions(this);
        }
        calculateThreatMaps();
    }

    public Tile getTile(Position pos) {
        return board[pos.x][pos.y];
    }

    public boolean isCheckMate() {
        return false;
    }

    public void calculateThreatMaps() {
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
                if (board[j][i].isOcupied()) {
                    for (Map.Entry<String,Piece> entry:signs.entrySet()){
                        if(entry.getValue().equals(board[j][i].getPiece())){
                            System.out.print(entry.getKey());
                        }
                    }
                } else if (board[j][i].getColor() == Color.BLACK) {
                    System.out.print('#');
                } else {
                    System.out.print('0');
                }
                System.out.print(' ');
            }
            System.out.println();
        }
    }
    public void visualizeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[j][i].isOcupied()) {
                    for (Map.Entry<String,Piece> entry:signs.entrySet()){
                        if(entry.getValue().equals(board[j][i].getPiece())){
                            System.out.print(entry.getKey());
                        }
                    }
                }
                else {
                    System.out.print("-");
                }
            }
            System.out.println();
        }
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
}

