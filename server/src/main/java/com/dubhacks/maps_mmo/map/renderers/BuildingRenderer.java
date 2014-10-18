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

public class BuildingRenderer extends Renderer {
    public BuildingRenderer(GameMapBuilder.MapParameters mapParameters) {
        super(mapParameters);
    }

    @Override
    public void render(byte[][] tiles, List<File> files) throws IOException {
        BufferedImage buildingImage = new BufferedImage(mapParameters.width, mapParameters.height, BufferedImage.TYPE_BYTE_BINARY);
        FeatureCollection buildings = new ObjectMapper().readValue(files.get(0), FeatureCollection.class);
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
                    tiles[y][x] = GameMap.BUILDING_PLACEHOLDER;
                }
            }
        }
        ImageIO.write(buildingImage, "bmp", new File("buildingImage.bmp"));
    }

    @Override
    public GeoJsonFileType getFileType() {
        return GeoJsonFileType.Buildings;
    }
}
