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
	private static String currentAPIVersion = "1.0.0";//TODO update
	private Inventory inv;
	private Player player;
	private boolean isOpen = false;
	private Button buttons[];
	private int slotCount = 0;
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
		buttons[slot].setItem(item);
		refresh();
	}
	public void setButton(int slot, Material type, String name){//Replaces a button in the given slot with a new one
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		item.setItemMeta(itemMeta);
		buttons[slot].setItem(item);
		refresh();
	}
	public void setButton(int slot, ItemStack item){//Replaces a button in the given slot with a new one (for advanced users)
		buttons[slot].setItem(item);
		refresh();
	}
	public ItemStack getButton(int slot){//Gets the item in the given menu slot (let hackiness ensue!)
		return inv.getItem(slot);
	}
	public int getQuantity(int slot){//Gets the quantity of the button's item in the given slot
		return inv.getItem(slot).getAmount();
	}
	public void setQuantity(int slot, int quantity){//Sets the quantity of a button in the given slot
		ItemStack temp;
		temp = buttons[slot].getItem();
		temp.setAmount(quantity);
		buttons[slot].setItem(temp);
		temp = buttons[slot].getToggleItem();
		temp.setAmount(quantity);
		buttons[slot].setToggleItem(temp);
		refresh();
	}
	public int getNumberOfButtons(){//Gets the number of slots currently in the menu
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
	public boolean isButtonToggled(int slot){//Returns whether or not the given button has been toggled
		return buttons[slot].isToggled();
	}
	public ItemStack getButtonToggleItem(int slot){//Returns the item used by the given button when it (next) gets toggled
		return buttons[slot].getToggleItem();
	}
	public void setButtonToggleItem(int slot, Material type, String name, List<String> description){//Sets the item used when the button is (next) toggled
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		itemMeta.setLore(description);
		item.setItemMeta(itemMeta);
		buttons[slot].setToggleItem(item);
	}
	public void setButtonToggleItem(int slot, Material type, String name){//Sets the item used when the button is (next) toggled
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		item.setItemMeta(itemMeta);
		buttons[slot].setToggleItem(item);
	}
	public void setButtonToggleItem(int slot, ItemStack item){//Sets the item used when the button is (next) toggled (for advanced users)
		buttons[slot].setToggleItem(item);
	}
	public void setButtonToggle(int slot, boolean canToggle){//Sets whether a button is togglable
		buttons[slot].setToggle(canToggle);
	}
	public void toggleButton(int slot){//Toggles the given button
		buttons[slot].toggle();
		refresh();
	}
	public static String getAPIVersion(){
		return currentAPIVersion;
	}
	@EventHandler
	private void onInventoryClose(InventoryCloseEvent event){
		if (event.getPlayer() == player && isOpen == true){
			isOpen = false;
		}
	}
	@EventHandler
	private void onInventoryClick(InventoryClickEvent event){
		if (event.getWhoClicked() == player && isOpen == true && event.getSlot() < inv.getSize()){
			MenuEvent calledEvent = new MenuEvent(event.getSlot(), event.getCurrentItem(), (Player) event.getWhoClicked(), event.getClick(), event.getHotbarButton(), inv, this);
			Bukkit.getServer().getPluginManager().callEvent(calledEvent);
			event.setCancelled(true);
			//event.setCurrentItem(null);
			this.closeMenu();
			this.openMenu();
			if (buttons[calledEvent.getSlot()] != null && !calledEvent.isCancelled()){
				if (buttons[calledEvent.getSlot()].canToggle()){
					buttons[calledEvent.getSlot()].toggle();
					refresh();
				}
			}
		}
	}
	private void refresh(){
		inv.clear();
		for (int i = 0; i < buttons.length; i ++){
			if (buttons[i] != null){
				inv.setItem(buttons[i].getSlot(), buttons[i].getItem());
			}
		}
	}
	private void initInv(Player player, String name, int slotNum){
		if (slotNum % 9 != 0){
			Bukkit.getServer().getLogger().severe("Please tell your local plugin author: 'You must use a multiple of 9 for a slot number!'");
			slotNum = slotNum + (9 - (slotNum % 9));
		}
		for (int i = 0; i < slotNum; i++){
			buttons[i] = null;
		}
		inv = Bukkit.createInventory(player, slotNum, name);
	}
	private void addItem(ItemStack item){
		while (buttons[slotCount] != null){
			slotCount++;
		}
		inv.addItem(item);
		buttons[slotCount] = new Button(this, item, slotCount, false, null, false);
		slotCount++;
	}
}
