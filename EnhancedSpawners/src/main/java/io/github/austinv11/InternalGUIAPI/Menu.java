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
	private Inventory inv;
	private Player player;
	private boolean isOpen = false;
	public Menu(Player p, String name, int numberOfSlots, EnhancedSpawners plugin/*, String inventoryType*/){
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		player = p;
		initInv(player, name, numberOfSlots);
	}
	public void addButton(Material type, String name, List<String> description){//Adds a button to the menu
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		itemMeta.setLore(description);
		item.setItemMeta(itemMeta);
		addItem(item);
	}
	public void addButton(Material type, String name){//Adds a button to the menu
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		item.setItemMeta(itemMeta);
		addItem(item);
	}
	public void addButton(ItemStack buttonItem){//Adds a button to the menu (for advanced users)
		addItem(buttonItem);
	}
	public void openMenu(){//Opens the menu (for the menu's player)
		player.openInventory(inv);
		isOpen = true;
	}
	public boolean isMenuOpen(){//Returns whether or not the menu is open
		return isOpen;
	}
	public Inventory getMenuInventory(){//Returns the menu's inventory (let hackiness ensue!)
		return inv;
	}
	public String getMenuName(){//Returns the name of the menu;
		return inv.getTitle();
	}
	public void setButton(int slot, Material type, String name, List<String> description){//Replaces a button in the given slot with a new one
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		itemMeta.setLore(description);
		item.setItemMeta(itemMeta);
		inv.setItem(slot, item);
	}
	public void setButton(int slot, Material type, String name){//Replaces a button in the given slot with a new one
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		item.setItemMeta(itemMeta);
		inv.setItem(slot, item);
	}
	public ItemStack getButton(int slot){//Gets the item in the given menu slot (let hackiness ensue!)
		return inv.getItem(slot);
	}
	public int getQuantity(int slot){//Gets the quantity of the button's item in the given slot
		return inv.getItem(slot).getAmount();
	}
	public void setQuantity(int slot, int quantity){//Sets the quantity of a button in the given slot
		inv.getItem(slot).setAmount(quantity);
	}
	public int getNumberOfButtons(){//Gets the number of buttons currently in the menu
		return inv.getSize();
	}
	public void closeMenu(){//Closes the menu for the menu's player (if its open)
		if (isOpen){
			player.closeInventory();
		}
	}
	public Player getPlayer(){//Returns the player involved in this menu
		return player;
	}
	@EventHandler
	private void onInventoryClose(InventoryCloseEvent event){
		if (event.getPlayer() == player && isOpen == true){
			isOpen = false;
		}
	}
	@EventHandler
	private void onInventoryClick(InventoryClickEvent event){
		if (event.getWhoClicked() == player && isOpen == true){
			MenuEvent calledEvent = new MenuEvent(event.getSlot(), event.getCurrentItem(), (Player) event.getWhoClicked(), event.getClick(), event.getHotbarButton(), inv);
			Bukkit.getServer().getPluginManager().callEvent(calledEvent);
			event.setCancelled(true);
			//event.setCurrentItem(null);
			this.closeMenu();
			this.openMenu();
		}
	}
	private void initInv(Player player, String name, int slotNum){
		inv = Bukkit.createInventory(player, slotNum, name);
	}
	private void addItem(ItemStack item){
		inv.addItem(item);
	}
}
