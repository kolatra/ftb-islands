package com.cricketcraft.ftbisland.commands;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.util.IslandUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveIslandsCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public SaveIslandsCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island_save");
        aliases.add("islands_save");
    }

    @Override
    public String getCommandName() {
        return aliases.get(0);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "island_save";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {
        try {
            FTBIslands.saveIslands(FTBIslands.getIslands());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
