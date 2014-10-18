package com.dubhacks.maps_mmo.client.gui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartPanel extends JPanel {

    /**
     * Create the frame.
     */
    public StartPanel(final JPanel cards) {
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gbl_contentPane);

        JLabel lblMapsMmoGame = new JLabel("Maps MMO Game");
        GridBagConstraints gbc_lblMapsMmoGame = new GridBagConstraints();
        gbc_lblMapsMmoGame.insets = new Insets(0, 0, 5, 0);
        gbc_lblMapsMmoGame.gridx = 0;
        gbc_lblMapsMmoGame.gridy = 0;
        add(lblMapsMmoGame, gbc_lblMapsMmoGame);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                CardLayout cl = (CardLayout)cards.getLayout();
                cl.show(cards, "Connect");
            }
        });
        GridBagConstraints gbc_btnConnect = new GridBagConstraints();
        gbc_btnConnect.insets = new Insets(0, 0, 5, 0);
        gbc_btnConnect.gridx = 0;
        gbc_btnConnect.gridy = 1;
        add(btnConnect, gbc_btnConnect);

        JButton btnAbout = new JButton("About");
        btnAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout)cards.getLayout();
                cl.show(cards, "About");
            }
        });
        GridBagConstraints gbc_btnAbout = new GridBagConstraints();
        gbc_btnAbout.insets = new Insets(0, 0, 5, 0);
        gbc_btnAbout.gridx = 0;
        gbc_btnAbout.gridy = 2;
        add(btnAbout, gbc_btnAbout);
    }

}
