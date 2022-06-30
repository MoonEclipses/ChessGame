package GUI;

import Game.Board;
import Piecies.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PromotionFrame extends JFrame {
    private String choosenPiece;
    private PiecePanel queen;
    private PiecePanel rook;
    private PiecePanel knight;

    public PromotionFrame(String title, Piece piece, Board board, Table.BoardPanel boardPanel) throws HeadlessException {
        super(title);
        setSize(new Dimension(300, 125));
        JPanel choosePanel = new JPanel();
        JButton promote = new JButton("promote");
        promote.setEnabled(false);
        promote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Piece pieceToPromote;
                if (choosenPiece == "Queen"){
                    pieceToPromote = new Queen(null,piece.getColor());
                }else if(choosenPiece == "Rook"){
                    pieceToPromote = new Rook(null,piece.getColor());
                }else {
                    pieceToPromote = new Knight(null,piece.getColor());
                }
                ((Pawn) piece).promote(pieceToPromote, board);
                board.recalculateAttackingPositions();
                dispose();
                boardPanel.drawBoard(board);
            }
        });
        Game.Color color;
        if (piece.getColor() == Game.Color.WHITE) {
            color = Game.Color.WHITE;
        } else {
            color = Game.Color.BLACK;
        }
        queen = new PiecePanel(promote, new Queen(null, color));
        rook = new PiecePanel(promote, new Rook(null, color));
        knight = new PiecePanel(promote, new Knight(null, color));
        choosePanel.add(queen);
        choosePanel.add(rook);
        choosePanel.add(knight);
        add(choosePanel,BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel();
        promote.setSize(new Dimension(50,20));
        buttonPanel.add(promote);
        add(buttonPanel,BorderLayout.SOUTH);
        setUndecorated(true);
        setVisible(true);
        validate();
    }
    protected void redraw(){
        queen.drawPanel();
        knight.drawPanel();
        rook.drawPanel();
    }

    private class PiecePanel extends JPanel {
        Piece piece;

        public PiecePanel(JButton button, Piece piece) {
            super();
            this.piece = piece;
            String path = "assets/pieces/" + piece.getColor().name() + piece.getType() + ".png";
            File file = new File(path);
            BufferedImage image = null;
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image newimg = image.getScaledInstance(60, 60, Image.SCALE_REPLICATE);
            ImageIcon imageIcon = new ImageIcon(newimg);
            JLabel pieceIco = new JLabel(imageIcon);
            add(pieceIco);
            setSize(new Dimension(70, 70));
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (choosenPiece == null || choosenPiece != piece.getType()) {
                        choosenPiece = piece.getType();
                        setBorder(BorderFactory.createLineBorder(Color.PINK));
                        button.setEnabled(true);
                        redraw();
                    }
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

        private void drawPanel() {
            if (choosenPiece != piece.getType()) {
                setBorder(null);
            }
            repaint();
            validate();
        }
    }
}
