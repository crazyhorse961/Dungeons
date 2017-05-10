package com.crazyhoorse961.dungeons;/**
 * Created by nini7 on 09.05.2017.
 */

import com.crazyhoorse961.dungeons.commands.DungeonCommand;
import com.crazyhoorse961.dungeons.listeners.DeathListener;
import com.crazyhoorse961.dungeons.listeners.QuitListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author crazyhoorse961
 */
public class Dungeon extends JavaPlugin
{


    private List<String> playersInDungeon = new ArrayList<String>();
    private Map<String, Integer> playersDeath = new HashMap<String, Integer>();

    private static Dungeon instance;


    public void onEnable(){
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getCommand("dungeon").setExecutor(new DungeonCommand());
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
}
