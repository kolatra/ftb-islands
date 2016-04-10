package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class SetIslandSpawnCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public SetIslandSpawnCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island setspawn");
        aliases.add("islands setspawn");
    }

    @Override
    public String getCommandName() {
        return aliases.get(0);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "island setspawn <IslandName> or island setspawn <IslandName> <X> <Y> <Z>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {

    }
}
