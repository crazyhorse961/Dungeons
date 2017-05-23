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
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Map;

/**
 * @author crazyhoorse961
 */
public class DeathListener implements Listener
{

    private int lives;
    private Location dungeonspawn;
    private Location lobbyspawn;
    private boolean called;

    private Map<String, Integer> deaths = Dungeon.getInstance().getPlayersDeath();

    @EventHandler
    public void onPlayerDeath(PlayerRespawnEvent e) {
        if (!called) {
            loadConfig();
        }
        Player player = e.getPlayer();
        if (!Dungeon.getInstance().getPlayersInDungeon().contains(player.getName())) {
            return;
        }
        String name = player.getName().toLowerCase();
        if (deaths.containsKey(name)) {
            if (deaths.get(name) == lives) {
                deaths.remove(name);
                e.setRespawnLocation(lobbyspawn);
                Dungeon.getInstance().getPlayersInDungeon().forEach(pls -> Bukkit.getPlayer(pls).sendMessage(ChatColor.translateAlternateColorCodes('&', Dungeon.getInstance().getConfig().getString("no-lives-death").replace("%player%", player.getName()))));
                if (Dungeon.getInstance().getPlayersInDungeon().contains("done")) {
                    for (String str : Dungeon.getInstance().getCachedPlayers()) {
                        for (String commands : Dungeon.getInstance().getConfig().getStringList("commands.end")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replace("%player%", str));
                        }
                    }
                    Dungeon.getInstance().getCachedPlayers().clear();
                    return;
                }
            }
            deaths.putIfAbsent(name, 0);
            deaths.put(name, deaths.get(name) + 1);
            Bukkit.getScheduler().runTaskLater(Dungeon.getInstance(), () -> player.teleport(dungeonspawn), 2);
            Dungeon.getInstance().getPlayersInDungeon().forEach(pls -> Bukkit.getPlayer(pls).sendMessage(ChatColor.translateAlternateColorCodes('&', Dungeon.getInstance().getConfig().getString("lost-one-life").replace("%player%", player.getName()))));
        }
    }

    private void loadConfig(){
        lives = Dungeon.getInstance().getConfig().getInt("lives");
        dungeonspawn = stringToLocation(Dungeon.getInstance().getConfig().getString("dungeonspawn"));
        lobbyspawn = stringToLocation(Dungeon.getInstance().getConfig().getString("lobbyspawn"));
        called = true;
    }

    private Location stringToLocation(String loc){
        String[] values = loc.split(";");
        return new Location(Bukkit.getWorld(values[3]), Double.parseDouble(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]));
    }
}
