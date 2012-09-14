package de.xasz.xRefill;

import java.util.HashMap;
import java.util.Map;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class xListener implements Listener, CommandExecutor{
	private Map<String,String> waitingPlayers = null;
	private xRefill x = null; 
    public xListener(xRefill plugin) {
    	x = plugin;
    	waitingPlayers = new HashMap<String,String>();
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
                    	 if(!p.hasPermission("xRefill.on")){
                    		 p.sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.RED+" No Permission ");	 
                    		 return false;
                    	 }
                    	if(waitingPlayers.containsKey(p.getName())){
                    		if(waitingPlayers.get(p.getName()) == "on")
                    			p.sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.GREEN+" You already did this... ");
                    		else
                    			waitingPlayers.put(p.getName(), "on");
                    	}else{
                    		p.sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.GREEN+" The next Chest or Dispenser you hit will be enabled to refill.");
                    		waitingPlayers.put(p.getName(), "on");
                    	}
                    	
                    } else if(args[0].equals("off")){
                   	 if(!p.hasPermission("xRefill.off")){
                		 p.sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.RED+" No Permission ");	 
                		 return false;
                	 }
                    	if(waitingPlayers.containsKey(p.getName())){
                    		if(waitingPlayers.get(p.getName()) == "off")
                    			p.sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.GREEN+" You already did this... ");
                    		else
                    			waitingPlayers.put(p.getName(), "off");
                    	}else{
                    		p.sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.GREEN+" The next Chest or Dispenser you hit will be enabled to refill.");
                    		waitingPlayers.put(p.getName(), "off");
                    	}
                    } else if(args[0].equals("check")){
                   	 if(!p.hasPermission("xRefill.check")){
                		 p.sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.RED+" No Permission ");	 
                		 return false;
                	 }
                    	if(waitingPlayers.containsKey(p.getName())){
                    		if(waitingPlayers.get(p.getName()) == "check")
                    			p.sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.GREEN+" You already did this... ");
                    		else
                    			waitingPlayers.put(p.getName(), "check");
                    	}else{
                    		p.sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.GREEN+" The next Chest or Dispenser you hit will be checked for refill.");
                    		waitingPlayers.put(p.getName(), "check");
                    	}
                    }else {
                    	p.sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.RED+" Invalid Command.");
                    }
            	}
            }
            else{
            	sender.sendMessage("Not Availible in Console");
            }
            return true;
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
		            if(waitingPlayers.containsKey(event.getPlayer().getName())){
		            	waitingPlayers.remove(event.getPlayer().getName());
		    	}
		    	if(waitingPlayers.containsKey(event.getPlayer().getName())){
		    		waitingPlayers.remove(event.getPlayer().getName());
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
    		if(waitingPlayers.containsKey(event.getPlayer().getName())){        		
	    		if(event.getClickedBlock().getType() == Material.DISPENSER){
	            	Block block = event.getClickedBlock();
	            	if(event.getPlayer() instanceof Player){
	        			if(this.x.sql != null){
	        				String command = waitingPlayers.get(event.getPlayer().getName());
	        				if(command == "on"){
	        					if(x.sql.watchBlock(block.getWorld().getUID().toString(), block.getX(), block.getY(), block.getZ()))
	        						event.getPlayer().sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.WHITE+" Block is refilled now.");
	        					else
	        						event.getPlayer().sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.WHITE+" Error occured.");

	        				}else if(command == "check"){
	            				if(x.sql.isBlockWatched(block.getWorld().getUID().toString(), block.getX(), block.getY(), block.getZ()))
	        						event.getPlayer().sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.WHITE+" Block is refilled.");
	            				else
	        						event.getPlayer().sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.WHITE+" Block is not refilled.");
	        				}else if(command == "off"){
	        					x.sql.disBlock(block.getWorld().getUID().toString(), block.getX(), block.getY(), block.getZ());
	            				event.getPlayer().sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.WHITE+" Block is normal now.");
	            			}
	        			}
	            	}
	            }else{
	            	event.getPlayer().sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.WHITE+" Did this look like an Dispenser ?");
	            	event.getPlayer().sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.WHITE+" Disabled");	
	            }
				waitingPlayers.remove(event.getPlayer().getName());
    		}
          }
    }
	@EventHandler(priority = EventPriority.MONITOR)
    public void onBlockDispense(BlockDispenseEvent event){
        if ( event.getBlock().getState() instanceof Dispenser){
        	ItemStack item = event.getItem();
            Dispenser dispenser = (Dispenser) event.getBlock().getState();
            if (this.x.sql != null && this.x.sql.isBlockWatched(event.getBlock().getWorld().getUID().toString(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ())){
                dispenser.getInventory().addItem(event.getItem());	
            }
        }
	}
	@EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event){
        if ( event.getBlock().getState() instanceof Dispenser){
        	 if (this.x.sql != null && this.x.sql.isBlockWatched(event.getBlock().getWorld().getUID().toString(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ())){
        		 event.getPlayer().sendMessage(ChatColor.WHITE+"[xRefill]"+ChatColor.WHITE+" You are not allowed to break this block as long it is set up for automatic Refill ?");
        		 event.setCancelled(true);
        	 }
        }
	}
}
