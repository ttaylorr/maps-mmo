package com.dubhacks.maps_mmo.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiPolygon;
import org.geojson.Polygon;

public class GameMapBuilder {
    
    private static final int BINARY_IMAGE_SET = 0xffffffff;
    
    private final Map<GeoJsonFileType, List<File>> files = new TreeMap<>();
    
    private final double resolution;
    
    private MapParameters mapParameters;
    private byte[][] tiles;
    
    public GameMapBuilder() {
        this(0.0001);
    }
    
    /**
     * 
     * @param resolution resolution in degrees
     */
    public GameMapBuilder(double resolution) {
        if (resolution <= 0 || resolution > 10) {
            throw new IllegalArgumentException("resolution must be between 0 and 10");
        }
        
        this.resolution = resolution;
    }
    
    public void addFile(GeoJsonFileType type, File file) {
        if (type != null) {
            List<File> list = this.files.get(type);
            if (list == null) {
                list = new LinkedList<>();
                this.files.put(type, list);
            }
            list.add(file);
        }
    }
    
    public GameMap render() throws IOException {
        GeoJsonFileType[] types = GeoJsonFileType.values();
        for (GeoJsonFileType type : types) {
            if (!this.files.containsKey(type)) {
                throw new IllegalStateException("must set a GeoJSON file for " + type + " before rendering");
            }
        }
        
        long time, totalTime = 0;
        
        System.out.print("Calculating map parameters...");
        time = System.currentTimeMillis();
        this.mapParameters = this.calculateMapParameters();
        time = System.currentTimeMillis() - time;
        totalTime += time;
        System.out.printf("done (%d ms)\n", time);
        System.out.printf("width: %d    height: %d\n", mapParameters.width, mapParameters.height);
        System.out.printf("LNG[x]  min:%10.6f  max:%10.6f\n", mapParameters.latitude.getMin(), mapParameters.latitude.getMax());
        System.out.printf("LAT[y]  min:%10.6f  max:%10.6f\n", mapParameters.longitude.getMin(), mapParameters.longitude.getMax());
        System.out.println();
        
        System.out.print("Allocating map...");
        time = System.currentTimeMillis();
        this.tiles = new byte[mapParameters.height][mapParameters.width];
        time = System.currentTimeMillis() - time;
        totalTime += time;
        System.out.printf("done (%d ms)\n", time);
        
        System.out.print("Rendering water...");
        time = System.currentTimeMillis();
        {
            BufferedImage waterImage = new BufferedImage(mapParameters.width, mapParameters.height, BufferedImage.TYPE_BYTE_BINARY);
            List<File> waterFiles = this.files.get(GeoJsonFileType.Water);
            for (File file : waterFiles) {
                FeatureCollection waterAreas = new ObjectMapper().readValue(file, FeatureCollection.class);
                List<Feature> waterAreasList = waterAreas.getFeatures();
                for (Feature waterArea : waterAreasList) {
                    GeoJsonObject geometry = waterArea.getGeometry();
                    if (geometry instanceof Polygon) {
                        draw((Polygon) geometry, waterImage, this.mapParameters);
                    } else if (geometry instanceof LineString) {
                        draw((LineString) geometry, waterImage, this.mapParameters);
                    }
                }
            }
            for (int x = 0; x < mapParameters.width; x++) {
                for (int y = 0; y < mapParameters.height; y++) {
                    if (waterImage.getRGB(x, y) == BINARY_IMAGE_SET) {
                        this.tiles[y][x] = GameMap.TERRAIN_WATER;
                    }
                }
            }
            ImageIO.write(waterImage, "bmp", new File("waterImage.bmp"));
        }
        time = System.currentTimeMillis() - time;
        totalTime += time;
        System.out.printf("done (%d ms)\n", time);
        
        System.out.print("Rendering roads...");
        time = System.currentTimeMillis();
        {
            BufferedImage roadImage = new BufferedImage(mapParameters.width, mapParameters.height, BufferedImage.TYPE_BYTE_BINARY);
            FeatureCollection roads = new ObjectMapper().readValue(this.files.get(GeoJsonFileType.Roads).get(0), FeatureCollection.class);
            List<Feature> roadList = roads.getFeatures();
            for (Feature road : roadList) {
                GeoJsonObject geometry = road.getGeometry();
                if (geometry instanceof LineString) {
                    draw((LineString) geometry, roadImage, this.mapParameters);
                }
            }
            for (int x = 0; x < mapParameters.width; x++) {
                for (int y = 0; y < mapParameters.height; y++) {
                    if (roadImage.getRGB(x, y) == BINARY_IMAGE_SET) {
                        this.tiles[y][x] = GameMap.ROAD_MEDIUM;
                    }
                }
            }
            ImageIO.write(roadImage, "bmp", new File("roadImage.bmp"));
        }
        time = System.currentTimeMillis() - time;
        totalTime += time;
        System.out.printf("done (%d ms)\n", time);
        
        System.out.print("Rendering buildings...");
        time = System.currentTimeMillis();
        {
            BufferedImage buildingImage = new BufferedImage(mapParameters.width, mapParameters.height, BufferedImage.TYPE_BYTE_BINARY);
            FeatureCollection buildings = new ObjectMapper().readValue(this.files.get(GeoJsonFileType.Buildings).get(0), FeatureCollection.class);
            List<Feature> buildingList = buildings.getFeatures();
            for (Feature building : buildingList) {
                GeoJsonObject geometry = building.getGeometry();
                if (geometry instanceof MultiPolygon) {
                    draw((MultiPolygon)geometry, buildingImage, this.mapParameters);
                } else if (geometry instanceof Polygon) {
                    draw((Polygon)geometry, buildingImage, this.mapParameters);
                } else if (geometry instanceof LineString) {
                    draw((LineString)geometry, buildingImage, this.mapParameters);
                }
            }
            for (int x = 0; x < mapParameters.width; x++) {
                for (int y = 0; y < mapParameters.height; y++) {
                    if (buildingImage.getRGB(x, y) == BINARY_IMAGE_SET) {
                        this.tiles[y][x] = GameMap.BUILDING_PLACEHOLDER;
                    }
                }
            }
            ImageIO.write(buildingImage, "bmp", new File("buildingImage.bmp"));
        }
        time = System.currentTimeMillis() - time;
        totalTime += time;
        System.out.printf("done (%d ms)\n", time);
        
        GameMap gameMap = new GameMap(this.tiles);
        this.tiles = null;
        this.mapParameters = null;
        
        System.out.printf("Finished rendering in %d ms\n", totalTime);
        
        return gameMap;
    }
    
