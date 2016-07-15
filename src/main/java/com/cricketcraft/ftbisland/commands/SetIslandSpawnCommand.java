package com.cricketcraft.ftbisland.commands;

import com.cricketcraft.ftbisland.IslandUtils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

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
        String islandName = input[0];
        int x = Integer.parseInt(input[1]);
        int y = Integer.parseInt(input[2]);
        int z = Integer.parseInt(input[3]);
        IslandUtils.setSpawnForIsland(islandName, x, y, z);
        sender.addChatMessage(new ChatComponentText("Island Spawn set."));
    }
}
