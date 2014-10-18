package com.dubhacks.maps_mmo.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import com.dubhacks.maps_mmo.core.map.GameMap;

public class GameAssets {

    private static BufferedImage terrain;
    private static BufferedImage blank;

    private static Map<Byte, BufferedImage> tiles;

    /**
     * Loads game assets into memory.
     *
     * @return <code>true</code> if assets properly loaded, otherwise
     *         <code>false</code>
     * @throws IOException
     */
    public static boolean load() throws IOException {
        terrain = ImageIO.read(
                /*ClassLoader.getSystemResourceAsStream*/new File("src/main/resources/LPC_Terrain_0/terrain.png"));

        blank = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

        tiles = new TreeMap<>();
        tiles.put(GameMap.BLANK_TILE, blank);
        tiles.put(GameMap.ROAD_MEDIUM, terrain.getSubimage(928, 672, 32, 32));

        return true;
    }

    public static BufferedImage getMapTile(byte b) {
        BufferedImage image = tiles.get(b);
        return image == null ? blank : image;
    }

}
