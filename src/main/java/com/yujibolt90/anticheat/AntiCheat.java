package com.yujibolt90.anticheat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiCheat extends JavaPlugin implements Listener {
		
		Map<String, Long> playersInCombat = new HashMap<String, Long>();
		
		static final int COMBAT_TIME = 30;
		
		boolean takeTime = true;
		long currentTime;
				
	    @Override
	    public void onEnable() {
	    	getServer().getPluginManager().registerEvents(this, this);
	        getLogger().info("AntiCheat activated!");
	    }
	    
	    @Override
	    public void onDisable() {
	    	getLogger().info("AntiCheat de-activated!");
	    }
	    
	    
	    @EventHandler
	    public void onPlayerLeaveGame(PlayerQuitEvent event) {
	        Player player = event.getPlayer();
	        if (playersInCombat.containsKey(player.getName())) {
	        	player.setHealth(0.0D);
	        	getLogger().info("Pvp log");
	        }
	    }
	    
	    @EventHandler
	    public void isDamaged(EntityDamageByEntityEvent event) {
	    	if (event.getEntityType() == EntityType.PLAYER){
	    		Player victim = (Player) event.getEntity();
	    		Player attacker = (Player) event.getDamager();
	    		long timestamp = victim.getWorld().getTime();
	    		
	    		getLogger().info("Logged in combat");
	    		
	    		playersInCombat.put(victim.getName(), timestamp);
	    		playersInCombat.put(attacker.getName(), timestamp);
	    		
	    		Bukkit.getScheduler().runTaskLater(this, (task) ->
	            {
	                task.cancel();
	                playersInCombat.remove(victim.getName());
	                playersInCombat.remove(attacker.getName());
	                getLogger().info("Out of combat");
	             }, 20L * COMBAT_TIME);}
	    				 
	    }
}
