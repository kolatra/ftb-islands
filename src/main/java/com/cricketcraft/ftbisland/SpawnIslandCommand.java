package com.cricketcraft.ftbisland;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
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
    public int getRequiredPermissionLevel() {
        return 3;
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
            sender.addChatMessage(new ChatComponentText("Use /island create to create an island"));
            return;
        } else if(input.length == 1) {
            if(input[0].equalsIgnoreCase("create")) {
                IslandCreator.createIsland(sender.getCommandSenderName());
                try {
                    FTBIslands.saveIslands(IslandCreator.islandLocations);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(input[0].equalsIgnoreCase("save")) {
                try {
                    FTBIslands.saveIslands(IslandCreator.islandLocations);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(input[0].equalsIgnoreCase("createAll")) {
                for(IslandCreator.IslandPos pos : FTBIslands.islandLoc) {
                    IslandCreator.spawnIslandAt(world, pos.getX(), pos.getY(), pos.getZ(), sender.getCommandSenderName());
                }
            } else if(input[0].equalsIgnoreCase("help")) {
                sender.addChatMessage(new ChatComponentText("[FTB Islands] /island create, createall, save, help, join"));
            }
        } else if(input.length == 2) {
            if(input[0].equalsIgnoreCase("join")) {
                IslandCreator.joinIsland(input[1], player);
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
