package com.dubhacks.maps_mmo.map.classifiers;

import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import com.dubhacks.maps_mmo.core.map.MinMaxLngLat;
import org.geojson.*;

import java.util.List;

public class LandUsageClassifier extends Classifier {
    @Override
    public GeoJsonFileType getType() {
        return GeoJsonFileType.LAND_USAGES;
    }

    @Override
    public void classify(MinMaxLngLat mm, List<Feature> features) {
        for (Feature landUsage : features) {
            GeoJsonObject geometry = landUsage.getGeometry();
            if (geometry instanceof MultiPolygon) {
                put(mm, (MultiPolygon)geometry);
            } else if (geometry instanceof LineString) {
                put(mm, (LineString)geometry);
            } else if (geometry instanceof Polygon) {
                put(mm, (Polygon)geometry);
            } else {
                throw new RuntimeException("land usage encountered with geometry: " + geometry.getClass().getCanonicalName() + " (type:" + landUsage.getProperty("type") + ")");
            }
        }
    }
}
