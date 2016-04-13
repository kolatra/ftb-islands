package com.cricketcraft.ftbisland.util;

import com.cricketcraft.ftbisland.FTBIslands;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import static com.cricketcraft.ftbisland.util.IslandCreator.islandLocations;
import static com.cricketcraft.ftbisland.util.IslandCreator.reloadIslands;
import static com.cricketcraft.ftbisland.util.IslandCreator.save;

public class IslandUtils {

    public static void renameIsland(String oldName, String newName) {
        IslandCreator.IslandPos pos = islandLocations.get(oldName);
        islandLocations.remove(oldName);
        islandLocations.put(newName, pos);
        save();
    }

    public static void setSpawnForIsland(String s, int x, int y, int z) {
        IslandCreator.IslandPos pos = new IslandCreator.IslandPos(x, y, z);
        islandLocations.remove(s);
        islandLocations.put(s, pos);
        save();
    }

    public static void joinIsland(String islandName, EntityPlayer player) {
        if (player == null) {
            FTBIslands.logger.info("The join command must be run in game.");
        } else {
            reloadIslands();
            if (islandLocations.containsKey(islandName)) {
                IslandCreator.IslandPos pos = new IslandCreator.IslandPos(0, 60, 0);
                for(String key : islandLocations.keySet()) {
                    if (key.equalsIgnoreCase(islandName)) {
                        pos = islandLocations.get(key);
                    }
                }
                if (player.dimension != 0)
                    player.travelToDimension(0);
                player.setPositionAndUpdate(pos.getX() + 1.5, pos.getY() + 2, pos.getZ() + 1.5);
            } else {
                player.addChatComponentMessage(new ChatComponentText("Island does not exist!"));
            }
        }
    }

    public static void deleteIsland(String islandName, EntityPlayer player) {
        if (player != null)
            player.addChatMessage(new ChatComponentText(String.format("Deleted Island %s at %d", islandName, islandLocations.get(islandName))));
        islandLocations.remove(islandName);
        save();
    }
}
