package com.cricketcraft.ftbisland;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandCreator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class IslandUtils {

    public static void renameIsland(String oldName, String newName) {
        IslandCreator.IslandPos pos = IslandCreator.islandLocations.get(oldName);
        IslandCreator.islandLocations.remove(oldName);
        IslandCreator.islandLocations.put(newName, pos);
        IslandCreator.save();
    }

    public static void setSpawnForIsland(String s, int x, int y, int z) {
        IslandCreator.IslandPos pos = new IslandCreator.IslandPos(x, y, z);
        IslandCreator.islandLocations.remove(s);
        IslandCreator.islandLocations.put(s, pos);
        IslandCreator.save();
    }

    public static void joinIsland(String islandName, EntityPlayer player) {
        if (player == null) {
            FTBIslands.logger.info("The join command must be run in game.");
        } else {
            IslandCreator.reloadIslands();
            if (IslandCreator.islandLocations.containsKey(islandName)) {
                IslandCreator.IslandPos pos = new IslandCreator.IslandPos(0, 60, 0);
                for (String key : IslandCreator.islandLocations.keySet()) {
                    if (key.equalsIgnoreCase(islandName)) {
                        pos = IslandCreator.islandLocations.get(key);
                    }
                }
                if (player.dimension != 0) {
                    player.travelToDimension(0);
                }
                player.setPositionAndUpdate(pos.getX() + 1.5, pos.getY() + 2, pos.getZ() + 1.5);
            } else {
                player.addChatComponentMessage(new ChatComponentText("Island does not exist!"));
            }
        }
    }

    public static void deleteIsland(String islandName, EntityPlayer player) {
        if (player != null) {
            player.addChatMessage(new ChatComponentText(String.format("Deleted Island %s at %d", islandName, IslandCreator.islandLocations.get(islandName))));
        }
        IslandCreator.islandLocations.remove(islandName);
        IslandCreator.save();
    }
}
