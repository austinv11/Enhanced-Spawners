package io.github.austinv11.InternalGUIAPI;

import io.github.austinv11.EnhancedSpawners.EnhancedSpawners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Menu implements Listener{
	private static Inventory inv;
	private static Player player;
	private static boolean isOpen = false;
	public Menu(Player p, String name, EnhancedSpawners plugin/*, String inventoryType*/){
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		player = p;
		initInv(player, name);
	}
	public static void addButton(Material type, String name, List<String> description){
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(description);
		item.setItemMeta(itemMeta);
		addItem(inv.getSize(), item);
	}
	public static void addButton(Material type, String name){
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		item.setItemMeta(itemMeta);
		addItem(inv.getSize(), item);
	}
	public static void openMenu(){
		player.openInventory(inv);
		isOpen = true;
	}
	public static boolean isMenuOpen(){
		return isOpen;
	}
	@EventHandler
	private static void onInventoryClose(InventoryCloseEvent event){
		if (event.getPlayer() == player && isOpen == true){
			isOpen = false;
		}
	}
	@EventHandler
	private static void onInventoryClick(InventoryClickEvent event){
		if (event.getWhoClicked() == player && isOpen == true){
			MenuEvent calledEvent = new MenuEvent(event.getSlot(), event.getCurrentItem(), (Player) event.getWhoClicked(), event.getClick(), event.getHotbarButton(), inv);
			Bukkit.getServer().getPluginManager().callEvent(calledEvent);
			event.setCancelled(true);
		}
	}
	private static void initInv(Player player, String name){
		inv = Bukkit.createInventory(player, 0, name);
	}
	private static void addItem(int slot, ItemStack item){
		inv.setMaxStackSize(inv.getSize()+1);
		inv.addItem(item);
	}
}
