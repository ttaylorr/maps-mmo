package com.dubhacks.maps_mmo.client.gui;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.dubhacks.maps_mmo.client.Game;
import com.dubhacks.maps_mmo.client.listeners.MapPacketListener;
import com.dubhacks.maps_mmo.event.EventManager;
import com.dubhacks.maps_mmo.event.Listener;

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
        eventManager.addListener(new MapPacketListener());
        Game game = new Game(eventManager);

        GamePanel gamePanel = new GamePanel(game);
        panel.add(gamePanel, "Game");
        panel.add(new ConnectPanel(game, panel), "Connect");

        Timer timer = new Timer(1000 / 60, new TimerListener(game, gamePanel));
        timer.start();
    }

}
