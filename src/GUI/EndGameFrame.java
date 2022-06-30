package GUI;

import Piecies.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndGameFrame extends JFrame {
    public EndGameFrame(String title,String message) throws HeadlessException {
        super(title);
        setSize(new Dimension(600, 200));
        JPanel panel = new JPanel();
        JButton ok = new JButton("ok");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               dispose();
            }
        });
        TextField textField = new TextField();
        textField.setText(message);
        textField.setFont( new Font("Times new Roman", Font.PLAIN,38) );
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
        panel.setSize(600,150);
        panel.add(textField, BorderLayout.CENTER);
        panel.setSize(600,100);
        add(panel,BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        ok.setSize(new Dimension(50,20));
        buttonPanel.add(ok);
        add(buttonPanel,BorderLayout.SOUTH);
        setUndecorated(true);
        setVisible(true);
        validate();
    }
}
