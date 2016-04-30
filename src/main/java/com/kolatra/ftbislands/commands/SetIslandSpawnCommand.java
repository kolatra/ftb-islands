package com.kolatra.ftbislands.commands;

import com.kolatra.ftbislands.util.IslandUtils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class SetIslandSpawnCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public SetIslandSpawnCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island_setspawn");
        aliases.add("islands_setspawn");
    }

    @Override
    public String getCommandName() {
        return aliases.get(0);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "island_setspawn <IslandName> or island_setspawn <IslandName> <X> <Y> <Z>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {
        IslandUtils.setSpawnForIsland(input[0], Integer.getInteger(input[1]), Integer.getInteger(input[2]), Integer.getInteger(input[3]));
    }
}
