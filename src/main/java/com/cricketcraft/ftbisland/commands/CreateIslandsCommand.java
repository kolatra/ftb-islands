package com.cricketcraft.ftbisland.commands;

import com.cricketcraft.ftbisland.util.IslandCreator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CreateIslandsCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public CreateIslandsCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island_create");
        aliases.add("islands_create");
    }

    @Override
    public List getCommandAliases() {
        return aliases;
    }

    @Override
    public String getCommandName() {
        return aliases.get(0);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "island_create <name> or create <name> <player>";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] input) {
        return input.length == 1 ? getListOfStringsMatchingLastWord(input, getPlayers())
        : (input.length == 2 ? getListOfStringsMatchingLastWord(input, getPlayers())
        : null);
    }

    protected String[] getPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {
        World world = sender.getEntityWorld();
        EntityPlayerMP player = (EntityPlayerMP) world.getPlayerEntityByName(sender.getCommandSenderName());
        if (!IslandCreator.createIsland(world, input[0], player != null ? player : null)) {

        }
    }
}
