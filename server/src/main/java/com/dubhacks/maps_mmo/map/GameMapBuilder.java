package com.dubhacks.maps_mmo.map;

import com.dubhacks.maps_mmo.core.map.GameMap;
import com.dubhacks.maps_mmo.core.map.MapInfo;
import com.dubhacks.maps_mmo.core.map.MinMaxLngLat;
import com.dubhacks.maps_mmo.map.classifiers.*;
import com.dubhacks.maps_mmo.map.renderers.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.*;

public class GameMapBuilder {

    private final Set<FeatureCollection> features = new HashSet<>();
    private final double resolution;

    private MapInfo mapInfo;
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

    public void source(String s) throws IOException {
        URL url = new URL(s);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        this.features.add(new ObjectMapper().readValue(conn.getInputStream(), FeatureCollection.class));
    }

    public GameMap process() throws IOException {
        System.out.println("Calculating map parameters...");
        this.mapInfo = this.calculateMapParameters();
        System.out.printf("width: %d    height: %d\n", mapInfo.width, mapInfo.height);
        System.out.printf("LNG[x]  min:%10.6f  max:%10.6f\n", mapInfo.latitude.getMin(), mapInfo.latitude.getMax());
        System.out.printf("LAT[y]  min:%10.6f  max:%10.6f\n", mapInfo.longitude.getMin(), mapInfo.longitude.getMax());
        System.out.println();

        System.out.println("Allocating map...");
        this.tiles = new byte[mapInfo.height][mapInfo.width];
        GameMap gameMap = new GameMap(this.tiles, this.mapInfo);

        List<Renderer> renderers = new ArrayList<>();
        renderers.add(new BuildingRenderer(gameMap));
        renderers.add(new WaterRenderer(gameMap));
        renderers.add(new RoadRenderer(gameMap));

        for (Renderer renderer : renderers) {
            System.out.print("Starting render of type: " + renderer.getClass().getSimpleName() + "...");
            long start = System.currentTimeMillis();
            for (FeatureCollection features : this.features) {
                renderer.render(features.getFeatures());
            }
            System.out.print(" (Finished in "+(System.currentTimeMillis() - start)+"ms)\n");
        }

        this.tiles = null;
        this.mapInfo = null;

        return gameMap;
    }

    private MapInfo calculateMapParameters() throws IOException {
        MinMaxLngLat mm = new MinMaxLngLat();

        for (FeatureCollection collection : this.features) {
            for (Feature f : collection.getFeatures()) {
                GeoJsonObject geometry = f.getGeometry();
                if (geometry instanceof Polygon) {
                    Classifiers.put(mm, (Polygon) geometry);
                } else if (geometry instanceof LineString) {
                    Classifiers.put(mm, (LineString) geometry);
                } else if (geometry instanceof MultiPolygon) {
                    Classifiers.put(mm, (MultiPolygon) geometry);
                }
            }
        }

        return new MapInfo(mm, this.resolution);
    }
}
