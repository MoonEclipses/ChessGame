package Game;


import GUI.Table;
import Piecies.Piece;
import Piecies.Position;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        Table table = new Table(game);
        game.startGame();
        table.drawPieces();
    }
}
