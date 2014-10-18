package com.dubhacks.maps_mmo.map.renderers;

import com.dubhacks.maps_mmo.map.GameMap;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import com.dubhacks.maps_mmo.map.MapInfo;
import org.geojson.*;
import org.geojson.Polygon;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

public abstract class Renderer {
    protected static final int BINARY_IMAGE_SET = 0xffffffff;

    protected final GameMap map;

    public Renderer(GameMap map) {
        this.map = map;
    }

    protected BufferedImage allocateImage() {
        return new BufferedImage(this.map.info.width, this.map.info.height, BufferedImage.TYPE_BYTE_BINARY);
    }

    public abstract void render(List<Feature> features) throws IOException;

    public abstract GeoJsonFileType getFileType();

    protected void draw(LineString line, BufferedImage image) {
        this.draw(line, 1, image);
    }

    protected void draw(LineString line, float width, BufferedImage image) {
        Graphics2D g = image.createGraphics();
        g.setStroke(new BasicStroke(width));

        List<LngLatAlt> coords = line.getCoordinates();
        for (int i = 0; i < coords.size() - 1; i++) {
            Point start = normalize(coords.get(i), this.map.info);
            Point end = normalize(coords.get(i + 1), this.map.info);
            g.drawLine(start.x, start.y, end.x, end.y);
        }
        g.dispose();
    }

    protected void draw(Polygon poly, BufferedImage image) {
        Graphics2D g = image.createGraphics();
        LinkedList<Point> points = new LinkedList<>();
        for (LngLatAlt coord : poly.getExteriorRing()) {
            points.add(normalize(coord, this.map.info));
        }

        GeneralPath polyLine = new GeneralPath(GeneralPath.WIND_EVEN_ODD, points.size());
        Iterator<Point> iter = points.iterator();
        Point p = iter.next();
        polyLine.moveTo(p.x, p.y);
        while (iter.hasNext()) {
            p = iter.next();
            polyLine.lineTo(p.x, p.y);
        }
        polyLine.closePath();
        g.fill(polyLine);
        g.dispose();
    }

    protected void draw(MultiPolygon multiPoly, BufferedImage image) {
        Graphics2D g = image.createGraphics();
        for (List<List<LngLatAlt>> poly : multiPoly.getCoordinates()) {
            LinkedList<Point> points = new LinkedList<>();
            for (LngLatAlt coord : poly.get(0)) {
                points.add(normalize(coord, this.map.info));
            }
            GeneralPath polyLine = new GeneralPath(GeneralPath.WIND_EVEN_ODD, points.size());
            Iterator<Point> iter = points.iterator();
            Point p = iter.next();
            polyLine.moveTo(p.x, p.y);
            while (iter.hasNext()) {
                p = iter.next();
                polyLine.lineTo(p.x, p.y);
            }
            polyLine.closePath();
            g.fill(polyLine);
        }
        g.dispose();
    }

    protected void write(BufferedImage image) {
        for (int xPos = 0; xPos < this.map.info.width; xPos++) {
            for (int yPos = 0; yPos < this.map.info.width; yPos++) {
                if (image.getRGB(xPos, yPos) == BINARY_IMAGE_SET) {
                    this.map.set(xPos, yPos, this.getFileType());
                }
            }
        }
    }

    private Point normalize(LngLatAlt point, MapInfo info) {
        Point p = new Point();
        p.x = (int) ((point.getLongitude() - info.longitude.getMin()) / info.longitude.getRange() * (double)info.width);
        p.y = (int) ((point.getLatitude() - info.latitude.getMin()) / info.latitude.getRange() * (double)info.height);
        p.y = info.height - 1 - p.y;
        return p;
    }
}
