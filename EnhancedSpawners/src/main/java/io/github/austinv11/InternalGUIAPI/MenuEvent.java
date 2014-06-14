package io.github.austinv11.InternalGUIAPI;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private static boolean cancelled = false;
	private static int slot,numberKey;
	private static ItemStack buttonItem;
	private static Player player;
	private static ClickType clickType;
	private static Inventory menuInv;
	//private static Menu menu;
	public MenuEvent(int slot, ItemStack buttonItem, Player invPlayer, ClickType click, int numKey, Inventory menuInventory){
		MenuEvent.slot = slot;
		MenuEvent.buttonItem = buttonItem;
		player = invPlayer;
		clickType = click;
		numberKey = numKey;
		menuInv = menuInventory;
		//MenuEvent.menu = menu;
	}
	public HandlerList getHandlers(){
	    return handlers;
	}
	public static HandlerList getHandlerList(){
	    return handlers;
	}
	public static int getSlot(){//Returns the slot clicked on or -1 if cancelled
		if (!cancelled){
			return slot;
		}
		return -1;
	}
	public static String getButtonName(){//Returns the button name clicked on or null if cancelled
		if (!cancelled){
			return buttonItem.getItemMeta().getDisplayName();
		}
		return null;
	}
	public static ItemStack getButtonItem(){//Returns the item clicked on or null if cancelled
		if (!cancelled){
			return buttonItem;
		}
		return null;
	}
	public static List<String> getButtonDescription(){//Returns the given description to the clicked button or null if cancelled
		if (!cancelled){
			return buttonItem.getItemMeta().getLore();
		}
		return null;
	}
	public static Player getPlayer(){//Returns the player that interacted with the menu or null if cancelled
		if (!cancelled){
			return player;
		}
		return null;
	}
	public static ClickType getClickType(){//Returns method that the player activated the button or null if cancelled
		if (!cancelled){
			return clickType;
		}
		return null;
	}
	public static int getHotbarButton(){//If ClickType is NUMBER_KEY, it returns the key pressed (0-8), otherwise -1
		if (!cancelled){
			return numberKey;
		}
		return -1;
	}
	public static Inventory getMenuInventory(){//Returns the menu's inventory or null if cancelled
		if (!cancelled){
			return menuInv;
		}
		return null;
	}
	public static String getMenuName(){//Returns menu's name or null if cancelled
		if (!cancelled){
			return menuInv.getTitle();
		}
		return null;
	}
	/*FIXME public static Menu getMenu(){//Returns the menu object associated with this event or null if cancelled
		if (!cancelled){
			return menu;
		}
		return null;
	}*/
	public static boolean isCancelled(){//Returns whether the event is currently cancelled
		return cancelled;
	}
	public static void setCancelled(boolean cancel){//If cancelled, any returns made is set to null
		cancelled = cancel;
	}
}