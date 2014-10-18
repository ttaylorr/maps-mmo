package com.dubhacks.maps_mmo.map.renderers;

import com.dubhacks.maps_mmo.core.map.GameMap;
import com.dubhacks.maps_mmo.map.GeoJsonFileType;
import org.geojson.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BuildingRenderer extends Renderer {

    public BuildingRenderer(GameMap map) {
        super(map);
    }

    @Override
    public void render(List<Feature> features) throws IOException {
        Map<Type, BufferedImage> imageMap = new TreeMap<>();
        
        Type[] types = Type.values();
        for (Type type : types) {
            imageMap.put(type, this.allocateBinaryImage());
        }

        for (Feature building : features) {
            if ("yes".equals(building.getProperty("building"))) {
                String name = (String) building.getProperty("name");
                BufferedImage image = imageMap.get(getType(name));
                GeoJsonObject geometry = building.getGeometry();
                if (geometry instanceof MultiPolygon) {
                    this.draw((MultiPolygon) geometry, image);
                } else if (geometry instanceof Polygon) {
                    this.draw((Polygon) geometry, image);
                } else if (geometry instanceof LineString) {
                    this.draw((LineString) geometry, image);
                }
            }
        }

        for (Map.Entry<Type, BufferedImage> entry : imageMap.entrySet()) {
            this.write(entry.getValue(), entry.getKey().mapByte);
        }
    }

    @Override
    public GeoJsonFileType getFileType() {
        return GeoJsonFileType.BUILDINGS;
    }
    
    private static Map<String, Type> typeMap = initializeTypeMap();

    private static Map<String, Type> initializeTypeMap() {
        typeMap = new TreeMap<>();
        
        typeMap.put("Safeway [0-9]+", Type.GROCERY);
        typeMap.put("Albertsons", Type.GROCERY);
        typeMap.put("QFC", Type.GROCERY);
        typeMap.put("Safeway [0-9]+ Fuel Station", Type.GAS_STATION);
        typeMap.put("Jack in the Box", Type.FAST_FOOD);
        typeMap.put(".*Arco", Type.GAS_STATION);
        typeMap.put(".* Library", Type.LIBRARY);
        typeMap.put(".* Church", Type.CHURCH);
        typeMap.put(".* School", Type.SCHOOL);
        typeMap.put(".* Storage", Type.PUBLIC_STORAGE);
        typeMap.put(".* Inn", Type.HOTEL);
        typeMap.put(".* Hotel", Type.HOTEL);
        typeMap.put("Shell", Type.GAS_STATION);
        typeMap.put(".*Sushi.*", Type.RESTAURANT);
        typeMap.put(".*Teriyaki.*", Type.RESTAURANT);
        typeMap.put(".*Chevron.*", Type.GAS_STATION);
        typeMap.put(".* Post Office", Type.POST_OFFICE);
        typeMap.put("Red Robin", Type.RESTAURANT);
        typeMap.put(".* Bank", Type.BANK);
        typeMap.put("Am Pm", Type.GAS_STATION);
        typeMap.put("ampm", Type.GAS_STATION);
        typeMap.put("[^A-Za-z]+", Type.UNKNOWN);
        typeMap.put(". Block", Type.UNKNOWN);
        typeMap.put("Building .", Type.UNKNOWN);
        typeMap.put("Wells Fargo", Type.BANK);
        typeMap.put(".", Type.UNKNOWN);
        typeMap.put("Walgreens", Type.DRUGSTORE);
        typeMap.put(".*Pizza.*", Type.PIZZA);
        typeMap.put("McDonald's", Type.FAST_FOOD);
        typeMap.put("Rite Aid", Type.DRUGSTORE);
        typeMap.put("KFC", Type.FAST_FOOD);
        typeMap.put("Burger King", Type.FAST_FOOD);
        typeMap.put("Taco Time", Type.FAST_FOOD);
        typeMap.put("Taco Bell", Type.FAST_FOOD);
        typeMap.put(".*Avenue.*", Type.UNKNOWN);
        
        return typeMap;
    }
    
    public static Type getType(String name) {
        if (name != null) {
            for (Map.Entry<String, Type> entry : typeMap.entrySet()) {
                if (name.matches(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return Type.UNKNOWN;
    }
    
    public enum Type {
        UNKNOWN(GameMap.BUILDING_PLACEHOLDER),
        OTHER(GameMap.BUILDING_OTHER),
        GAS_STATION(GameMap.BUILDING_GAS_STATION),
        GROCERY(GameMap.BUILDING_GROCERY),
        LIBRARY(GameMap.BUILDING_LIBRARY),
        PUBLIC_RESTOOM(GameMap.BUILDING_PUBLIC_RESTOOM),
        FAST_FOOD(GameMap.BUILDING_FAST_FOOD),
        CHURCH(GameMap.BUILDING_CHURCH),
        POST_OFFICE(GameMap.BUILDING_POST_OFFICE),
        DRUGSTORE(GameMap.BUILDING_DRUGSTORE),
        PUBLIC_STORAGE(GameMap.BUILDING_PUBLIC_STORAGE),
        HOTEL(GameMap.BUILDING_HOTEL),
        SCHOOL(GameMap.BUILDING_SCHOOL),
        RESTAURANT(GameMap.BUILDING_RESTAURANT),
        GYM(GameMap.BUILDING_GYM),
        BANK(GameMap.BUILDING_BANK),
        COFFEE_SHOP(GameMap.BUILDING_COFFEE_SHOP),
        PIZZA(GameMap.BUILDING_PIZZA);
        
        public final byte mapByte;
        private Type(byte b) {
            mapByte = b;
        }
    }
    
}
