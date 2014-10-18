package com.dubhacks.maps_mmo.map.renderers;

import com.dubhacks.maps_mmo.map.GameMapBuilder;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import org.geojson.*;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class Renderer {
    protected static final int BINARY_IMAGE_SET = 0xffffffff;

    protected final GameMapBuilder.MapParameters mapParameters;

    public Renderer(GameMapBuilder.MapParameters mapParameters) {
        this.mapParameters = mapParameters;
    }

    public abstract void render(byte[][] tiles, List<File> files) throws IOException;

    public abstract GeoJsonFileType getFileType();

    protected void draw(LineString line, BufferedImage image, GameMapBuilder.MapParameters params) {
        draw(line, 1, image, params);
    }

    protected void draw(LineString line, float width, BufferedImage image, GameMapBuilder.MapParameters params) {
        Graphics2D g = image.createGraphics();
        g.setStroke(new BasicStroke(width));
        List<LngLatAlt> coords = line.getCoordinates();
        for (int i = 0; i < coords.size() - 1; i++) {
            Point start = normalize(coords.get(i), params);
            Point end = normalize(coords.get(i + 1), params);
            g.drawLine(start.x, start.y, end.x, end.y);
        }
        g.dispose();
    }

    protected void draw(org.geojson.Polygon poly, BufferedImage image, GameMapBuilder.MapParameters params) {
        Graphics2D g = image.createGraphics();
        LinkedList<Point> points = new LinkedList<>();
        for (LngLatAlt coord : poly.getExteriorRing()) {
            points.add(normalize(coord, params));
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

    protected void draw(MultiPolygon multiPoly, BufferedImage image, GameMapBuilder.MapParameters params) {
        Graphics2D g = image.createGraphics();
        for (List<List<LngLatAlt>> poly : multiPoly.getCoordinates()) {
            LinkedList<Point> points = new LinkedList<>();
            for (LngLatAlt coord : poly.get(0)) {
                points.add(normalize(coord, params));
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

    private Point normalize(LngLatAlt point, GameMapBuilder.MapParameters params) {
        Point p = new Point();
        p.x = (int) ((point.getLongitude() - params.longitude.getMin()) / params.longitude.getRange() * (double)params.width);
        p.y = (int) ((point.getLatitude() - params.latitude.getMin()) / params.latitude.getRange() * (double)params.height);
        p.y = params.height - 1 - p.y;
        return p;
    }
}
