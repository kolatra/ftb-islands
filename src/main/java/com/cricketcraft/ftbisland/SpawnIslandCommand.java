package com.cricketcraft.ftbisland;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpawnIslandCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public SpawnIslandCommand() {
        aliases = new ArrayList();
        aliases.add("island");
    }

    @Override
    public String getCommandName() {
        return "island";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "island <text>";
    }

    @Override
    public List getCommandAliases() {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {
        ChunkCoordinates coordinates = sender.getPlayerCoordinates();
        int x = coordinates.posX;
        int y = coordinates.posY;
        int z = coordinates.posZ;
        World world = sender.getEntityWorld();
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        if(input.length == 0) {
            sender.addChatMessage(new ChatComponentText("Invalid Arguments"));
            return;
        } else if(input.length == 1) {
            if(input[0].equalsIgnoreCase("create")) {
                sender.addChatMessage(new ChatComponentText("You must give the island a name, such as /island create Cricket"));
            } else if(input[0].equalsIgnoreCase("save") && MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) {
                try {
                    FTBIslands.saveIslands(IslandCreator.islandLocations);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(input[0].equalsIgnoreCase("createAll") && MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) {
                for(IslandCreator.IslandPos pos : FTBIslands.islandLoc) {
                    IslandCreator.spawnIslandAt(world, pos.getX(), pos.getY(), pos.getZ(), sender.getCommandSenderName(), player);
                }
            }
        } else if(input.length == 2) {
            if(input[0].equalsIgnoreCase("join")) {
                IslandCreator.joinIsland(input[1].toLowerCase(), player);
            } else if(input[0].equalsIgnoreCase("create") && MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) {
                if(!IslandCreator.createIsland(world, input[1].toLowerCase(), player))
                    sender.addChatMessage(new ChatComponentText("You've already created an island for that player"));
                try {
                    FTBIslands.saveIslands(IslandCreator.islandLocations);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(input[0].equalsIgnoreCase("delete") && MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) {

            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] pars) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int index) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
