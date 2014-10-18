package com.dubhacks.maps_mmo.client.gui;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.dubhacks.maps_mmo.client.ConnectListener;
import com.dubhacks.maps_mmo.client.Game;
import com.dubhacks.maps_mmo.client.GameAssets;
import com.dubhacks.maps_mmo.client.PacketListener;
import com.dubhacks.maps_mmo.event.EventManager;

public class MainWindow {

    private JFrame frmMapsMmo;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainWindow window = new MainWindow();
                    window.frmMapsMmo.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        try {
            GameAssets.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        frmMapsMmo = new JFrame();
        frmMapsMmo.setTitle("Maps MMO");
        frmMapsMmo.setBounds(100, 100, 450, 300);
        frmMapsMmo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMapsMmo.getContentPane().setLayout(new CardLayout(0, 0));

        JPanel panel = new JPanel();
        frmMapsMmo.getContentPane().add(panel, "name_255863797250686");
        panel.setLayout(new CardLayout(0, 0));

        StartPanel startPanel = new StartPanel(panel);
        panel.add(startPanel, "name_255977455118084");

        EventManager eventManager = new EventManager();
        Game game = new Game(eventManager);

        GamePanel gamePanel = new GamePanel(game);
        panel.add(gamePanel, "Game");
        panel.add(new ConnectPanel(game, panel), "Connect");

        Timer timer = new Timer(1000 / 60, new TimerListener(game, gamePanel));
        timer.start();

        eventManager.addListener(new ConnectListener(game));
        eventManager.addListener(new PacketListener(game));
    }

}
