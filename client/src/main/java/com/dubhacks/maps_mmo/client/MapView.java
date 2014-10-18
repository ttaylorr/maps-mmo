package com.dubhacks.maps_mmo.client;

import java.awt.Graphics2D;

public class MapView {
    private final Graphics2D g;
    private final Bounds bounds;
    private final double scaleX;
    private final double scaleY;

    public MapView(Graphics2D graphics, Bounds bounds) {
        g = graphics;
        this.bounds = bounds;
        scaleX = g.getClipBounds().getWidth() / (bounds.max.getLongitude() - bounds.min.getLongitude());
        scaleY = g.getClipBounds().getHeight() / (bounds.max.getLatitude() - bounds.min.getLatitude());
    }

    public Bounds getBounds() {
        return bounds;
    }

    private double getOffsetX() {
        return bounds.min.getLongitude();
    }

    private double getOffsetY() {
        return bounds.min.getLatitude();
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        g.drawLine((int)((x1 - getOffsetX()) * scaleX),
                   (int)(g.getClipBounds().getHeight() - 1 - (y1 - getOffsetY()) * scaleY),
                   (int)((x2 - getOffsetX()) * scaleX),
                   (int)(g.getClipBounds().getHeight() - 1 - (y2 - getOffsetY()) * scaleY));
    }
}
