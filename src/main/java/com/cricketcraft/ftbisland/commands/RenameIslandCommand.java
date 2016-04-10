package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class RenameIslandCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public RenameIslandCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island rename");
        aliases.add("islands rename");
    }

    @Override
    public String getCommandName() {
        return aliases.get(0);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "island rename <OldName> <NewName>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {

    }
}
