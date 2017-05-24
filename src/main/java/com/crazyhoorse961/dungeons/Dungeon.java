package com.crazyhoorse961.dungeons;/**
 * Created by nini7 on 09.05.2017.
 */

import com.crazyhoorse961.dungeons.commands.DungeonCommand;
import com.crazyhoorse961.dungeons.listeners.DeathListener;
import com.crazyhoorse961.dungeons.listeners.QuitListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * @author crazyhoorse961
 */
public class Dungeon extends JavaPlugin
{


    private List<String> playersInDungeon = new ArrayList<String>();
    private Map<String, Integer> playersDeath = new HashMap<String, Integer>();
    private List<String> cachedPlayers = new ArrayList<>();

    private static Dungeon instance;


    public void onEnable(){
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        PluginCommand cmd = getCommand("dungeon");
        cmd.setAliases(Arrays.asList("dg"));
        cmd.setExecutor(new DungeonCommand());
        DeathListener.get().loadConfig();
    }

    public void onDisable(){
        saveConfig();
    }

    public static Dungeon getInstance() {
        return instance;
    }

    public List<String> getPlayersInDungeon() {
        return playersInDungeon;
    }

    public Map<String, Integer> getPlayersDeath() {
        return playersDeath;
    }

    public List<String> getCachedPlayers() {
        return cachedPlayers;
    }
}
