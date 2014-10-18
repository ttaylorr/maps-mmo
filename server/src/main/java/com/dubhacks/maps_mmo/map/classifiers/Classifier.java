package com.dubhacks.maps_mmo.map.classifiers;

import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import com.dubhacks.maps_mmo.map.MinMaxLngLat;
import org.geojson.*;

import java.util.List;

public abstract class Classifier {
    public abstract GeoJsonFileType getType();

    public abstract void classify(MinMaxLngLat mm, List<Feature> features);
}
