package com.crazyhoorse961.dungeons.listeners;/**
 * Created by nini7 on 09.05.2017.
 */

import com.crazyhoorse961.dungeons.Dungeon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author crazyhoorse961
 */
public class DeathListener implements Listener
{

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        if(Dungeon.getInstance().getPlayersInDungeon().contains(p.getName())){
            if(Dungeon.getInstance().getPlayersDeath().get(p.getName()) >= Dungeon.getInstance().getConfig().getInt("lives")){
                Dungeon.getInstance().getPlayersInDungeon().forEach(pls -> Bukkit.getPlayer(pls).sendMessage(ChatColor.translateAlternateColorCodes('&', Dungeon.getInstance().getConfig().getString("no-lives-death").replace("%player%", p.getName()))));
                Dungeon.getInstance().getPlayersInDungeon().remove(p.getName());
                Dungeon.getInstance().getPlayersDeath().remove(p.getName());
                String[] values = Dungeon.getInstance().getConfig().getString("lobbyspawn").split(" ");
                p.teleport(new Location(Bukkit.getWorld(values[3]), Double.valueOf(values[0]), Double.valueOf(values[1]), Double.valueOf(values[2])));
                return;
            }
            Dungeon.getInstance().getPlayersDeath().remove(p.getName());
            Dungeon.getInstance().getPlayersDeath().put(p.getName(), Dungeon.getInstance().getPlayersDeath().get(p.getName()) + 1);
            return;
        }
    }
}
