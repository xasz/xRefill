package de.xasz.xRefill;


import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class xRefill extends JavaPlugin{
	public String mysqlUser = "";
	public String mysqlPassword = "";
	public String mysqlServer = "";
	public int mysqlPort = 3604;
	public String mysqlDatabase = "";
	public xMYSQLConnector sql = null;
	public xListener handler = null;
    @Override
    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        PluginDescriptionFile pdfFile = this.getDescription();
        this.sql = new xMYSQLConnector(this);
        
        handler = new xListener(this);   
        this.getCommand("xr").setExecutor(handler);
        
        mysqlDatabase = this.getConfig().getString("mysql.db");               
        mysqlUser = this.getConfig().getString("mysql.username");             
        mysqlServer = this.getConfig().getString("mysql.server");           
        mysqlPort = this.getConfig().getInt("mysql.port");            
        mysqlPassword = this.getConfig().getString("mysql.password");

        this.getConfig().options().copyDefaults(true);
        saveConfig();
        
    	System.out.println("xRefill enabled");
    }
    
    @Override
    public void onDisable() {
    	System.out.println("xRefill disabled");
    }

}
