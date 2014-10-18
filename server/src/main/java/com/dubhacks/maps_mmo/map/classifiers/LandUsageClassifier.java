package com.dubhacks.maps_mmo.map.classifiers;

import com.dubhacks.maps_mmo.map.GameMapBuilder;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import org.geojson.*;

import java.util.List;

public class LandUsageClassifier extends Classifier {
    @Override
    public GeoJsonFileType getType() {
        return GeoJsonFileType.LandUsages;
    }

    @Override
    public void classify(GameMapBuilder.MinMaxLngLat mm, List<Feature> features) {
        for (Feature landUsage : features) {
            GeoJsonObject geometry = landUsage.getGeometry();
            if (geometry instanceof MultiPolygon) {
                mm.put((MultiPolygon)geometry);
            } else if (geometry instanceof LineString) {
                mm.put((LineString)geometry);
            } else if (geometry instanceof Polygon) {
                mm.put((Polygon)geometry);
            } else {
                throw new RuntimeException("land usage encountered with geometry: " + geometry.getClass().getCanonicalName() + " (type:" + landUsage.getProperty("type") + ")");
            }
        }
    }
}
