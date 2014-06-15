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
	private boolean cancelled;
	private int slot,numberKey;
	private ItemStack buttonItem;
	private Player player;
	private ClickType clickType;
	private Inventory menuInv;
	private Menu menu;
	public MenuEvent(int slot2, ItemStack buttonItem2, Player invPlayer, ClickType click, int numKey, Inventory menuInventory, Menu menu2){
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
	public int getSlot(){//Returns the slot clicked on or -1 if cancelled
		if (!cancelled){
			return slot;
		}
		return -1;
	}
	public String getButtonName(){//Returns the button name clicked on or null if cancelled
		if (!cancelled && buttonItem.hasItemMeta()){
			if (buttonItem.getItemMeta().hasDisplayName()){
				return buttonItem.getItemMeta().getDisplayName();
			}
		}
		return null;
	}
	public ItemStack getButtonItem(){//Returns the item clicked on or null if cancelled
		if (!cancelled){
			return buttonItem;
		}
		return null;
	}
	public List<String> getButtonDescription(){//Returns the given description to the clicked button or null if cancelled
		if (!cancelled && buttonItem.hasItemMeta()){
			if (buttonItem.getItemMeta().hasLore()){
				return buttonItem.getItemMeta().getLore();
			}
		}
		return null;
	}
	public Player getPlayer(){//Returns the player that interacted with the menu or null if cancelled
		if (!cancelled){
			return player;
		}
		return null;
	}
	public ClickType getClickType(){//Returns method that the player activated the button or null if cancelled
		if (!cancelled){
			return clickType;
		}
		return null;
	}
	public int getHotbarButton(){//If ClickType is NUMBER_KEY, it returns the key pressed (0-8), otherwise -1
		if (!cancelled){
			return numberKey;
		}
		return -1;
	}
	public Inventory getMenuInventory(){//Returns the menu's inventory or null if cancelled
		if (!cancelled){
			return menuInv;
		}
		return null;
	}
	public String getMenuName(){//Returns menu's name or null if cancelled
		if (!cancelled){
			return menuInv.getTitle();
		}
		return null;
	}
	public Menu getMenu(){//Returns the menu object associated with this event or null if cancelled
		if (!cancelled){
			return menu;
		}
		return null;
	}
	public boolean isCancelled(){//Returns whether the event is currently cancelled
		return cancelled;
	}
	public void setCancelled(boolean cancel){//If cancelled, any returns made is set to null and it doesn't toggle the button
		cancelled = cancel;
	}
}