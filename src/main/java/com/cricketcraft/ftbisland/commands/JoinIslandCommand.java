package com.cricketcraft.ftbisland.commands;

import com.cricketcraft.ftbisland.util.IslandUtils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

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
    public String getCommandUsage(ICommandSender sender) {
        return "island_join <IslandName>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        IslandUtils.joinIsland(input[0], player);
    }
}
