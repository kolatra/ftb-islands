package com.cricketcraft.ftbisland.commands;

import com.cricketcraft.ftbisland.FTBIslands;
import com.cricketcraft.ftbisland.IslandUtils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class DeleteIslandCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public DeleteIslandCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island_delete");
        aliases.add("islands_delete");
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
        return "island_delete <IslandName>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {
        World world = sender.getEntityWorld();
        EntityPlayerMP player = (EntityPlayerMP) world.getPlayerEntityByName(sender.getCommandSenderName());
        boolean exists = player != null;
        IslandUtils.deleteIsland(input[0], exists ? player : null);
        if (exists) {
            player.addChatComponentMessage(new ChatComponentText(String.format("Successfully deleted island %s", input[0])));
        } else {
            FTBIslands.logger.info(String.format("Successfully deleted island %s", input[0]));
        }
    }
}
