package com.kolatra.ftbislands.commands;

import com.kolatra.ftbislands.FTBIslands;
import com.kolatra.ftbislands.util.IslandCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class ListIslandsCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public ListIslandsCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island_list");
        aliases.add("islands_list");
    }

    @Override
    public String getCommandName() {
        return aliases.get(0);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "island_list";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {
        final EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        try {
            for (Map.Entry<String, IslandCreator.IslandPos> entry : FTBIslands.getIslands().entrySet()) {
                String key = entry.getKey();
                player.addChatComponentMessage(new ChatComponentText(key));
            }
            //FTBIslands.getIslands().forEach((k, v) -> player.addChatComponentMessage(new ChatComponentText(k)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
