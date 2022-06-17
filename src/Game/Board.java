package Game;


import Piecies.Piece;
import Piecies.Position;

import java.util.List;

public class Board {

    private Tile[][] board;

    private List<Piece> blackPieces;
    private List<Piece> whitePieces;

    public Board() {

    }

    public Tile[][] getStandartBoard() {
        if(board == null) {
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

    public Tile getTile(Position pos){
        return board[pos.x][pos.y];
    }

    public boolean isCheckMate(){
        return false;
    }

    public void visualizeConsoleBoard() {
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

