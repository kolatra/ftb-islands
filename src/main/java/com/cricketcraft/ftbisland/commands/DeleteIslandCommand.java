package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class DeleteIslandCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public DeleteIslandCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island delete");
        aliases.add("islands delete");
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
        return "island delete <IslandName>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {

    }
}
