package Game;

import Piecies.Piece;
import Piecies.Position;

public class Board {
    private static Tile[][] board = new Tile[8][8];

    public static Tile[][] getStandartBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i+1 + j+1) % 2 == 0) {
                    board[i][j] = new Tile(Color.BLACK, null);
                } else {
                    board[i][j] = new Tile(Color.WHITE, null);
                }
            }
        }
        return board;
    }

    public static void visualizeConsoleBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j].getColor() == Color.BLACK){
                    System.out.print('#');
                }else {
                    System.out.print('0');
                }
            }
            System.out.println();
        }
    }
}

