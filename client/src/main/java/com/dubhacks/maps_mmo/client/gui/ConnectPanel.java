package com.dubhacks.maps_mmo.client.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.dubhacks.map_mmo.net.NetworkDefaults;
import com.dubhacks.maps_mmo.client.Game;

public class ConnectPanel extends JPanel {
    private final Game game;

    private final JTextField txtHostname;
    private final JTextField txtPort;
    private final JTextField txtUsername;

    /**
     * Create the panel.
     */
    public ConnectPanel(final Game game) {
        this.game = game;

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 1.0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        JLabel lblConnectToA = new JLabel("Connect to a Server");
        lblConnectToA.setFont(new Font("Tahoma", Font.PLAIN, 48));
        GridBagConstraints gbc_lblConnectToA = new GridBagConstraints();
        gbc_lblConnectToA.insets = new Insets(0, 0, 5, 0);
        gbc_lblConnectToA.gridx = 0;
        gbc_lblConnectToA.gridy = 0;
        gbc_lblConnectToA.gridwidth = 2;
        add(lblConnectToA, gbc_lblConnectToA);

                JLabel lblUsername = new JLabel("Username");
                GridBagConstraints gbc_lblUsername = new GridBagConstraints();
                gbc_lblUsername.anchor = GridBagConstraints.WEST;
                gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
                gbc_lblUsername.gridx = 0;
                gbc_lblUsername.gridy = 1;
                add(lblUsername, gbc_lblUsername);

                txtUsername = new JTextField();
                GridBagConstraints gbc_txtUsername = new GridBagConstraints();
                gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
                gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
                gbc_txtUsername.gridx = 1;
                gbc_txtUsername.gridy = 1;
                add(txtUsername, gbc_txtUsername);
                txtUsername.setColumns(10);

                JLabel lblHostname = new JLabel("Hostname");
                GridBagConstraints gbc_lblHostname = new GridBagConstraints();
                gbc_lblHostname.anchor = GridBagConstraints.WEST;
                gbc_lblHostname.insets = new Insets(0, 0, 5, 5);
                gbc_lblHostname.gridx = 0;
                gbc_lblHostname.gridy = 2;
                add(lblHostname, gbc_lblHostname);

                txtHostname = new JTextField();
                txtHostname.setText("localhost");
                GridBagConstraints gbc_txtHostname = new GridBagConstraints();
                gbc_txtHostname.fill = GridBagConstraints.HORIZONTAL;
                gbc_txtHostname.insets = new Insets(0, 0, 5, 0);
                gbc_txtHostname.gridx = 1;
                gbc_txtHostname.gridy = 2;
                add(txtHostname, gbc_txtHostname);
                txtHostname.setColumns(10);

        JLabel lblPort = new JLabel("Port");
        GridBagConstraints gbc_lblPort = new GridBagConstraints();
        gbc_lblPort.anchor = GridBagConstraints.WEST;
        gbc_lblPort.insets = new Insets(0, 0, 5, 5);
        gbc_lblPort.gridx = 0;
        gbc_lblPort.gridy = 3;
        add(lblPort, gbc_lblPort);

        txtPort = new JTextField();
        txtPort.setText(Integer.toString(NetworkDefaults.DEFAULT_PORT));
        GridBagConstraints gbc_txtPort = new GridBagConstraints();
        gbc_txtPort.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtPort.insets = new Insets(0, 0, 5, 0);
        gbc_txtPort.gridx = 1;
        gbc_txtPort.gridy = 3;
        add(txtPort, gbc_txtPort);
        txtPort.setColumns(10);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    game.connect(txtHostname.getText(), Integer.parseInt(txtPort.getText()), txtUsername.getText());
                } catch (IOException e) {
                    System.err.println("ERROR connecting to server: " + e.getMessage());
                }
            }
        });
        GridBagConstraints gbc_btnConnect = new GridBagConstraints();
        gbc_btnConnect.anchor = GridBagConstraints.EAST;
        gbc_btnConnect.gridx = 1;
        gbc_btnConnect.gridy = 4;
        add(btnConnect, gbc_btnConnect);

    }

}
