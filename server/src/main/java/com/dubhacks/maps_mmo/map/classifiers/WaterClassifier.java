package com.dubhacks.maps_mmo.map.classifiers;

import com.dubhacks.maps_mmo.map.GameMapBuilder;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import org.geojson.*;

import java.util.List;

public class WaterClassifier extends Classifier {
    @Override
    public GeoJsonFileType getType() {
        return GeoJsonFileType.Water;
    }

    @Override
    public void classify(GameMapBuilder.MinMaxLngLat mm, List<Feature> features) {
        for (Feature waterArea : features) {
            GeoJsonObject geometry = waterArea.getGeometry();
            if (geometry instanceof Polygon) {
                mm.put((Polygon)geometry);
            } else if (geometry instanceof LineString) {
                mm.put((LineString)geometry);
            } else if (geometry instanceof MultiPolygon) {
                mm.put((MultiPolygon)geometry);
            } else {
                throw new RuntimeException("water area encountered with geometry: " + geometry.getClass().getCanonicalName() + " (type:" + waterArea.getProperty("type") + ")");
            }
        }
    }
}
