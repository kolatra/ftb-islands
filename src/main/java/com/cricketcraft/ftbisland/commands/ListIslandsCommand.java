package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class ListIslandsCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public ListIslandsCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island list");
        aliases.add("islands list");
    }

    @Override
    public String getCommandName() {
        return aliases.get(0);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "island list";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {

    }
}
