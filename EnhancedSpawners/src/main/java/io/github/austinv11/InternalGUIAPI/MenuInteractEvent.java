package io.github.austinv11.InternalGUIAPI;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
/**
 * <b>
 * MenuInteractEvent Object:
 * </b>
 * Activated when a created menu is interacted in
 *
 */
public class MenuInteractEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private int slot,numberKey;
	private ItemStack buttonItem;
	private Player player;
	private ClickType clickType;
	private Inventory menuInv;
	private Menu menu;
	public MenuInteractEvent(int slot2, ItemStack buttonItem2, Player invPlayer, ClickType click, int numKey, Inventory menuInventory, Menu menu2){
		slot = slot2;
		buttonItem = buttonItem2;
		player = invPlayer;
		clickType = click;
		numberKey = numKey;
		menuInv = menuInventory;
		cancelled = false;
		menu = menu2;
	}
	public HandlerList getHandlers(){
	    return handlers;
	}
	public static HandlerList getHandlerList(){
	    return handlers;
	}
	/**
	 * Returns the slot clicked on or -1 if cancelled.
	 * @return The slot number clicked on, -1 if cancelled.
	 */
	public int getSlot(){
		if (!cancelled){
			return slot;
		}
		return -1;
	}
	/**
	 * Returns the button name clicked on or null if cancelled.
	 * @return The button name, null if cancelled.
	 */
	public String getButtonName(){
		if (!cancelled && buttonItem.hasItemMeta()){
			if (buttonItem.getItemMeta().hasDisplayName()){
				return buttonItem.getItemMeta().getDisplayName();
			}
		}
		return null;
	}
	/**
	 * Returns the item clicked on or null if cancelled.
	 * @return The ItemStack clicked on, null if cancelled.
	 */
	public ItemStack getButtonItem(){
		if (!cancelled){
			return buttonItem;
		}
		return null;
	}
	/**
	 * Returns the given description to the clicked button or null if cancelled.
	 * @return The given description to the clicked button, null if empty or cancelled.
	 */
	public List<String> getButtonDescription(){
		if (!cancelled && buttonItem.hasItemMeta()){
			if (buttonItem.getItemMeta().hasLore()){
				return buttonItem.getItemMeta().getLore();
			}
		}
		return null;
	}
	/**
	 * Returns the player that interacted with the menu or null if cancelled.
	 * @return The player that interacted with the menu, null if cancelled.
	 */
	public Player getPlayer(){
		if (!cancelled){
			return player;
		}
		return null;
	}
	/**
	 * Returns method that the player activated the button or null if cancelled.
	 * @return ClickType used to interact with the menu, null if cancelled.
	 */
	public ClickType getClickType(){
		if (!cancelled){
			return clickType;
		}
		return null;
	}
	/**
	 * If ClickType is NUMBER_KEY, it returns the key pressed (0-8), otherwise -1.
	 * @return The key pressed (0-8), -1 if event is cancelled or if ClickType is not NUMBER_KEY.
	 */
	public int getHotbarButton(){
		if (!cancelled){
			return numberKey;
		}
		return -1;
	}
	/**
	 * Returns the menu's inventory or null if cancelled.
	 * @return A copy of the object associated with the menu's inventory, null if cancelled.
	 */
	public Inventory getMenuInventory(){
		if (!cancelled){
			return menuInv;
		}
		return null;
	}
	/**
	 * Returns menu's name or null if cancelled.
	 * @return Menu's name, null if cancelled.
	 */
	public String getMenuName(){
		if (!cancelled){
			return menuInv.getTitle();
		}
		return null;
	}
	/**
	 * Returns the menu object associated with this event or null if cancelled.
	 * @return The menu object associated with this event, null if cancelled.
	 */
	public Menu getMenu(){
		if (!cancelled){
			return menu;
		}
		return null;
	}
	/**
	 * Returns whether the event is currently cancelled.
	 * @return Boolean, whether the event is cancelled.
	 */
	public boolean isCancelled(){
		return cancelled;
	}
	/**
	 * If cancelled, any returns made is set to null and it doesn't toggle the button.
	 * @param cancel Boolean, whether the event should be cancelled.
	 */
	public void setCancelled(boolean cancel){
		cancelled = cancel;
	}
}