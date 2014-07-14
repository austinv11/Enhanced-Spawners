package io.github.austinv11.EnhancedSpawners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryGlitchTweaker implements Listener{
	//Temporary Workaround
	public InventoryGlitchTweaker(EnhancedSpawners plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(InventoryClickEvent event){
		if (event.getClick() == ClickType.SHIFT_LEFT){
			Player p = (Player) event.getWhoClicked();
			p.updateInventory();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event){
		Player p = (Player) event.getPlayer();
		p.updateInventory();
	}
}
