package com.dubhacks.maps_mmo.map.renderers;

import com.dubhacks.maps_mmo.map.GameMap;
import com.dubhacks.maps_mmo.map.GameMapBuilder;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class RoadRenderer extends Renderer {
    public RoadRenderer(GameMapBuilder.MapParameters mapParameters) {
        super(mapParameters);
    }

    @Override
    public void render(byte[][] tiles, List<File> files) throws IOException {
        BufferedImage roadImage = new BufferedImage(mapParameters.width, mapParameters.height, BufferedImage.TYPE_BYTE_BINARY);
        FeatureCollection roads = new ObjectMapper().readValue(files.get(0), FeatureCollection.class);
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
                    tiles[y][x] = GameMap.ROAD_MEDIUM;
                }
            }
        }
        ImageIO.write(roadImage, "bmp", new File("roadImage.bmp"));
    }

    @Override
    public GeoJsonFileType getFileType() {
        return GeoJsonFileType.Roads;
    }
}
