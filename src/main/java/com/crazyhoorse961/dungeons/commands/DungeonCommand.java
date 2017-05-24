package com.crazyhoorse961.dungeons.commands;/**
 * Created by nini7 on 09.05.2017.
 */

import com.crazyhoorse961.dungeons.Dungeon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author crazyhoorse961
 */
public class DungeonCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        switch(strings.length){
            case 0:
                commandSender.sendMessage(ChatColor.RED + "You might specify an argument (/dg <start|end|leave|reload>");
                return true;
            case 1:
                switch(strings[0]){
                    case "end":
                        if(!commandSender.hasPermission("dg.admin.end")){
                            commandSender.sendMessage(ChatColor.RED  +"You don't have the permission to end the dungeon!");
                            return true;
                        }
                        if(Dungeon.getInstance().getPlayersInDungeon().isEmpty()){
                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Dungeon.getInstance().getConfig().getString("not-started")));
                            return true;
                        }
                            for(String commands : Dungeon.getInstance().getConfig().getStringList("commands.end")){
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands);
                        }
                        Bukkit.getOnlinePlayers().forEach(pls -> {
                            if(Dungeon.getInstance().getPlayersInDungeon().contains(pls.getName())){
                                Dungeon.getInstance().getPlayersInDungeon().remove(pls.getName());
                                Dungeon.getInstance().getPlayersDeath().remove(pls.getName());
                                Dungeon.getInstance().getCachedPlayers().remove(pls.getName());
                            }
                        });
                        String[] values = Dungeon.getInstance().getConfig().getString("lobbyspawn").split(";");
                        Location toTp = new Location(Bukkit.getWorld(values[3]), Double.valueOf(values[0]), Double.valueOf(values[1]), Double.valueOf(values[2]));
                        Bukkit.getOnlinePlayers().forEach(pls -> pls.teleport(toTp));
                        return true;
                    case "start":
                        if(!commandSender.hasPermission("dg.admin.start")){
                            commandSender.sendMessage(ChatColor.RED  + "You don't have the permission to start the dungeon!");
                            return true;
                        }
                        if(!Dungeon.getInstance().getPlayersInDungeon().isEmpty()){
                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Dungeon.getInstance().getConfig().getString("already-started")));
                            return true;
                        }
                        Bukkit.getOnlinePlayers().forEach(pls -> Dungeon.getInstance().getPlayersInDungeon().add(pls.getName()));
                        Bukkit.getOnlinePlayers().forEach(pls -> Dungeon.getInstance().getCachedPlayers().add(pls.getName()));
                        Bukkit.getOnlinePlayers().forEach(pls -> Dungeon.getInstance().getPlayersDeath().put(pls.getName(), 0 ));
                        String[] valuesD = Dungeon.getInstance().getConfig().getString("dungeonspawn").split(";");
                        Location toTpD = new Location(Bukkit.getWorld(valuesD[3]), Double.valueOf(valuesD[0]), Double.valueOf(valuesD[1]), Double.valueOf(valuesD[2]));
                        Bukkit.getOnlinePlayers().forEach(pls -> pls.teleport(toTpD));
                            for(String commands : Dungeon.getInstance().getConfig().getStringList("commands.start")){
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands);
                        }
                        return true;
                    case "leave":
                        if(!commandSender.hasPermission("dg.player.leave") || (!(commandSender instanceof Player))){
                            commandSender.sendMessage(ChatColor.RED + "You don't have the permission or either you aren't a player");
                            return true;
                        }
                        Dungeon.getInstance().getPlayersInDungeon().remove(commandSender.getName());
                        Dungeon.getInstance().getPlayersDeath().remove(commandSender.getName());
                        String[] lobby = Dungeon.getInstance().getConfig().getString("lobbyspawn").split(";");
                        ((Player) commandSender).teleport(new Location(Bukkit.getWorld(lobby[3]), Double.valueOf(lobby[0]), Double.valueOf(lobby[1]), Double.valueOf(lobby[2])));
                        commandSender.sendMessage(ChatColor.GREEN + "You leaved the dungeon!");
                        return true;
                    case "reload":
                        if(!commandSender.hasPermission("dg.admin.reload")){
                            commandSender.sendMessage(ChatColor.RED + "You don't have the permission to reload the config");
                            return true;
                        }
                        Dungeon.getInstance().reloadConfig();
                }
        }
        return true;
    }
}
