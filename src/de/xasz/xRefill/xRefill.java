package de.xasz.xRefill;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class xRefill extends JavaPlugin{
    @Override
    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        PluginDescriptionFile pdfFile = this.getDescription();
    	System.out.println("xRefill enabled");
    }
    
    @Override
    public void onDisable() {
    	System.out.println("xRefill disabled");
    }

}
