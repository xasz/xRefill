package de.xasz.xRefill;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class xListener implements Listener, CommandExecutor{
	private List<String> waitingOnPlayers = null;
	private List<String> waitingOffPlayers = null;
	private List<String> waitingCheckPlayers = null;
	private xRefill x = null; 
    public xListener(xRefill plugin) {
    	x = plugin;
    	waitingOnPlayers = new ArrayList<String>();
    	waitingOffPlayers = new ArrayList<String>();
    	waitingCheckPlayers = new ArrayList<String>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
    {
        String commandName = command.getName().toLowerCase();
        String[] trimmedArgs = args;
        if(commandName.equals("xr"))
        {
            if(sender instanceof Player ){
            	Player p = x.getServer().getPlayer(sender.getName());
            	if(args.length <= 1){
                    if (args.length == 0 || args[0].equals("on")){
                    	if(waitingOffPlayers.contains(p.getName())){
                    		waitingOffPlayers.remove(p.getName());
                    	}
                    	if(waitingCheckPlayers.contains(p.getName())){
                    		waitingCheckPlayers.remove(p.getName());
                    	}
                    	if(waitingOnPlayers.contains(p.getName())){
                    		System.out.println("bla");
                    		p.sendMessage(ChatColor.WHITE+"[xRefill] You already did this... ");
                    	}else{
                    		p.sendMessage(ChatColor.WHITE+"[xRefill] The next Chest or Dispenser you hit will be enabled to refill.");
                    		waitingOnPlayers.add(p.getName());
                    	}
                    	
                    } else if(args[0].equals("off")){
                    	if(waitingOnPlayers.contains(p.getName())){
                    		waitingOnPlayers.remove(p.getName());
                    	}
                    	if(waitingCheckPlayers.contains(p.getName())){
                    		waitingCheckPlayers.remove(p.getName());
                    	}
                    	if(waitingOffPlayers.contains(p.getName())){
                    		p.sendMessage("[xRefill] You already did this... ");
                    	}else{
                        	p.sendMessage("[xRefill] The next Chest or Dispenser you hit will be disabled to refill.");
                    		waitingOffPlayers.add(p.getName());
                    	}
                    } else if(args[0].equals("check")){
                    	if(waitingOnPlayers.contains(p.getName())){
                    		waitingOnPlayers.remove(p.getName());
                    	}	
                    	if(waitingOffPlayers.contains(p.getName())){
                    		waitingOffPlayers.remove(p.getName());
                    	}
                    	p.sendMessage("[xRefill] The next Chest or Dispenser you hit will be checked.");
                    	waitingCheckPlayers.add(p.getName());
                    }else {
                    	p.sendMessage("[xRefill] Invalid Command.");
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
		return playerKicked(event);
    	
    }
    public boolean playerKicked(PlayerQuitEvent event){
    	try
    	{
	    	if(event.getPlayer() instanceof Player ){
		            if(waitingOffPlayers.contains(event.getPlayer().getName())){
		    		waitingOffPlayers.remove(event.getPlayer().getName());
		    	}
		    	if(waitingOffPlayers.contains(event.getPlayer().getName())){
		    		waitingOffPlayers.remove(event.getPlayer().getName());
		    	}
	    	}
    	}catch(Exception ex){
    		System.out.println(ex.getMessage());
    	}
    	return true;
    }
	@EventHandler(priority = EventPriority.NORMAL)
    public void playerLeftClick(PlayerInteractEvent event){
    	if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            if(event.getClickedBlock().getType() == Material.DISPENSER){
            	Block block = event.getClickedBlock();
            	if(event.getPlayer() instanceof Player){
            		if(waitingCheckPlayers.contains(event.getPlayer().getName())){
            			if(this.x.sql != null){
            				if(x.sql.isBlockWatched(block.getWorld().getUID().toString(), block.getX(), block.getY(), block.getZ()))
            						event.getPlayer().sendMessage("[xRefill] Block is refilled.");
            				else
        						event.getPlayer().sendMessage("[xRefill] Block is not refilled.");
            			}
            		}
            		if(waitingOnPlayers.contains(event.getPlayer().getName())){
            			if(this.x.sql != null){
            				x.sql.watchBlock(block.getWorld().getUID().toString(), block.getX(), block.getY(), block.getZ());
            				event.getPlayer().sendMessage("[xRefill] Block is refilled now.");
            			}
            			waitingOnPlayers.remove(event.getPlayer().getName());
            		}else if(waitingOffPlayers.contains(event.getPlayer().getName())){
                			if(this.x.sql != null){
                				x.sql.disBlock(block.getWorld().getUID().toString(), block.getX(), block.getY(), block.getZ());
                				event.getPlayer().sendMessage("[xRefill] Block is normal now.");
                			}
                			waitingOffPlayers.remove(event.getPlayer().getName());
                	}
            	}
            }else{
            	if(waitingOffPlayers.contains(event.getPlayer().getName()) || waitingOnPlayers.contains(event.getPlayer().getName()) || waitingCheckPlayers.contains(event.getPlayer().getName()))
            	{
            		event.getPlayer().sendMessage("Not an Dispenser ....");
        			waitingOffPlayers.remove(event.getPlayer().getName());
        			waitingOnPlayers.remove(event.getPlayer().getName());
        			waitingCheckPlayers.remove(event.getPlayer().getName());
            	}
            }
            
          }
    }
}
