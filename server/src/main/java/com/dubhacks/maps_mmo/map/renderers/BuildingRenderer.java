package com.dubhacks.maps_mmo.map.renderers;

import com.dubhacks.maps_mmo.core.map.GameMap;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import org.geojson.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class BuildingRenderer extends Renderer {
    public BuildingRenderer(GameMap map) {
        super(map);
    }

    @Override
    public void render(List<Feature> features) throws IOException {
        BufferedImage image = this.allocateBinaryImage();

        for (Feature building : features) {
            if ("yes".equals(building.getProperty("building"))) {
                GeoJsonObject geometry = building.getGeometry();
                if (geometry instanceof MultiPolygon) {
                    this.draw((MultiPolygon) geometry, image);
                } else if (geometry instanceof Polygon) {
                    this.draw((Polygon) geometry, image);
                } else if (geometry instanceof LineString) {
                    this.draw((LineString) geometry, image);
                }
            }
        }

        this.write(image, GameMap.BUILDING_PLACEHOLDER);
    }

    @Override
    public GeoJsonFileType getFileType() {
        return GeoJsonFileType.BUILDINGS;
    }
}
