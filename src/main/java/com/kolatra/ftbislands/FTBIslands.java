package com.kolatra.ftbislands;

import com.google.gson.GsonBuilder;
import com.kolatra.ftbislands.commands.CreateIslandsCommand;
import com.kolatra.ftbislands.commands.DeleteIslandCommand;
import com.kolatra.ftbislands.commands.ListIslandsCommand;
import com.kolatra.ftbislands.util.IslandCreator;
import com.kolatra.ftbislands.commands.CreateAllIslandsCommand;
import com.kolatra.ftbislands.commands.JoinIslandCommand;
import com.kolatra.ftbislands.commands.RenameIslandCommand;
import com.kolatra.ftbislands.commands.SaveIslandsCommand;
import com.kolatra.ftbislands.commands.SetIslandSpawnCommand;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = FTBIslands.MODID, name = FTBIslands.NAME, version = FTBIslands.VERSION, dependencies = "required-after:FTBU", acceptableRemoteVersions = "*")
public class FTBIslands {
    public static final String MODID = "FTBI";
    public static final String NAME = "FTB Islands";
    public static final String VERSION = "1.1.1";
    public static File islands;
    public static Logger logger;

    public static ArrayList<IslandCreator.IslandPos> islandLoc = new ArrayList<IslandCreator.IslandPos>();

    @Mod.EventHandler
    public void serverLoading(FMLServerStartingEvent event) {
        logger.info("Registering commands.");
        event.registerServerCommand(new CreateAllIslandsCommand());
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
        for(int c = 0; c < 100; c++) {
            addIslandToList(c);
        }
    }

    private void addIslandToList(int x) {
        if(x != 0) {
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
        logger = LogManager.getLogger("FTBI");
        File dir = event.getModConfigurationDirectory();
        File directory = new File(dir.getParentFile(), "local");
        islands = new File(directory, "islands.json");
        try {
            directory.mkdirs();
            islands.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveIslands(HashMap<String, IslandCreator.IslandPos> map) throws IOException {
        String s = new GsonBuilder().create().toJson(map);
        FileOutputStream outputStream = new FileOutputStream(islands);
//        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        FileUtils.writeStringToFile(islands, s); // possibly?
        DataOutputStream out = new DataOutputStream(outputStream);
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