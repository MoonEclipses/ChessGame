package Game;

import java.util.Random;

public class Game {
    private Board board;
    private Player player;

    public Game(){
        board = new Board();
        board.getStandartBoard();
        Random random = new Random();
        player = new Player();
        if (random.nextBoolean()) {
            player.setColor(Color.WHITE);
            player.setCurrentMove(true);
        } else {
            player.setColor(Color.BLACK);
            player.setCurrentMove(false);
        }
    }
    public void startGame(){
        board.arrangePieces(player);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
