package com.dubhacks.maps_mmo.util;

import com.damnhandy.uri.template.UriTemplate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GoogleMaps {
    public static LatLng fromString(String location) throws IOException {
        String path = UriTemplate.fromTemplate("http://maps.googleapis.com/maps/api/geocode/json?address={address}")
                .set("address", location).expand();

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        GoogleMapsResult r = new ObjectMapper().readValue(conn.getInputStream(), GoogleMapsResult.class);
        if (r != null && r.results.size() > 0) {
            return r.results.get(0).geometry.location;
        } else {
            return null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoogleMapsResult {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public List<Response> results;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Response {
            public Geometry geometry;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Geometry {
                public LatLng location;
            }
        }
    }

    public static class LatLng {
        public double lat;
        public double lng;
    }
}