    private static void draw(LineString line, BufferedImage image, MapParameters params) {
        draw(line, 1, image, params);
    }
    
    private static void draw(LineString line, float width, BufferedImage image, MapParameters params) {
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
    
    private static void draw(Polygon poly, BufferedImage image, MapParameters params) {
        Graphics2D g = image.createGraphics();
        LinkedList<Point> points = new LinkedList<>();
        for (LngLatAlt coord : poly.getExteriorRing()) {
            points.add(normalize(coord, params));
        }
//        for (List<LngLatAlt> ring : poly.getInteriorRings()) {
//            for (LngLatAlt coord : ring) {
//                points.add(normalize(coord, params));
//            }
//        }
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
    
    private static void draw(MultiPolygon multiPoly, BufferedImage image, MapParameters params) {
        Graphics2D g = image.createGraphics();
        for (List<List<LngLatAlt>> poly : multiPoly.getCoordinates()) {
            LinkedList<Point> points = new LinkedList<>();
//            for (List<LngLatAlt> ring : poly) {
//                for (LngLatAlt coord : ring) {
//                    points.add(normalize(coord, params));
//                }
//            }
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
    
    private static boolean[][] plotLine(Point p1, Point p2, boolean[][] map) {
        int dx = p2.x - p1.x;
        int dy = p2.y - p1.y;
        
        int D = 2 * dy - dx;
        
        map[p1.y][p1.x] = true;
        
        int y = p1.y;
        
        for (int x = p1.x + 1; x <= p2.x; x++) {
            if (D > 0) {
                y++;
                map[y][x] = true;
                D += 2 * dy - 2 * dx;
            } else {
                map[y][x] = true;
                D += 2 * dy;
            }
        }
        
        return map;
    }
    
    private static Point normalize(LngLatAlt point, MapParameters params) {
        Point p = new Point();
        p.x = (int) ((point.getLongitude() - params.longitude.getMin()) / params.longitude.getRange() * (double)params.width);
        p.y = (int) ((point.getLatitude() - params.latitude.getMin()) / params.latitude.getRange() * (double)params.height);
        p.y = params.height - 1 - p.y;
        return p;
    }
    
    private MapParameters calculateMapParameters() throws IOException {
        MinMaxLngLat mm = new MinMaxLngLat();
        
        // roads
        {
            FeatureCollection roads = new ObjectMapper().readValue(this.files.get(GeoJsonFileType.Roads).get(0), FeatureCollection.class);
            List<Feature> roadList = roads.getFeatures();
            for (Feature road : roadList) {
                GeoJsonObject geometry = road.getGeometry();
                if (geometry instanceof LineString) {
                    mm.put((LineString)geometry);
//                    LineString line = (LineString)geometry;
//                    List<LngLatAlt> coordinates = line.getCoordinates();
//                    for (LngLatAlt point : coordinates) {
//                        lng.put(point.getLongitude());
//                        lat.put(point.getLatitude());
//                    }
                } else {
                    throw new RuntimeException("road encountered with geometry: " + geometry.getClass().getCanonicalName());
                }
            }
        }
        
        // water areas
        {
            List<File> waterFiles = this.files.get(GeoJsonFileType.Water);
            for (File file : waterFiles) {
                FeatureCollection waterAreas = new ObjectMapper().readValue(file, FeatureCollection.class);
                List<Feature> waterAreasList = waterAreas.getFeatures();
                for (Feature waterArea : waterAreasList) {
                    GeoJsonObject geometry = waterArea.getGeometry();
                    if (geometry instanceof Polygon) {
                        mm.put((Polygon)geometry);
                    } else if (geometry instanceof LineString) {
                        mm.put((LineString)geometry);
                    } else if (geometry instanceof MultiPolygon) {
                        mm.put((MultiPolygon)geometry);
                    } else {
                        throw new RuntimeException("water area encountered with geometry: " + geometry.getClass().getCanonicalName() + " (type:" + waterArea.getProperty("type") + ")");
                    }
                }
            }
        }
        
        // land usages
        {
            FeatureCollection landUsages = new ObjectMapper().readValue(this.files.get(GeoJsonFileType.LandUsages).get(0), FeatureCollection.class);
            List<Feature> landUsagesList = landUsages.getFeatures();
            for (Feature landUsage : landUsagesList) {
                GeoJsonObject geometry = landUsage.getGeometry();
                if (geometry instanceof MultiPolygon) {
                    mm.put((MultiPolygon)geometry);
//                    MultiPolygon poly = (MultiPolygon)geometry;
//                    List<LngLatAlt> coordinates = poly.getExteriorRing();
//                    for (LngLatAlt point : coordinates) {
//                        lng.put(point.getLongitude());
//                        lat.put(point.getLatitude());
//                    }
                } else if (geometry instanceof LineString) {
                    mm.put((LineString)geometry);
                } else if (geometry instanceof Polygon) {
                    mm.put((Polygon)geometry);
                } else {
                    throw new RuntimeException("land usage encountered with geometry: " + geometry.getClass().getCanonicalName() + " (type:" + landUsage.getProperty("type") + ")");
                }
            }
        }
        
        // buildings
        {
            FeatureCollection buildings = new ObjectMapper().readValue(this.files.get(GeoJsonFileType.Buildings).get(0), FeatureCollection.class);
            List<Feature> buildingList = buildings.getFeatures();
            for (Feature building : buildingList) {
                GeoJsonObject geometry = building.getGeometry();
                if (geometry instanceof MultiPolygon) {
                    mm.put((MultiPolygon)geometry);
                } else if (geometry instanceof Polygon) {
                    mm.put((Polygon)geometry);
                } else if (geometry instanceof LineString) {
                    mm.put((LineString)geometry);
                } else {
                    throw new RuntimeException("building encountered with geometry: " + geometry.getClass().getCanonicalName() + " (type:" + building.getProperty("type") + ")");
                }
            }
        }
        
        return new MapParameters(mm, this.resolution);
    }
    
    private static class MapParameters {
        
        public final MinMax latitude;
        public final MinMax longitude;
        
        public final int width;
        public final int height;
        
        public MapParameters(MinMaxLngLat mm, double resolution) {
            this.latitude = mm.getLatitude();
            this.longitude = mm.getLongitude();
            
            this.height = (int) (this.latitude.getRange() / resolution);
            this.width = (int) (this.longitude.getRange() / resolution);
        }
        
    }
    
    private static class MinMax {
        
        private Double min;
        private Double max;
        
        public MinMax() {
            this.min = null;
            this.max = null;
        }
        
        public double getMin() {
            return this.min;
        }
        
        public double getMax() {
            return this.max;
        }
        
        public double getRange() {
            return this.max - this.min;
        }
        
        public void put(double d) {
            if (this.min == null) {
                this.min = d;
            } else if (d < this.min) {
                this.min = d;
            }
            
            if (this.max == null) {
                this.max = d;
            } else if (d > this.max) {
                this.max = d;
            }
        }
        
    }
    
    private static class MinMaxLngLat {
        
        private final MinMax lng = new MinMax();
        private final MinMax lat = new MinMax();
        
        public MinMax getLongitude() {
            return this.lng;
        }
        
        public MinMax getLatitude() {
            return this.lat;
        }
        
        public void put(LngLatAlt point) {
            this.lng.put(point.getLongitude());
            this.lat.put(point.getLatitude());
        }
        
        public void put(Polygon poly) {
            for (LngLatAlt point : poly.getExteriorRing()) {
                this.put(point);
            }
        }
        
        public void put(MultiPolygon multiPoly) {
            for (List<List<LngLatAlt>> poly : multiPoly.getCoordinates()) {
                for (List<LngLatAlt> ring : poly) {
                    for (LngLatAlt point : ring) {
                        this.put(point);
                    }
                }
            }
        }
        
        public void put(LineString line) {
            for (LngLatAlt point : line.getCoordinates()) {
                this.put(point);
            }
        }
        
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        GameMapBuilder gmb = new GameMapBuilder();
        
        gmb.addFile(GeoJsonFileType.Roads, new File("seattle_washington.imposm-geojson\\seattle_washington-roads.geojson"));
        gmb.addFile(GeoJsonFileType.Water, new File("seattle_washington.imposm-geojson\\seattle_washington-waterareas.geojson"));
        gmb.addFile(GeoJsonFileType.Water, new File("seattle_washington.imposm-geojson\\seattle_washington-waterareas_gen0.geojson"));
        gmb.addFile(GeoJsonFileType.Water, new File("seattle_washington.imposm-geojson\\seattle_washington-waterareas_gen1.geojson"));
        gmb.addFile(GeoJsonFileType.Water, new File("seattle_washington.imposm-geojson\\seattle_washington-waterways.geojson"));
        gmb.addFile(GeoJsonFileType.LandUsages, new File("seattle_washington.imposm-geojson\\seattle_washington-landusages.geojson"));
        gmb.addFile(GeoJsonFileType.Buildings, new File("seattle_washington.imposm-geojson\\seattle_washington-buildings.geojson"));
        
        GameMap map = gmb.render();
        
        System.out.print("Waiting for gc...");
        gmb = null;
        System.gc();
        Thread.sleep(10000);
        System.out.println("done");
        
        System.out.print("Writing to image...");
        writeToImage(map, "png", new File("gamemap.png"));
        System.out.println("done");
    }
    
    private static void writeToImage(GameMap map, String format, File imageFile) throws IOException {
        BufferedImage image = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = image.createGraphics();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                switch (map.get(x, y)) {
                    case GameMap.TERRAIN_WATER:
                        g.setColor(Color.darkGray);
                        g.drawLine(x, y, x, y);
                        break;
                    case GameMap.ROAD_MEDIUM:
                        g.setColor(Color.lightGray);
                        g.drawLine(x, y, x, y);
                        break;
                    case GameMap.BUILDING_PLACEHOLDER:
                        g.setColor(Color.white);
                        g.drawLine(x, y, x, y);
                        break;
                }
            }
        }
        g.dispose();
        ImageIO.write(image, format, imageFile);
    }
    
}
