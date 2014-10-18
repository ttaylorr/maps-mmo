package com.dubhacks.maps_mmo.client;

import java.io.File;
import java.io.IOException;

import org.geojson.Feature;
import org.geojson.FeatureCollection;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
    public static void main(String[] args) throws JsonParseException, IOException {
        FeatureCollection featureCollection = new ObjectMapper().readValue(new File("seattle_washington-roads.geojson"), FeatureCollection.class);
        int i = 0;
        for (Feature feature : featureCollection.getFeatures()) {
            System.out.println(feature.getProperties().get("name"));
            if ((i++) >= 100) break;
        }
    }
}
