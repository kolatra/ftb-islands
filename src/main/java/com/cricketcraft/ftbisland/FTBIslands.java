package com.cricketcraft.ftbisland;

import com.cricketcraft.ftbisland.commands.*;
import com.cricketcraft.ftbisland.util.IslandCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

import net.minecraft.nbt.*;

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
        islands = new File(directory, "islands.nbt");
        try {
            directory.mkdirs();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveIslands(HashMap<String, IslandCreator.IslandPos> map) throws IOException {
        NBTTagCompound islandNbt = new NBTTagCompound();
        NBTTagList isleData = new NBTTagList();
        for(java.util.Map.Entry<String, IslandCreator.IslandPos> entry : map.values())
        {
            NBTTagCompound single = new NBTTagCompound();
            single.setString("p", entry.getKey());
            single.setInteger("x",entry.getValue().getX());
            single.setByte("y",(byte)(entry.getValue().getY()-128));
            single.setInteger("z",entry.getValue().getZ());            
            isleData.appendTag(single);
        }        
        islandNbt.setTag("data", isleData);
        FileOutputStream outputStream = new FileOutputStream(islands);
        CompressedStreamTools.writeCompressed(islandNbt, outputStream);
        outputStream.close();
    }

    public static HashMap<String, IslandCreator.IslandPos> getIslands() throws IOException, ClassNotFoundException {
        HashMap<String, IslandCreator.IslandPos> islands = null;
        if(!islands.exists())
        {
            islands = new HashMap<String, IslandCreator.IslandPos>();
            return islands;
        }
        
        FileInputStream fileIn = new FileInputStream(islands);
        NBTTagCompound islandNbt = CompressedStreamTools.readCompressed(new FileInputStream(file));
        fileIn.close();
        NBTTagList isleData = islandNbt.getTagList("data");
        islands = new HashMap<String, IslandCreator.IslandPos>(isleData.tagCount()+ (int)(isleData.tagCount()*0.5));
        for (int index = 0; index < isleData.tagCount(); index++)
        {
            NBTTagCompound singleton = isleData.getCompoundTagAt(index);
            islands.put(single.getString("p"), new IslandCreator.IslandPos(single.getInteger("x"), single.getByte("y")+128, single.getInteger("z")));
        }

        return retVal;
    }
}
