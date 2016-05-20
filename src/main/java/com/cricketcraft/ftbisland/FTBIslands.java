package com.cricketcraft.ftbisland;

import com.cricketcraft.ftbisland.commands.CreateIslandsCommand;
import com.cricketcraft.ftbisland.commands.DeleteIslandCommand;
import com.cricketcraft.ftbisland.commands.JoinIslandCommand;
import com.cricketcraft.ftbisland.commands.ListIslandsCommand;
import com.cricketcraft.ftbisland.commands.RenameIslandCommand;
import com.cricketcraft.ftbisland.commands.SaveIslandsCommand;
import com.cricketcraft.ftbisland.commands.SetIslandSpawnCommand;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.common.config.Configuration;

@Mod(modid = FTBIslands.MODID, name = FTBIslands.NAME, version = FTBIslands.VERSION, dependencies = "required-after:FTBU", acceptableRemoteVersions = "*")
public class FTBIslands {
    public static final String MODID = "FTBI";
    public static final String NAME = "FTB Islands";
    public static final String VERSION = "1.3.1";
    public static int maxIslands;
    public static File islands;
    public static Logger logger;
    public static boolean skyFactory;
    public static boolean platform;
    private static File oldIslands;
    private static File directory;

    public static ArrayList<IslandCreator.IslandPos> islandLoc = new ArrayList<IslandCreator.IslandPos>();

    @Mod.EventHandler
    public void serverLoading(FMLServerStartingEvent event) {
        logger.info("Registering commands.");
        event.registerServerCommand(new CreateIslandsCommand());
        event.registerServerCommand(new DeleteIslandCommand());
        event.registerServerCommand(new JoinIslandCommand());
        event.registerServerCommand(new ListIslandsCommand());
        event.registerServerCommand(new RenameIslandCommand());
        event.registerServerCommand(new SaveIslandsCommand());
        event.registerServerCommand(new SetIslandSpawnCommand());
        logger.info("Finished registering commands.");
        loadIslands();
        loadChestLoot();
    }

    private void loadIslands() {
        for (int c = 0; c < maxIslands; c++) {
            addIslandToList(c);
        }
    }

    private void addIslandToList(int x) {
        if (x != 0) {
            islandLoc.add(new IslandCreator.IslandPos(x * 1000, 60, x * 1000));
            islandLoc.add(new IslandCreator.IslandPos(-x * 1000, 60, x * 1000));
            islandLoc.add(new IslandCreator.IslandPos(-x * 1000, 60, -x * 1000));
            islandLoc.add(new IslandCreator.IslandPos(x * 1000, 60, -x * 1000));
        } else {
            islandLoc.add(new IslandCreator.IslandPos(x * 1000, 60, x * 1000));
        }
    }

    private void loadChestLoot() {

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.init(new File(event.getModConfigurationDirectory(), "ftbi/FTB_Islands.cfg"));
        logger = LogManager.getLogger("FTBI");
        File dir = event.getModConfigurationDirectory();
        directory = new File(dir.getParentFile(), "local");
        oldIslands = new File(directory, "islands.ser");
        islands = new File(directory, "islands.json");
        if (oldIslands.exists()) {
            logger.info("Islands.ser found, attempting conversion.");
            try {
                convert();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            directory.mkdirs();
            islands.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(islands.getPath()));
        if (br.readLine() == null) {
            logger.info("Islands file empty, placing a default value.");
            IslandCreator.islandLocations.put("default", new IslandCreator.IslandPos(0, 60, 0));
            try {
                saveIslands(IslandCreator.islandLocations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveIslands(HashMap<String, IslandCreator.IslandPos> map) throws IOException {
        String s = new GsonBuilder().create().toJson(map);
        FileUtils.writeStringToFile(islands, s);
    }

    public static HashMap<String, IslandCreator.IslandPos> getIslands() throws IOException {
        FileInputStream stream = new FileInputStream(islands);
        HashMap<String, IslandCreator.IslandPos> map = new Gson().fromJson(FileUtils.readFileToString(islands), new TypeToken<HashMap<String, IslandCreator.IslandPos>>(){}.getType());
        stream.close();
        return map;
    }

    private static class Config {
        private static Configuration config;

        public static void init(File file) {
            if (config == null) {
                config = new Configuration(file);
                loadConfig();
            }
        }

        private static void loadConfig() {
            FTBIslands.maxIslands = config.getInt("Max Islands", "misc", 100, 1, 1000, "The maximum amount of islands that can be created. This number will be multiplied by four." +
                    " Be careful with high numbers.");
            FTBIslands.skyFactory = config.getBoolean("Sky Factory", "misc", false, "Set this to true if you are playing on Sky Factory.");
            FTBIslands.platform = config.getBoolean("Platform", "misc", false, "Set to true if you want to start on a 3x3 platform, or false for a tree.");

            if (config.hasChanged())
                config.save();
        }

        @SubscribeEvent
        public void onChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.modID.equalsIgnoreCase(FTBIslands.MODID))
                loadConfig();
        }
    }

    private static void convert() throws IOException, ClassNotFoundException {
        if (!oldIslands.exists())
            return;
        logger.info("Old islands file found! Trying to convert to new format!");

        FileInputStream fileIn = new FileInputStream(oldIslands);
        ObjectInputStream in = new ObjectInputStream(fileIn);

        HashMap<String, IslandCreator.IslandPos> map = (HashMap<String, IslandCreator.IslandPos>) in.readObject();
        in.close();
        fileIn.close();
        String s = new GsonBuilder().create().toJson(map);

        File newFile = new File(directory, "islands.json");
        FileOutputStream outputStream = new FileOutputStream(newFile);
        FileUtils.writeStringToFile(newFile, s);
        outputStream.close();
        oldIslands.delete();
        logger.info("Conversion completed.");
    }
}
