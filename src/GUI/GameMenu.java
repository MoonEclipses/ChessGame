package GUI;

import Game.Board;
import Game.Game;

import javax.swing.*;

public class GameMenu extends JMenu {
    private boolean we = false;
    private boolean be = false;
    GameMenu(String name, Board board, Table table){
        super(name);
        JMenuItem restart = new JMenuItem("Restart");
        add(restart);
        restart.addActionListener(e -> {
            Game game = new Game();
            table.setGame(game);
            table.setBoard(game.getBoard());
            game.startGame();
            table.newBoardPanel();
            table.drawPieces();
        });
        JMenuItem revertMove = new JMenuItem("Revert move");
        add(revertMove);
        revertMove.addActionListener(e -> {
            table.getBoard().revertMove(table.getGame().getPlayer());
            table.drawBoard(board);
        });
        JMenuItem enableWhiteThreatMap = new JMenuItem("White threatmap");
        add(enableWhiteThreatMap);
        enableWhiteThreatMap.addActionListener(e -> {
            if(!we){
                table.getBoardPanel().getThreatMap().addAll(board.getWhiteThreatMap());
            }else {
                table.getBoardPanel().getThreatMap().clear();
            }
            we = !we;
            table.getBoardPanel().drawBoard(board);
        });
        JMenuItem enableBlackThreatMap = new JMenuItem("Black threatmap");
        add(enableBlackThreatMap);
        enableBlackThreatMap.addActionListener(e -> {
            if(!be){
                table.getBoardPanel().getThreatMap().addAll(board.getBlackThreatMap());
            }else {
                table.getBoardPanel().getThreatMap().clear();
            }
            be = !be;
            table.getBoardPanel().drawBoard(board);
        });
    }
}
