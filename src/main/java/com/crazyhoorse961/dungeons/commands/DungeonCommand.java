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
                commandSender.sendMessage(ChatColor.RED + "You might specify an argument (/dg <start/end>");
                return true;
            case 1:
                switch(strings[0]){
                    case "end":
                        if(!commandSender.hasPermission("dg.admin.end")){
                            commandSender.sendMessage(ChatColor.RED  +"You don't have the permission to end the dungeon!");
                            return true;
                        }
                        //END DUNGEON
                    case "start":
                        if(!commandSender.hasPermission("dg.admin.start")){
                            commandSender.sendMessage(ChatColor.RED  + "You don't have the permission to start the dungeon!");
                            return true;
                        }
                        //START DUNGEON
                    case "leave":
                        if(!commandSender.hasPermission("dg.player.leave") || (!(commandSender instanceof Player))){
                            commandSender.sendMessage(ChatColor.RED + "You don't have the permission or either you aren't a player");
                        }
                        Dungeon.getInstance().getPlayersInDungeon().remove(commandSender.getName());
                        Dungeon.getInstance().getPlayersDeath().remove(commandSender.getName());
                        String[] values = Dungeon.getInstance().getConfig().getString("lobbyspawn").split(" ");
                        ((Player) commandSender).teleport(new Location(Bukkit.getWorld(values[3]), Double.valueOf(values[0]), Double.valueOf(values[1]), Double.valueOf(values[2])));
                        commandSender.sendMessage(ChatColor.GREEN + "You leaved the dungeon!");
                }
        }
        return true;
    }
}
