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
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Map;

/**
 * @author crazyhoorse961
 */
public class DeathListener implements Listener {

    private int lives;
    private Location dungeonspawn;
    public Location lobbyspawn;
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
                Dungeon.getInstance().getPlayersInDungeon().remove(name);
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Dungeon.getInstance().getConfig().getString("no-lives-death").replace("%player%", name)));
                if (deaths.isEmpty()) {
                    for (String commands : Dungeon.getInstance().getConfig().getStringList("commands.end")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands);
                    }
                    Dungeon.getInstance().getCachedPlayers().clear();
                }
                Bukkit.getScheduler().runTaskLater(Dungeon.getInstance(), () -> player.teleport(lobbyspawn), 20);
                return;
            }
        }
        deaths.put(name, deaths.containsKey(name) ? deaths.get(name) + 1 : 1);
        Bukkit.getScheduler().runTaskLater(Dungeon.getInstance(), () -> {
            if (deaths.containsKey(name)){
                player.teleport(dungeonspawn);
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Dungeon.getInstance().getConfig().getString("lost-one-life").replace("%player%", player.getName())));
            }}, 20);
    }
    private void loadConfig() {
        lives = Dungeon.getInstance().getConfig().getInt("lives");
        dungeonspawn = stringToLocation(Dungeon.getInstance().getConfig().getString("dungeonspawn"));
        lobbyspawn = stringToLocation(Dungeon.getInstance().getConfig().getString("lobbyspawn"));
        called = true;
    }

    private Location stringToLocation(String loc) {
        String[] values = loc.split(";");
        return new Location(Bukkit.getWorld(values[3]), Double.parseDouble(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]));
    }
    public static DeathListener get(){
        return new DeathListener();
    }
}
