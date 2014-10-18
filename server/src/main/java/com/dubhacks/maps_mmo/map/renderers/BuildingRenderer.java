package com.dubhacks.maps_mmo.map.renderers;

import com.dubhacks.maps_mmo.map.GameMap;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import org.geojson.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class BuildingRenderer extends Renderer {
    public BuildingRenderer(GameMap map) {
        super(map);
    }

    @Override
    public void render(List<Feature> features) throws IOException {
        BufferedImage buildingImage = this.allocateImage();
        for (Feature building : features) {
            GeoJsonObject geometry = building.getGeometry();
            if (geometry instanceof MultiPolygon) {
                draw((MultiPolygon)geometry, buildingImage);
            } else if (geometry instanceof Polygon) {
                draw((Polygon)geometry, buildingImage);
            } else if (geometry instanceof LineString) {
                draw((LineString)geometry, buildingImage);
            }
        }

        for (int x = 0; x < this.map.info.width; x++) {
            for (int y = 0; y < this.map.info.height; y++) {
                if (buildingImage.getRGB(x, y) == BINARY_IMAGE_SET) {
                    this.map.tiles[y][x] = GameMap.BUILDING_PLACEHOLDER;
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
