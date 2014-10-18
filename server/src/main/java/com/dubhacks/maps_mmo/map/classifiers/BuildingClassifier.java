package com.dubhacks.maps_mmo.map.classifiers;

import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import com.dubhacks.maps_mmo.core.map.MinMaxLngLat;
import org.geojson.*;

import java.util.List;

public class BuildingClassifier extends Classifier {
    @Override
    public GeoJsonFileType getType() {
        return GeoJsonFileType.BUILDINGS;
    }

    @Override
    public void classify(MinMaxLngLat mm, List<Feature> features) {
        for (Feature building : features) {
            GeoJsonObject geometry = building.getGeometry();
            if (geometry instanceof MultiPolygon) {
                put(mm, (MultiPolygon)geometry);
            } else if (geometry instanceof Polygon) {
                put(mm, (Polygon)geometry);
            } else if (geometry instanceof LineString) {
                put(mm, (LineString)geometry);
            } else {
                throw new RuntimeException("building encountered with geometry: " + geometry.getClass().getCanonicalName() + " (type:" + building.getProperty("type") + ")");
            }
        }
    }
}
