package com.cricketcraft.ftbisland.commands;

import com.cricketcraft.ftbisland.IslandUtils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class RenameIslandCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public RenameIslandCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island_rename");
        aliases.add("islands_rename");
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
        return "/island_rename <OldName> <NewName>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {
        IslandUtils.renameIsland(input[0], input[1]);
    }
}
