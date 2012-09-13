package de.xasz.xRefill;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class xListener implements Listener{
    public xListener(xRefill plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }	
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
    {
        String commandName = command.getName().toLowerCase();
        String[] trimmedArgs = args;

        

        if(commandName.equals("xr"))
        {
            if(sender instanceof Player ){
            	if(args.length > 1){
                    if (args.length == 0 || args[0].equals("on")){
                    	sender.sendMessage("[xRefill] The next Chest or Dispenser you hit will be enabled to refill.");
                    	
                    } else if(args[0].equals("off")){
                    	sender.sendMessage("[xRefill] The next Chest or Dispenser you hit will be disabled to refill.");
                    	
                    } else{
                    	sender.sendMessage("[xRefill] Invalid Command.");
                    }
            	}
            }
            else{
            	sender.sendMessage("Not Availible in Console");
            }
        
        }	

        return false;
    }
    public boolean playerQuit(PlayerQuitEvent event){
		return false;
    	
    }
    public boolean playerKicked(PlayerQuitEvent event){
		return false;
    	
    }
}
