package com.crazyhoorse961.dungeons.listeners;/**
 * Created by nini7 on 10.05.2017.
 */

import com.crazyhoorse961.dungeons.Dungeon;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author crazyhoorse961
 */
public class QuitListener implements Listener
{

    @EventHandler
    public void onQuit(PlayerQuitEvent ev){
        if(Dungeon.getInstance().getPlayersInDungeon().contains(ev.getPlayer().getName())){
            Dungeon.getInstance().getPlayersInDungeon().remove(ev.getPlayer().getName());
            Dungeon.getInstance().getPlayersDeath().remove(ev.getPlayer().getName());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent ev){
        if(Dungeon.getInstance().getCachedPlayers().contains(ev.getPlayer().getName())){
            Bukkit.getScheduler().runTaskLater(Dungeon.getInstance(), () -> ev.getPlayer().teleport(DeathListener.get().lobbyspawn), 5);
            Dungeon.getInstance().getCachedPlayers().remove(ev.getPlayer().getName());
            return;
        }
    }
}
