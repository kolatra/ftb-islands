package com.cricketcraft.ftbisland;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class SpawnIslandCommand extends CommandBase implements ICommand {
    private List<String> aliases;

    public SpawnIslandCommand() {
        aliases = new ArrayList();
        aliases.add("island");
    }

    @Override
    public String getCommandName() {
        return "island";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "island <text>";
    }

    @Override
    public List getCommandAliases() {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] input) {
        World world = sender.getEntityWorld();
        EntityPlayerMP player = (EntityPlayerMP) world.getPlayerEntityByName(sender.getCommandSenderName());

        if (player != null) {
            if(input.length == 0) {
                sender.addChatMessage(new ChatComponentText("Invalid Arguments"));
                return;
            } else if(input.length == 1) {
                if(input[0].equalsIgnoreCase("create")) {
                    sender.addChatMessage(new ChatComponentText("You must give the island a name, such as /island create Cricket"));
                } else if(input[0].equalsIgnoreCase("save") && MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) {
                    try {
                        FTBIslands.saveIslands(IslandCreator.islandLocations);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(input[0].equalsIgnoreCase("createAll") && MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) {
                    for(IslandCreator.IslandPos pos : FTBIslands.islandLoc) {
                        IslandCreator.spawnIslandAt(world, pos.getX(), pos.getY(), pos.getZ(), sender.getCommandSenderName(), player);
                    }
                } else if(input[0].equalsIgnoreCase("list")) {
                    StringBuilder builder = new StringBuilder();
                    try {
                        for(String string : FTBIslands.getIslands().keySet()) {
                            builder.append(string + ", ");
                        }
                        player.addChatMessage(new ChatComponentText("Current Islands: " + builder.toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if(input[0].equalsIgnoreCase("help")) {
                    player.addChatMessage(new ChatComponentText("Commands are: list, createAll(Only use if you don't want the join command), save, join <name>, create <name>, delete <name, rename <name>, setSpawn <name>"));
                }
            } else if(input.length == 2) {
                if(input[0].equalsIgnoreCase("join")) {
                    IslandCreator.joinIsland(input[1].toLowerCase(), player);
                } else if(input[0].equalsIgnoreCase("create") && MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) {
                    if(!IslandCreator.createIsland(world, input[1].toLowerCase(), player))
                        sender.addChatMessage(new ChatComponentText("You've already created an island for that player"));
                    try {
                        FTBIslands.saveIslands(IslandCreator.islandLocations);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(input[0].equalsIgnoreCase("delete") && MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) {
                    IslandCreator.deleteIsland(input[1].toLowerCase(), player);
                } else if (input[0].equalsIgnoreCase("setSpawn")) {
                    IslandCreator.setSpawnForIsland(input[1].toLowerCase(), sender.getPlayerCoordinates().posX, sender.getPlayerCoordinates().posY, sender.getPlayerCoordinates().posZ);
                    sender.addChatMessage(new ChatComponentText(String.format("Set spawn for island %s to %d, %d, %d", input[1].toLowerCase(), sender.getPlayerCoordinates().posX, sender.getPlayerCoordinates().posY, sender.getPlayerCoordinates().posZ)));
                }
            } else if(input.length == 3) {
                if (input[0].equalsIgnoreCase("rename")) {
                    IslandCreator.renameIsland(input[1].toLowerCase(), input[2].toLowerCase());
                    sender.addChatMessage(new ChatComponentText(String.format("Renamed island %s to %s", input[1].toLowerCase(), input[2].toLowerCase())));
                }
            }
        } else {
            if (input.length == 0) {
                FTBIslands.logger.info("Invalid arguments.");
                return;
            } else if (input.length == 1) {
                if (input[0].equalsIgnoreCase("create")) {
                    FTBIslands.logger.info("You must give the island a name, such as /island create Cricket");
                } else if (input[0].equalsIgnoreCase("save")) {
                    try {
                        FTBIslands.saveIslands(IslandCreator.islandLocations);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (input[0].equalsIgnoreCase("createAll")) {
                    for (IslandCreator.IslandPos pos : FTBIslands.islandLoc) {
                        IslandCreator.spawnIslandAt(world, pos.getX(), pos.getY(), pos.getZ(),  sender.getCommandSenderName(), null);
                    }
                } else if(input[0].equalsIgnoreCase("list")) {
                    StringBuilder builder = new StringBuilder();
                    try {
                        for(String string : FTBIslands.getIslands().keySet()) {
                            builder.append(string + ", ");
                        }
                        FTBIslands.logger.info(("Current Islands: " + builder.toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if(input[0].equalsIgnoreCase("help")) {
                    FTBIslands.logger.info((new ChatComponentText("Commands are: list, createAll(Only use if you don't want the join command), save, join <name>, create <name>, delete <name, rename <name>, setSpawn <name>")));
                }
            } else if (input.length == 2) {
                if (input[0].equalsIgnoreCase("join")) {
                    FTBIslands.logger.info("Join can only be run from in-game.");
                } else if (input[0].equalsIgnoreCase("create")) {
                    if (!IslandCreator.createIsland(world, input[1].toLowerCase(), null))
                        FTBIslands.logger.info("You've already created an island for that player.");
                    try {
                        FTBIslands.saveIslands(IslandCreator.islandLocations);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (input[0].equalsIgnoreCase("delete")) {
                    IslandCreator.deleteIsland(input[1].toLowerCase(), null);
                }
            } else if(input.length == 3) {
                if (input[0].equalsIgnoreCase("rename")) {
                    IslandCreator.renameIsland(input[1].toLowerCase(), input[2].toLowerCase());
                    FTBIslands.logger.info(String.format("Renamed island %s to %s", input[1].toLowerCase(), input[2].toLowerCase()));
                }
            } else if(input.length == 5) {
                if (input[0].equalsIgnoreCase("setSpawn")) {
                    IslandCreator.setSpawnForIsland(input[1].toLowerCase(), Integer.valueOf(input[2]), Integer.valueOf(input[3]), Integer.valueOf(input[4]));
                    FTBIslands.logger.info(String.format("Set island spawn for %s to %d, %d, %d", input[1].toLowerCase(), Integer.valueOf(input[2]), Integer.valueOf(input[3]), Integer.valueOf(input[4])));
                }
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] pars) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int index) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
