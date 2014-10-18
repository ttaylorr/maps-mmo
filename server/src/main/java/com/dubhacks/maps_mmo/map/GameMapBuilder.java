package com.dubhacks.maps_mmo.map;

import com.dubhacks.maps_mmo.map.classifiers.*;
import com.dubhacks.maps_mmo.map.renderers.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiPolygon;
import org.geojson.Polygon;

public class GameMapBuilder {
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

        System.out.print("Calculating map parameters...");
        this.mapParameters = this.calculateMapParameters();
        System.out.printf("width: %d    height: %d\n", mapParameters.width, mapParameters.height);
        System.out.printf("LNG[x]  min:%10.6f  max:%10.6f\n", mapParameters.latitude.getMin(), mapParameters.latitude.getMax());
        System.out.printf("LAT[y]  min:%10.6f  max:%10.6f\n", mapParameters.longitude.getMin(), mapParameters.longitude.getMax());
        System.out.println();

        System.out.println("Allocating map...");
        this.tiles = new byte[mapParameters.height][mapParameters.width];

        List<Renderer> renderers = new ArrayList<>();
        renderers.add(new BuildingRenderer(mapParameters));
        renderers.add(new WaterRenderer(mapParameters));
        renderers.add(new RoadRenderer(mapParameters));

        for (Renderer renderer : renderers) {
            System.out.print("Starting render of type: "+renderer.getClass().getSimpleName()+"...");
            long start = System.currentTimeMillis();
            renderer.render(tiles, this.files.get(renderer.getFileType()));
            System.out.print(" (Finished in "+(System.currentTimeMillis() - start)+"ms)\n");
        }

        GameMap gameMap = new GameMap(this.tiles);
        this.tiles = null;
        this.mapParameters = null;

        return gameMap;
    }

    private MapParameters calculateMapParameters() throws IOException {
        MinMaxLngLat mm = new MinMaxLngLat();

        List<Classifier> classifiers = Arrays.asList(new RoadClassifier(), new WaterClassifier(), new LandUsageClassifier(), new BuildingClassifier());
        for (Classifier classifier : classifiers) {
            classifier.classifyFiles(mm, this.files.get(classifier.getType()));
        }

        return new MapParameters(mm, this.resolution);
    }

    public static class MapParameters {
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
    
    public static class MinMax {
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
    
    public static class MinMaxLngLat {
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

        gmb.addFile(GeoJsonFileType.Water, new File("geojson/seattle_washington-waterareas.geojson"));
        gmb.addFile(GeoJsonFileType.Roads, new File("geojson/seattle_washington-roads.geojson"));
        gmb.addFile(GeoJsonFileType.Water, new File("geojson/seattle_washington-waterareas_gen1.geojson"));
        gmb.addFile(GeoJsonFileType.Water, new File("geojson/seattle_washington-waterareas_gen0.geojson"));
        gmb.addFile(GeoJsonFileType.Water, new File("geojson/seattle_washington-waterways.geojson"));
        gmb.addFile(GeoJsonFileType.LandUsages, new File("geojson/seattle_washington-landusages.geojson"));
        gmb.addFile(GeoJsonFileType.Buildings, new File("geojson/seattle_washington-buildings.geojson"));
        
        GameMap map = gmb.render();

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
