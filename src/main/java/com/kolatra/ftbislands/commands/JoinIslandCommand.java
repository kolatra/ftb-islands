package com.kolatra.ftbislands.commands;

import com.kolatra.ftbislands.util.IslandUtils;
import com.mojang.authlib.GameProfile;
import ftb.utils.api.FriendsAPI;
import ftb.utils.world.LMWorldServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class JoinIslandCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public JoinIslandCommand() {
        aliases = new ArrayList<String>();
        aliases.add("island_join");
        aliases.add("islands_join");
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
        return "island_join <IslandName>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        if (input.length == 0) {
            sender.addChatMessage(new ChatComponentText("Invalid arguments!"));
        } else {
            boolean friends = LMWorldServer.inst.getPlayer(player).isFriend(LMWorldServer.inst.getPlayer(input[0]));
            if (friends || input[0].equals(sender.getCommandSenderName())) {
                IslandUtils.joinIsland(input[0], player); // TODO this needs to be tested
            } else {
                sender.addChatMessage(new ChatComponentText("The selected player is not your friend!"));
            }
        }
    }
}
