package com.cricketcraft.ftbisland;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

@Mod(modid = FTBIslands.MODID, name = FTBIslands.NAME, version = FTBIslands.VERSION, dependencies = "required-after:excompressum;required-after:FTBL")
public class FTBIslands {
    public static final String MODID = "ftbisland";
    public static final String NAME = "FTB Islands";
    public static final String VERSION = "1.0";
    public static File islands;

    public static ArrayList<IslandCreator.IslandPos> islandLocations = new ArrayList<IslandCreator.IslandPos>();

    @Mod.EventHandler
    public void serverLoading(FMLServerStartingEvent event) {
        event.registerServerCommand(new SpawnIslandCommand());
        loadIslands();
        loadChestLoot();
    }

    private void loadIslands() {
        for(int c = 0; c < 100; c++) {
            addIslandToList(c);
        }
    }

    private void addIslandToList(int x) {
        islandLocations.add(new IslandCreator.IslandPos(x * 1000, 60, x * 1000));
        islandLocations.add(new IslandCreator.IslandPos(-x * 1000, 60, x * 1000));
        islandLocations.add(new IslandCreator.IslandPos(-x * 1000, 60, -x * 1000));
        islandLocations.add(new IslandCreator.IslandPos(x * 1000, 60, -x * 1000));
    }

    private void loadChestLoot() {

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File dir = event.getModConfigurationDirectory();
        File directory = new File(dir.getParentFile(), "local");
        islands = new File(directory.getParentFile(), "islands.ser");
        try {
            directory.mkdirs();
            islands.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveIslands(HashMap<String, IslandCreator.IslandPos> map) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(islands);
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(map);
        out.close();
        outputStream.close();
    }

    public static HashMap<String, IslandCreator.IslandPos> getIslands() throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(islands);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        HashMap<String, IslandCreator.IslandPos> retVal = (HashMap<String, IslandCreator.IslandPos>) in.readObject();
        in.close();
        fileIn.close();
        return retVal;
    }
}
