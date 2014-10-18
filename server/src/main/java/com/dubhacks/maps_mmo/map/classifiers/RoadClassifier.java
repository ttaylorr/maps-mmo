package com.dubhacks.maps_mmo.map.classifiers;

import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import com.dubhacks.maps_mmo.map.MinMaxLngLat;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;

import java.util.List;

public class RoadClassifier extends Classifier {
    @Override
    public GeoJsonFileType getType() {
        return GeoJsonFileType.ROADS;
    }

    @Override
    public void classify(MinMaxLngLat mm, List<Feature> features) {
        for (Feature road : features) {
            GeoJsonObject geometry = road.getGeometry();
            if (geometry instanceof LineString) {
                mm.put((LineString) geometry);
            } else {
                throw new RuntimeException("road encountered with geometry: " + geometry.getClass().getCanonicalName());
            }
        }
    }
}
