package com.dubhacks.maps_mmo.map.renderers;

import com.dubhacks.maps_mmo.core.map.GameMap;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;
import org.geojson.MultiPolygon;
import org.geojson.Polygon;

public class LandUsageRenderer extends Renderer {

    public LandUsageRenderer(GameMap map) {
        super(map);
    }

    @Override
    public void render(List<Feature> features) throws IOException {
        BufferedImage image = this.allocateBinaryImage();

        for (Feature feature : features) {
            switch ((String)feature.getProperty("type")) {
                case "forest":
                case "wood":
                    GeoJsonObject geometry = feature.getGeometry();
                    if (geometry instanceof Polygon) {
                        this.draw((Polygon) geometry, image);
                    } else if (geometry instanceof LineString) {
                        this.draw((LineString) geometry, image);
                    } else if (geometry instanceof MultiPolygon) {
                        this.draw((MultiPolygon)geometry, image);
                    }
            }
        }

        this.write(image, GameMap.TERRAIN_FOREST);
    }

    @Override
    public GeoJsonFileType getFileType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
