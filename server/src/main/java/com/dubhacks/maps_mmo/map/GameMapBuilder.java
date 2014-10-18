package com.dubhacks.maps_mmo.map;

import com.dubhacks.maps_mmo.map.classifiers.*;
import com.dubhacks.maps_mmo.map.renderers.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.geojson.*;

public class GameMapBuilder {
    private final Multimap<GeoJsonFileType, File> files = TreeMultimap.create();
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
    
    public void addFile(GeoJsonFileType type, File file) {
        if (type != null) {
            this.files.put(type, file);
        }
    }

    public GameMap process() throws IOException {
        GeoJsonFileType[] types = GeoJsonFileType.values();
        for (GeoJsonFileType type : types) {
            if (!this.files.containsKey(type)) {
                throw new IllegalStateException("must set a GeoJSON file for " + type + " before rendering");
            }
        }

        System.out.print("Calculating map parameters...");
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
            for (File file : this.files.get(renderer.getFileType())) {
                renderer.render(new ObjectMapper().readValue(file, FeatureCollection.class).getFeatures());
            }
            System.out.print(" (Finished in "+(System.currentTimeMillis() - start)+"ms)\n");
        }

        this.tiles = null;
        this.mapInfo = null;

        return gameMap;
    }

    private MapInfo calculateMapParameters() throws IOException {
        MinMaxLngLat mm = new MinMaxLngLat();

        List<Classifier> classifiers = Arrays.asList(new RoadClassifier(), new WaterClassifier(), new LandUsageClassifier(), new BuildingClassifier());
        for (Classifier classifier : classifiers) {
            classifier.classifyFiles(mm, this.files.get(classifier.getType()));
        }

        return new MapInfo(mm, this.resolution);
    }
}
