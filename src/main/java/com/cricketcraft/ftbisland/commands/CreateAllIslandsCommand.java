package com.cricketcraft.ftbisland.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class CreateAllIslandsCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public CreateAllIslandsCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island createall");
        aliases.add("islands createall");
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
        return aliases.get(0);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {

    }
}
