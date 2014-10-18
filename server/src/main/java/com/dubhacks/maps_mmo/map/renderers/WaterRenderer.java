package com.dubhacks.maps_mmo.map.renderers;

import com.dubhacks.maps_mmo.map.GameMap;
import com.dubhacks.maps_mmo.map.GameMapBuilder;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class WaterRenderer extends Renderer {
    public WaterRenderer(GameMapBuilder.MapParameters mapParameters) {
        super(mapParameters);
    }

    @Override
    public void render(byte[][] tiles, List<File> files) throws IOException {
        BufferedImage waterImage = new BufferedImage(mapParameters.width, mapParameters.height, BufferedImage.TYPE_BYTE_BINARY);
        for (File file : files) {
            FeatureCollection waterAreas = new ObjectMapper().readValue(file, FeatureCollection.class);
            List<Feature> waterAreasList = waterAreas.getFeatures();
            for (Feature waterArea : waterAreasList) {
                GeoJsonObject geometry = waterArea.getGeometry();
                if (geometry instanceof Polygon) {
                    this.draw((Polygon) geometry, waterImage, this.mapParameters);
                } else if (geometry instanceof LineString) {
                    this.draw((LineString) geometry, waterImage, this.mapParameters);
                }
            }
        }
        for (int x = 0; x < mapParameters.width; x++) {
            for (int y = 0; y < mapParameters.height; y++) {
                if (waterImage.getRGB(x, y) == BINARY_IMAGE_SET) {
                    tiles[y][x] = GameMap.TERRAIN_WATER;
                }
            }
        }
        ImageIO.write(waterImage, "bmp", new File("waterImage.bmp"));
    }

    @Override
    public GeoJsonFileType getFileType() {
        return GeoJsonFileType.Water;
    }
}
