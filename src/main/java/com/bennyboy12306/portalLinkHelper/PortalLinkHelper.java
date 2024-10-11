package com.bennyboy12306.portalLinkHelper;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class is the main entry point for the plugin.
 * @author Bennyboy12306.
 */

public final class PortalLinkHelper extends JavaPlugin {

    /**
     * This method handles enabling the plugin.
     */
    @Override
    public void onEnable() {
        // Plugin startup logic

        //Initialise bStats
        int pluginId = 23592; //If you have modified this plugin you may want to change this ID to your own
        Metrics metrics = new Metrics(this, pluginId);

        // Register event listener
        Bukkit.getPluginManager().registerEvents(new PortalCreateListener(), this);

        //Log plugin shut down
        getLogger().info("The Portal Link Helper Plugin has been enabled");
    }

    /**
     * This method handles disabling the plugin.
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic

        //Log plugin shut down
        getLogger().info("The Portal Link Helper Plugin has been disabled");
    }
}
