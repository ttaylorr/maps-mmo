package com.dubhacks.maps_mmo.map.renderers;

import com.dubhacks.maps_mmo.core.map.GameMap;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class RoadRenderer extends Renderer {

    public RoadRenderer(GameMap map) {
        super(map);
    }

    @Override
    public void render(List<Feature> features) throws IOException {
        BufferedImage image = this.allocateImage();

        for (Feature road : features) {
            String type = road.getProperty("type");
            GeoJsonObject geometry = road.getGeometry();
            if (geometry instanceof LineString) {
                this.draw((LineString) geometry, widthOf(type), image);
            }
        }

        this.write(image);
    }

    private static float widthOf(String type) {
        switch (type) {
            case "residential":
            case "tertiary": return 2;
            case "motorway":
            case "secondary": return (float) 2.5;
            case "primary": return 3;
            default: return 2;
        }
    }

    @Override
    public GeoJsonFileType getFileType() {
        return GeoJsonFileType.ROADS;
    }

}
