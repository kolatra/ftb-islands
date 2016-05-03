package com.cricketcraft.ftbisland;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.cricketcraft.ftbisland.commands.CreateIslandsCommand;
import com.cricketcraft.ftbisland.commands.DeleteIslandCommand;
import com.cricketcraft.ftbisland.commands.JoinIslandCommand;
import com.cricketcraft.ftbisland.commands.ListIslandsCommand;
import com.cricketcraft.ftbisland.commands.RenameIslandCommand;
import com.cricketcraft.ftbisland.commands.SaveIslandsCommand;
import com.cricketcraft.ftbisland.commands.SetIslandSpawnCommand;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    public static final String VERSION = "1.2.1";
    public static int maxIslands;
    public static File islands;
    public static Logger logger;
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
