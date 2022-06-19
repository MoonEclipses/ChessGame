package Game;

import Piecies.Piece;

import java.util.List;

import static Piecies.Position.pos;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.getStandartBoard();
        board.arrangePieces();
        board.visualizeConsoleBoard();
        List<Piece> pieces = board.getWhitePieces();
        for (int i = 3; i < 8; i++) {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            if (pieces.get(12).move(pos(7,i),board)){
                board.visualizeConsoleBoard();
            }else {
                System.out.println("move not success");
            }
            board.calculateThreatMaps();
        }
    }
}
