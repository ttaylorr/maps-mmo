package com.dubhacks.maps_mmo.client;

import java.util.Iterator;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;

import com.dubhacks.map_mmo.net.SocketPlayer;
import com.dubhacks.maps_mmo.event.EventManager;

public class Game {
    private final EventManager eventManager;

    private final FeatureCollection roads;
    private Bounds bounds;
    private GamePanel panel;

    private SocketPlayer connectingPlayer;
    private ClientPlayer player;

    public Game(EventManager eventManager, FeatureCollection roads) {
        this.eventManager = eventManager;
        this.roads = roads;
    }

    public void setPanel(GamePanel panel) {
        this.panel = panel;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds newBounds) {
        bounds = newBounds;
        repaint();
    }

    public void repaint() {
        if (panel != null) {
            panel.repaint();
        }
    }

    public void paint(MapView view) {
        for (Feature road : roads.getFeatures()) {
            if (road.getGeometry() instanceof LineString) {
                LineString str = (LineString)road.getGeometry();
                Iterator<LngLatAlt> coords = str.getCoordinates().iterator();
                LngLatAlt prev = coords.next();
                while (coords.hasNext()) {
                    LngLatAlt next = coords.next();
                    view.drawLine(prev.getLongitude(), prev.getLatitude(), next.getLongitude(), next.getLatitude());
                    prev = next;
                }
            }
        }
    }

    public ClientPlayer getPlayer() {
        return player;
    }

    public void setConnectingPlayer(SocketPlayer socketPlayer) {
        connectingPlayer = socketPlayer;
    }

    public void setPlayer(ClientPlayer player) {
        this.player = player;
    }

    public void tick() {
        if (connectingPlayer != null) {
            while (connectingPlayer.hasPacket()) {
                eventManager.dispatch(connectingPlayer, connectingPlayer.getNextPacket());
            }
        }
        if (player != null) {
            while (player.getSocketPlayer().hasPacket()) {
                eventManager.dispatch(player, player.getSocketPlayer().getNextPacket());
            }
        }
    }
}
