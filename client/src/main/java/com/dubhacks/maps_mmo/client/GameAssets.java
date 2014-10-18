package com.dubhacks.maps_mmo.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.dubhacks.maps_mmo.core.map.GameMap;

public class GameAssets {

    private static BufferedImage terrain;
    private static BufferedImage tree_single;
    private static BufferedImage character_single;
    private static BufferedImage character_brunette_single;

    private static BufferedImage blank;

    private static BufferedImage[] tiles;

    /**
     * Loads game assets into memory.
     * <p>
     * @return <code>true</code> if assets properly loaded, otherwise
     *         <code>false</code>
     * <p>
     * @throws IOException
     */
    public static boolean load() throws IOException {
        terrain = ImageIO.read(new File("src/main/resources/LPC_Terrain_0/terrain.png"));
        tree_single = ImageIO.read(new File("src/main/resources/tree_single.png"));
        character_single = ImageIO.read(new File("src/main/resources/character_single.png"));
        character_brunette_single = ImageIO.read(new File("src/main/resources/character_brunette_single.png"));

        blank = terrain.getSubimage(384, 352, 32, 32);

        tiles = new BufferedImage[256];
        tiles[GameMap.BLANK_TILE] = blank;
        tiles[GameMap.ROAD_MEDIUM] = terrain.getSubimage(928, 672, 32, 32);
        tiles[GameMap.TERRAIN_WATER] = terrain.getSubimage(896, 96, 32, 32);
        tiles[GameMap.TERRAIN_FOREST] = tree_single;
        tiles[GameMap.BUILDING_PLACEHOLDER] = terrain.getSubimage(128, 448, 32, 32);

        return true;
    }

    public static BufferedImage getMapTile(byte b) {
        BufferedImage image = tiles[b];
        return image == null ? blank : image;
    }

    public static BufferedImage getPlayerSprite() {
        return character_single;
    }

    public static BufferedImage getOtherPlayerSprite() {
        return character_brunette_single;
    }

}
