package com.andyjusa.enchant

import org.bukkit.plugin.java.JavaPlugin

class Main:JavaPlugin(){

    override fun onEnable() {
        logger.info("mineple Story Enabled")
        server.pluginManager.registerEvents(Event(),this)
    }

    override fun onDisable() {
        logger.info("mineple Story Disabled")
    }
}