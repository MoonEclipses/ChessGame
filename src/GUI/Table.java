package GUI;

import Game.Board;
import Game.Color;
import Game.Game;
import Game.Tile;
import Piecies.King;
import Piecies.Pawn;
import Piecies.Piece;
import Piecies.Position;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.swing.SwingUtilities.*;

public class Table {
    private JFrame frame;
    private BoardPanel boardPanel;
    private Board board;
    private Game game;

    private Tile srcTile;
    private Position distPos;
    private Piece movedPiece;
    private Set<Position> movedPieceLegalMoves;
    private Set<Position> movedPieceAttackMoves;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);

    public Table(Game game) {
        this.board = game.getBoard();
        this.game = game;
        this.frame = new JFrame("Chess");
        JMenuBar tableMenuBar = new JMenuBar();
        populateMenuBar(tableMenuBar);
        this.frame.setJMenuBar(tableMenuBar);

        this.boardPanel = new BoardPanel();
        this.frame.add(this.boardPanel, BorderLayout.CENTER);
        this.frame.setSize(OUTER_FRAME_DIMENSION);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    private void populateMenuBar(JMenuBar tableMenuBar) {
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createGameMenu());
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        fileMenu.add(open);
        return fileMenu;
    }

    private JMenu createGameMenu() {
        GameMenu gameMenu = new GameMenu("Game", board, this);
        return gameMenu;
    }

    public void drawPieces() {
        boardPanel.assignPieces();
    }

    public void drawBoard(Board board) {
        boardPanel.drawBoard(board);
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void newBoardPanel() {
        this.frame.remove(boardPanel);
        boardPanel = new BoardPanel();
        this.frame.add(this.boardPanel, BorderLayout.CENTER);
        this.frame.setSize(OUTER_FRAME_DIMENSION);
        this.frame.validate();
        this.frame.repaint();
    }

    public class BoardPanel extends JPanel {
        List<TilePanel> boardTiles;
        Set<Position> threatMap;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            this.threatMap = new HashSet<>();
            for (int i = 0; i < 8; i++) {
                for (int j = 7; j >= 0; j--) {
                    int k = i;
                    int p = j;
                    if (game.getPlayer().getColor() == Color.WHITE) {
                        k = 7 - k;
                        p = 7 - j;
                    }
                    TilePanel tilePanel = new TilePanel(this, p, k);
                    this.boardTiles.add(tilePanel);
                    add(tilePanel);
                }
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        private void assignPieces() {
            for (TilePanel tilePanel : boardTiles) {
                tilePanel.assignTilePieceIcon();
            }
            validate();
        }

        public void drawBoard(Board board) {
            removeAll();
            for (TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                if(threatMap.contains(tilePanel.position)){
                    tilePanel.setBorder(BorderFactory.createLineBorder(java.awt.Color.GREEN));
                }
                add(tilePanel);
            }
            validate();
            repaint();
        }

        public Set<Position> getThreatMap() {
            return threatMap;
        }
    }

    private class TilePanel extends JPanel {
        private final static java.awt.Color LIGHT_TILE_COLOR = java.awt.Color.decode("#EEC0A8");
        private final static java.awt.Color DARK_TILE_COLOR = java.awt.Color.decode("#693F28");
        private final static String BASE_ICON_PATH = "assets/pieces/";
        private final static Border YELLOW_BORDER = BorderFactory.createLineBorder(java.awt.Color.YELLOW);
        private final static Border BLUE_BORDER = BorderFactory.createLineBorder(java.awt.Color.BLUE);
        private Position position;

        TilePanel(BoardPanel boardPanel, int x, int y) {
            super(new GridBagLayout());
            this.position = Position.pos(x, y);
            setPreferredSize(TILE_PANEL_DIMENSION);
            assingTileColor();

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        clearVars();
                    } else if (isLeftMouseButton(e)) {
                        if (srcTile == null) {//first click
                            srcTile = board.getTile(position);
                            movedPiece = srcTile.getPiece();
                            if (movedPiece == null) {
                                srcTile = null;
                            }else if(board.isCheckMate() || board.isDraw()){
                                clearVars();
                            } else {
                                if (game.getPlayer().getColor() == movedPiece.getColor() && game.getPlayer().isCurrentMove() ||
                                        game.getPlayer().getColor() != movedPiece.getColor() && !game.getPlayer().isCurrentMove()) {
                                    movedPieceLegalMoves = movedPiece.calculateLegalMoves(board);
                                } else {
                                    srcTile = null;
                                }
                            }
                        } else {//second click
                            distPos = position;
                            if (movedPiece.move(distPos, board)) {
                                if (game.getPlayer().isCurrentMove()) {
                                    game.getPlayer().setCurrentMove(false);
                                } else {
                                    game.getPlayer().setCurrentMove(true);
                                }
                                if (movedPiece instanceof Pawn) {
                                    if (((Pawn) movedPiece).canBePromoted()) {
                                        new PromotionFrame("Promotion", movedPiece, board, boardPanel);
                                    }
                                }
                                if(board.isCheckMate()){
                                    if(((King)board.getWhiteKing()).isChecked(board)){
                                        new EndGameFrame("Game Over","Black wins");
                                    }else {
                                        new EndGameFrame("Game Over","White wins");
                                    }
                                }else if(board.isDraw()){
                                    new EndGameFrame("Game Over","Draw");
                                }
                                clearVars();
                            } else {
                                clearVars();
                            }
                        }
                    } else if (isMiddleMouseButton(e)) {
                        srcTile = board.getTile(position);
                        movedPiece = srcTile.getPiece();
                        if (movedPiece != null) movedPieceAttackMoves = movedPiece.getAttackedPositions();
                        srcTile = null;
                        movedPiece = null;
                    }
                    SwingUtilities.invokeLater(() -> boardPanel.drawBoard(board));

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            validate();
        }

        private void clearVars() {
            srcTile = null;
            movedPiece = null;
            distPos = null;
            movedPieceLegalMoves = null;
            movedPieceAttackMoves = null;
        }

        private void assignTilePieceIcon() {
            this.removeAll();
            Tile tile = board.getTile(position);
            if (tile.isOcupied()) {
                Piece piece = tile.getPiece();
                try {
                    String path = BASE_ICON_PATH + piece.getColor().name() + piece.getType() + ".png";
                    File file = new File(path);
                    BufferedImage image = ImageIO.read(file);
                    Image newimg = image.getScaledInstance(60, 60, Image.SCALE_REPLICATE);
                    ImageIcon imageIcon = new ImageIcon(newimg);
                    JLabel pieceIco = new JLabel(imageIcon);//https://www.youtube.com/watch?v=EH35McP9KlU
                    add(pieceIco);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assingTileColor() {
            Tile[][] tiles = board.getTileBoard();
            if (tiles[position.x][position.y].getColor() == Color.WHITE) {
                setBackground(LIGHT_TILE_COLOR);
            } else {
                setBackground(DARK_TILE_COLOR);
            }
            if (movedPieceLegalMoves != null && movedPieceLegalMoves.contains(position)) {
                setBorder(YELLOW_BORDER);
            } else if (movedPieceAttackMoves != null && movedPieceAttackMoves.contains(position)) {
                setBorder(BLUE_BORDER);
            } else {
                setBorder(null);
            }
        }

        public void drawTile(Board board) {
            assingTileColor();
            assignTilePieceIcon();
            validate();
            repaint();
        }
    }
}
