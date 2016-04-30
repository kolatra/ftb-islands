package com.kolatra.ftbislands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kolatra.ftbislands.commands.CreateIslandsCommand;
import com.kolatra.ftbislands.commands.DeleteIslandCommand;
import com.kolatra.ftbislands.commands.JoinIslandCommand;
import com.kolatra.ftbislands.commands.ListIslandsCommand;
import com.kolatra.ftbislands.commands.RenameIslandCommand;
import com.kolatra.ftbislands.commands.SaveIslandsCommand;
import com.kolatra.ftbislands.commands.SetIslandSpawnCommand;
import com.kolatra.ftbislands.util.IslandCreator;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.common.config.Configuration;

@Mod(modid = FTBIslands.MODID, name = FTBIslands.NAME, version = FTBIslands.VERSION, dependencies = "required-after:FTBU", acceptableRemoteVersions = "*")
public class FTBIslands {
    public static final String MODID = "FTBI";
    public static final String NAME = "FTB Islands";
    public static final String VERSION = "1.2.0";
    public static int maxIslands;
    public static File islands;
    public static Logger logger;

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
        File directory = new File(dir.getParentFile(), "local");
        islands = new File(directory, "islands.json");
        try {
            directory.mkdirs();
            islands.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveIslands(HashMap<String, IslandCreator.IslandPos> map) throws IOException {
        String s = new GsonBuilder().create().toJson(map);
        FileOutputStream outputStream = new FileOutputStream(islands);
        FileUtils.writeStringToFile(islands, s);
        outputStream.close();
    }

    public static HashMap<String, IslandCreator.IslandPos> getIslands() throws IOException {
        FileInputStream fileIn = new FileInputStream(islands);
        HashMap<String, IslandCreator.IslandPos> map = new Gson().fromJson(FileUtils.readFileToString(islands), new TypeToken<HashMap<String, IslandCreator.IslandPos>>(){}.getType());
        fileIn.close();
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

            if (config.hasChanged())
                config.save();
        }

        @SubscribeEvent
        public void onChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.modID.equalsIgnoreCase(FTBIslands.MODID))
                loadConfig();
        }
    }
}
