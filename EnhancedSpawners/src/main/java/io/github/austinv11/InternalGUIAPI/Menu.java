package io.github.austinv11.InternalGUIAPI;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
/**
 * <b>
 * Menu Object:
 * </b>
 * Contains all menu-related methods
 *
 */
public class Menu implements Listener{
	private static String currentAPIVersion = "1.0.2";
	private Inventory inv;
	private Player player;
	private boolean isOpen = false;
	private boolean buttonStatus[];
	private Button buttons[];
	private int slotCount = 0;
	private static Plugin api = Bukkit.getPluginManager().getPlugin("EnhancedSpawners");
	/**
	 * Menu object constructor.
	 * @param p Player involved with this menu.
	 * @param name Name of the menu.
	 * @param numberOfSlots Number of slots in the menu (must be either 0 or a multiple of 9).
	 */
	public Menu(Player p, String name, int numberOfSlots/*, String inventoryType*/){
		Bukkit.getServer().getPluginManager().registerEvents(this, api);
		player = p;
		initInv(player, name, numberOfSlots);
	}
	/**
	 * Adds a button to the menu.
	 * @param type Material used to represent the button.
	 * @param name Name of the button.
	 * @param description List used as the buttons description.
	 */
	public void addButton(Material type, String name, List<String> description){
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		itemMeta.setLore(description);
		item.setItemMeta(itemMeta);
		addItem(item);
	}
	/**
	 * Adds a button to the menu.
	 * @param type Material used to represent the button.
	 * @param name Name of the button.
	 */
	public void addButton(Material type, String name){
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		item.setItemMeta(itemMeta);
		addItem(item);
	}
	/**
	 * Adds a button to the menu (for advanced users).
	 * @param buttonItem ItemStack used to represent the button.
	 */
	public void addButton(ItemStack buttonItem){
		addItem(buttonItem);
	}
	/**
	 * Opens the menu (for the menu's player).
	 */
	public void openMenu(){
		player.openInventory(inv);
		isOpen = true;
	}
	/**
	 * Returns whether or not the menu is open.
	 * @return Whether the menu is open.
	 */
	public boolean isMenuOpen(){
		return isOpen;
	}
	/**
	 * Returns the menu's inventory.
	 * @return A copy of the menu's inventory.
	 */
	public Inventory getMenuInventory(){
		return inv;
	}
	/**
	 * Returns the name of the menu.
	 * @return The name of the menu's inventory.
	 */
	public String getMenuName(){
		return inv.getTitle();
	}
	/**
	 * Replaces a button in the given slot with a new one or creates a new button in the given slot.
	 * @param slot Desired slot number for the button.
	 * @param type Material used to represent the button.
	 * @param name Name of the button.
	 * @param description List used as the buttons description.
	 */
	public void setButton(int slot, Material type, String name, List<String> description){
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		itemMeta.setLore(description);
		item.setItemMeta(itemMeta);
		if (buttonStatus[slot] == true){
			buttons[slot].setItem(item);
		}else{
			buttons[slot] = new Button(this, item, slot, false, null, false);
			buttonStatus[slot] = true;
		}
		refresh();
	}
	/**
	 * Replaces a button in the given slot with a new one or creates a new button in the given slot.
	 * @param slot Desired slot number for the button.
	 * @param type Material used to represent the button.
	 * @param name Name of the button.
	 */
	public void setButton(int slot, Material type, String name){
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		item.setItemMeta(itemMeta);
		if (buttonStatus[slot] == true){
			buttons[slot].setItem(item);
		}else{
			buttons[slot] = new Button(this, item, slot, false, null, false);
			buttonStatus[slot] = true;
		}
		refresh();
	}
	/**
	 * Replaces a button in the given slot with a new one or creates a new button in the given slot (for advanced users).
	 * @param slot Desired slot number for the button.
	 * @param item ItemStack used to represent the button.
	 */
	public void setButton(int slot, ItemStack item){
		if (buttonStatus[slot] == true){
			buttons[slot].setItem(item);
		}else{
			buttons[slot] = new Button(this, item, slot, false, null, false);
			buttonStatus[slot] = true;
		}
		refresh();
	}
	/**
	 * Gets the item in the given menu slot.
	 * @param slot Slot number for the button.
	 * @return A copied ItemStack representing the item in the given slot.
	 */
	public ItemStack getButton(int slot){
		return inv.getItem(slot);
	}
	/**
	 * Gets the quantity of the button's item in the given slot.
	 * @param slot Slot number for the button.
	 * @return The number of items in the menu's inventory slot.
	 */
	public int getQuantity(int slot){
		return inv.getItem(slot).getAmount();
	}
	/**
	 * Sets the quantity of a button in the given slot.
	 * @param slot Slot number for the button.
	 * @param quantity Amount to set the quantity of items to.
	 */
	public void setQuantity(int slot, int quantity){
		ItemStack temp;
		temp = buttons[slot].getItem();
		temp.setAmount(quantity);
		buttons[slot].setItem(temp);
		temp = buttons[slot].getToggleItem();
		temp.setAmount(quantity);
		buttons[slot].setToggleItem(temp);
		refresh();
	}
	/**
	 * Gets the number of slots currently in the menu.
	 * @return The number of slots in the menu's inventory.
	 */
	public int getNumberOfButtons(){
		return inv.getSize();
	}
	/**
	 * Closes the menu for the menu's player (if its open).
	 */
	public void closeMenu(){
		if (isOpen){
			player.closeInventory();
		}
	}
	/**
	 * Returns the player involved in this menu.
	 * @return A copy of the player object involved with this menu.
	 */
	public Player getPlayer(){
		return player;
	}
	/**
	 * Returns whether or not the given button has been toggled.
	 * @param slot Slot number for the button.
	 * @return Boolean, whether or not the button has been toggled.
	 */
	public boolean isButtonToggled(int slot){
		return buttons[slot].isToggled();
	}
	/**
	 * Returns the item used by the given button when it (next) gets toggled.
	 * @param slot Slot number for the button.
	 * @return A copy of the ItemStack object used as the net toggle item.
	 */
	public ItemStack getButtonToggleItem(int slot){
		return buttons[slot].getToggleItem();
	}
	/**
	 * Sets the item used when the button is (next) toggled.
	 * @param slot Slot number for the button.
	 * @param type Material used to represent the button.
	 * @param name Name of the button.
	 * @param description List used as the buttons description.
	 */
	public void setButtonToggleItem(int slot, Material type, String name, List<String> description){
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		itemMeta.setLore(description);
		item.setItemMeta(itemMeta);
		buttons[slot].setToggleItem(item);
		buttons[slot].setToggle(true);
	}
	/**
	 * Sets the item used when the button is (next) toggled.
	 * @param slot Slot number for the button.
	 * @param type Material used to represent the button.
	 * @param name Name of the button.
	 */
	public void setButtonToggleItem(int slot, Material type, String name){
		ItemStack item = new ItemStack(type);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(null);
		item.setItemMeta(itemMeta);
		buttons[slot].setToggleItem(item);
	}
	/**
	 * Sets the item used when the button is (next) toggled (for advanced users).
	 * @param item ItemStack used to represent the button.
	 */
	public void setButtonToggleItem(int slot, ItemStack item){
		buttons[slot].setToggleItem(item);
	}
	/**
	 * Sets whether a button is togglable.
	 * @param slot Slot number for the desired button.
	 * @param canToggle Boolean for whether the button can be toggled.
	 */
	public void setButtonToggle(int slot, boolean canToggle){
		buttons[slot].setToggle(canToggle);
	}
	/**
	 * Toggles the given button.
	 * @param slot Slot number for the desired button.
	 */
	public void toggleButton(int slot){
		buttons[slot].toggle();
		refresh();
	}
	/**
	 * Returns the currently implemented version of GUIAPI.
	 * @return A String, representing the current API version being used.
	 */
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
		if (event.getWhoClicked() == player && isOpen == true){
			if (event.getSlot() < inv.getSize()){
				MenuInteractEvent calledEvent = new MenuInteractEvent(event.getSlot(), event.getCurrentItem(), (Player) event.getWhoClicked(), event.getClick(), event.getHotbarButton(), inv, this);
				Bukkit.getServer().getPluginManager().callEvent(calledEvent);
				if (buttonStatus[calledEvent.getSlot()] != false && !calledEvent.isCancelled()){
					if (buttons[calledEvent.getSlot()].canToggle()){
						buttons[calledEvent.getSlot()].toggle();
						refresh();
						}
					}
				}	
			event.setCancelled(true);
			//event.setCurrentItem(null);
			this.closeMenu();
			this.openMenu();
		}
	}
	private void refresh(){
		inv.clear();
		for (int i = 0; i < buttonStatus.length; i++){
			if (buttonStatus[i] != false){
				inv.setItem(buttons[i].getSlot(), buttons[i].getItem());
			}
		}
	}
	private void initInv(Player player, String name, int slotNum){
		if (slotNum % 9 != 0){
			Bukkit.getServer().getLogger().severe("Please tell your local plugin author: 'You must use a multiple of 9 for a slot number!'");
			slotNum = slotNum + (9 - (slotNum % 9));
		}
		boolean buttonStatus2[] = new boolean[slotNum];
		Button buttons2[] = new Button[slotNum];
		for (int i = 0; i < slotNum; i++){
			buttonStatus2[i] = false;
		}
		buttonStatus = buttonStatus2;
		buttons = buttons2;
		inv = Bukkit.createInventory(player, slotNum, name);
	}
	private void addItem(ItemStack item){
		while (buttonStatus[slotCount] != false){
			slotCount++;
		}
		inv.addItem(item);
		buttons[slotCount] = new Button(this, item, slotCount, false, null, false);
		buttonStatus[slotCount] = true;
		slotCount++;
	}
}