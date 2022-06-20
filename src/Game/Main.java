package Game;

import Piecies.Piece;
import Piecies.Rook;

import java.util.List;
import java.util.Random;

import static Piecies.Position.pos;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.getStandartBoard();
        board.arrangePieces();
        board.visualizeConsoleBoard();
        List<Piece> whitePieces = board.getWhitePieces();
        List<Piece> blackPieces = board.getBlackPieces();
        System.out.println("\n\n\n");
        Piece piece = whitePieces.get(12);
        if (piece.move(pos(4, 3), board)) {
            board.visualizeConsoleBoard();
        } else {
            System.out.println("move not success");
        }
        System.out.println("\n\n\n");
        piece = whitePieces.get(3);
        if (piece.move(pos(7, 4), board)) {
            board.visualizeConsoleBoard();
        } else {
            System.out.println("move not success");
        }


        System.out.println("\n\n\n");
        piece = blackPieces.get(6);
        if (piece.move(pos(5, 4), board)) {
            board.visualizeConsoleBoard();
        } else {
            System.out.println("move not success");
        }
        System.out.println("\n\n\n");
        piece = blackPieces.get(6);
        if (piece.move(pos(4, 3), board)) {
            board.visualizeConsoleBoard();
        } else {
            System.out.println("move not success");
        }
    }
}
