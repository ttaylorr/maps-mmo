package com.dubhacks.maps_mmo.map;

import com.dubhacks.maps_mmo.core.IGameMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameMap implements IGameMap {
    
    public static final byte TERRAIN_WATER = 10;
    public static final byte TERRAIN_FOREST = 11;
    
    public static final byte ROAD_SMALL  = 15;
    public static final byte ROAD_MEDIUM = 16;
    public static final byte ROAD_LARGE  = 17;
    
    public static final byte BUILDING_PLACEHOLDER = 50;
    
    private final int width;
    private final int height;
    
    public final byte[][] tiles;
    public final MapInfo info;

    public GameMap(byte[][] tiles, MapInfo info) {
        this.tiles = tiles;
        this.info = info;
        this.height = tiles.length;
        this.width = tiles[0].length;
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public byte get(int x, int y) {
        return this.tiles[y][x];
    }

    public void set(int x, int y, GeoJsonFileType type) {
        this.tiles[y][x] = type.asByte();
    }

    @Override
    public boolean isTraversable(byte tile) {
        return tile == ROAD_SMALL || tile == ROAD_MEDIUM || tile == ROAD_LARGE;
    }

    public void saveAsImage(File out) throws IOException {
        BufferedImage image = new BufferedImage(this.tiles.length, this.tiles[0].length, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = image.createGraphics();

        for (int x = 0; x < this.tiles.length; x++) {
            for (int y = 0; y < this.tiles[x].length; y++) {
                byte b = this.tiles[x][y];
                if (b != 0) {
                    GeoJsonFileType type = GeoJsonFileType.fromByte(b);

                    g.setColor(type.getColor());
                    g.drawLine(y, x, y, x);
                }
            }
        }

        g.dispose();
        ImageIO.write(image, "png", out);
    }
}
