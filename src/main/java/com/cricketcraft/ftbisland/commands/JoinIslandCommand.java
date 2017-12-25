package com.cricketcraft.ftbisland.commands;

import com.cricketcraft.ftbisland.IslandUtils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class JoinIslandCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public JoinIslandCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island_join");
        aliases.add("islands_join");
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
        return "island_join <IslandName> [PlayerName]";
    }
    
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] input) {
        return input.length == 2 ? getListOfStringsMatchingLastWord(input, getPlayers())
                : null;
    }

    protected String[] getPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {
        EntityPlayerMP player = null;
        if (input.length == 1) {
            player = getCommandSenderAsPlayer(sender);
        } else if (input.length == 2) {
            player = getPlayer(sender, input[1]);
        } else {
            sender.addChatMessage(new ChatComponentText("Invalid arguments!"));
            return;
        }
        IslandUtils.joinIsland(input[0], player);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }
}
