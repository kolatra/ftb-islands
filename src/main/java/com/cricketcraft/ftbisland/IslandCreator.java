package com.cricketcraft.ftbisland;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class IslandCreator implements Serializable {
    public static HashMap<String, IslandPos> islandLocations = new HashMap<String, IslandPos>();

    public static boolean createIsland(World world, String playerName) {
        reloadIslands();
        IslandPos pos = FTBIslands.islandLoc.get(islandLocations.size() + 1);
        spawnIslandAt(world, pos.getX(), pos.getY(), pos.getZ(), playerName);
        return true;
    }

    public static boolean spawnIslandAt(World world, int x, int y, int z, String playerName) {
        reloadIslands();
        if(!islandLocations.containsKey(playerName)) {
            for (int c = 0; c < 3; c++) {
                for (int d = 0; d < 3; d++) {
                    world.setBlock(x + c, y, z + d, Blocks.dirt);
                }
            }

            world.setBlock(x + 2, y + 1, z + 1, Blocks.chest);
            world.getBlock(x + 2, y + 1, z + 1).rotateBlock(world, x + 2, y + 1, z + 1, ForgeDirection.WEST);
            TileEntityChest chest = (TileEntityChest) world.getTileEntity(x + 2, y + 1, z + 1);
            chest.setInventorySlotContents(0, new ItemStack(Blocks.water, 1));
            chest.setInventorySlotContents(1, new ItemStack(Blocks.lava, 1));
            chest.setInventorySlotContents(2, new ItemStack(Items.dye, 64, 15));
            chest.setInventorySlotContents(3, new ItemStack(Items.dye, 64, 15));
            chest.setInventorySlotContents(4, new ItemStack(Items.apple, 16));
            chest.setInventorySlotContents(5, new ItemStack(Blocks.sapling, 8, 0));
            chest.setInventorySlotContents(6, new ItemStack(Items.spawn_egg, 2, 90));
            chest.setInventorySlotContents(7, new ItemStack(Items.spawn_egg, 2, 91));
            chest.setInventorySlotContents(8, new ItemStack(Items.spawn_egg, 2, 92));
            chest.setInventorySlotContents(9, new ItemStack(Items.spawn_egg, 2, 93));
            chest.setInventorySlotContents(10, new ItemStack(ExCompressum.chickenStick, 1));

            if (islandLocations.size() != 0) {
                islandLocations.put(playerName, FTBIslands.islandLoc.get(islandLocations.size() + 1));
            } else {
                islandLocations.put(playerName, FTBIslands.islandLoc.get(1));
            }

            islandLocations.put(playerName, new IslandPos(x, y, z));
            try {
                FTBIslands.saveIslands(islandLocations);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    public static void joinIsland(String islandName, EntityPlayer player) {
        reloadIslands();

        IslandPos pos = islandLocations.get(islandName);
        player.setPositionAndUpdate(pos.getX(), pos.getY() + 1, pos.getZ());
    }

    private static void reloadIslands() {
        try {
            islandLocations = FTBIslands.getIslands();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class IslandPos implements Serializable {
        private int x;
        private int y;
        private int z;

        public IslandPos(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }
    }
}
