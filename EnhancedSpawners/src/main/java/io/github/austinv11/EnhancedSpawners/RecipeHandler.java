package io.github.austinv11.EnhancedSpawners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeHandler implements Listener{
	ItemStack temperedEgg;
	ItemStack infusedEgg;
	ItemStack spawnEgg;
	public RecipeHandler(EnhancedSpawners plugin){//Inits items and events
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		//Tempered Egg TODO make it unthrowable
		List<String> tEggLore = new ArrayList<String>();
		tEggLore.add("It seems hollow...");
		temperedEgg = new ItemStack(Material.EGG);
		ItemMeta eggMeta = temperedEgg.getItemMeta();
		eggMeta.setDisplayName("Tempered Egg");
		eggMeta.setLore(tEggLore);
		temperedEgg.setItemMeta(eggMeta);
		//Infused egg TODO make it unthrowable
		/*List<String> iEggLore = new ArrayList<String>();FIXME
		iEggLore.add("You feel a slight attraction...");*/
		infusedEgg = new ItemStack(Material.EGG);
		infusedEgg.addUnsafeEnchantment(Enchantment.DURABILITY, 1);//TODO custom enchant
		ItemMeta iEggMeta = infusedEgg.getItemMeta();
		iEggMeta.setDisplayName("Infused Egg");
		//eggMeta.setLore(iEggLore);
		infusedEgg.setItemMeta(iEggMeta);
		//Init recipes
		initRecipes();
	}
	public void initRecipes(){
		Bukkit.getServer().addRecipe(new FurnaceRecipe(temperedEgg, Material.EGG));
		Bukkit.getServer().addRecipe(new FurnaceRecipe(temperedEgg, Material.MONSTER_EGG));
		
		ShapedRecipe iEgg = new ShapedRecipe(infusedEgg);
		iEgg.shape("GCG", "DED", "GBG");
		iEgg.setIngredient('G', Material.GOLD_INGOT);
		iEgg.setIngredient('E', Material.EGG);
		iEgg.setIngredient('C', Material.EMERALD);
		iEgg.setIngredient('B', Material.ENCHANTED_BOOK);
		iEgg.setIngredient('D', Material.DIAMOND);
		Bukkit.getServer().addRecipe(iEgg);
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onCraft(CraftItemEvent event){
		CraftingInventory inv = event.getInventory();
		if (inv.containsAtLeast(temperedEgg, 1)) {
			event.setCancelled(false);
		}else{
			event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityInteract(PlayerInteractEntityEvent event){
		if (event.getPlayer().getItemInHand() == infusedEgg){
			event.setCancelled(true);
			List<String> spawnLore = new ArrayList<String>();
			spawnLore.add("This egg contains the spirit of a "+event.getRightClicked().toString());
			spawnEgg = new ItemStack(Material.MONSTER_EGG);
			ItemMeta spawnMeta = spawnEgg.getItemMeta();
			spawnMeta.setDisplayName("Attuned Egg ("+event.getRightClicked().toString()+")");
			spawnMeta.setLore(spawnLore);
			spawnEgg.setItemMeta(spawnMeta);
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onInteract(PlayerInteractEvent event){
		if (event.getPlayer().getItemInHand() == infusedEgg || event.getPlayer().getItemInHand() == temperedEgg || event.getPlayer().getItemInHand() == spawnEgg){
			event.setCancelled(true);
		}
	}
}
