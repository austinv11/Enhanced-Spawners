package io.github.austinv11.EnhancedSpawners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeHandler implements Listener{
	ItemStack temperedEgg;
	ItemStack infusedEgg;
	ItemStack spawnEgg;
	ItemStack spawner;
	EnhancedSpawners plugin;
	public RecipeHandler(EnhancedSpawners pluginN){//Inits items and events
		pluginN.getServer().getPluginManager().registerEvents(this, pluginN);
		plugin = pluginN;
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
		if (plugin.getConfig().getBoolean("Features.changeSpawners") == true){
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
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		if (event.getBlockPlaced().getType() == Material.MOB_SPAWNER && event.getItemInHand().getItemMeta().getDisplayName().contains("Mob Spawner (")){
			String mobName = event.getItemInHand().getItemMeta().getDisplayName().substring(13, event.getItemInHand().getItemMeta().getDisplayName().length()).replace(")", "");
			BlockState state = event.getBlockPlaced().getState();
			CreatureSpawner spawner = (CreatureSpawner) state;
			spawner.setCreatureTypeByName(mobName.toUpperCase());
			spawner.update();
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if (plugin.getConfig().getBoolean("Features.silkTouchSpawners") == true){
			if (event.getBlock().getType() == Material.MOB_SPAWNER){
				Location loc = event.getBlock().getLocation().clone();
				Player player = event.getPlayer();
				if (player.getItemInHand().getEnchantmentLevel(Enchantment.SILK_TOUCH) != 0){
					event.setExpToDrop(0);
					CreatureSpawner spawnr = (CreatureSpawner) event.getBlock().getState();
					String mobName = spawnr.getCreatureTypeName();
					spawner = new ItemStack(Material.MOB_SPAWNER);
					ItemMeta spawnerMeta = spawner.getItemMeta();
					spawnerMeta.setDisplayName("Mob Spawner ("+mobName+")");
					spawner.setItemMeta(spawnerMeta);
					loc.getWorld().dropItemNaturally(loc, spawner);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onCraft(CraftItemEvent event){
		if (plugin.getConfig().getBoolean("Features.changeSpawners") == true){
			CraftingInventory inv = event.getInventory();
			if (inv.containsAtLeast(temperedEgg, 1)) {
				event.setCancelled(false);
			}else if (inv.contains(Material.EGG, 1) && inv.contains(Material.GOLD_INGOT, 4) && inv.contains(Material.DIAMOND, 2) && inv.contains(Material.ENCHANTED_BOOK, 1) && inv.contains(Material.EMERALD, 1)){
				event.setCancelled(true);
			}else{
				event.setCancelled(false);
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event){
		if (plugin.getConfig().getBoolean("Features.changeSpawners") == true){
			if (event.getClickedBlock().getType() == Material.MOB_SPAWNER && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Attuned Egg (")){
				Player player = event.getPlayer();
				BlockState state = event.getClickedBlock().getState();
				CreatureSpawner spawner = (CreatureSpawner) state;
				String mobName = player.getItemInHand().getItemMeta().getDisplayName().substring(13, player.getItemInHand().getItemMeta().getDisplayName().length()).replace(")", "");
				spawner.setCreatureTypeByName(mobName);
				spawner.update();
				Location eLoc = event.getClickedBlock().getLocation().clone();
				eLoc.setY(event.getClickedBlock().getLocation().getY() + 1);
				event.getClickedBlock().getWorld().playSound(eLoc, Sound.ANVIL_LAND, 10, 1);//TODO change sound
				event.getClickedBlock().getWorld().playEffect(eLoc, Effect.ENDER_SIGNAL, 0);
				int amount = player.getItemInHand().getAmount();
				if (amount == 1){
					ItemStack clear = new ItemStack (Material.AIR);
					player.setItemInHand(clear);
				}else{
					player.getItemInHand().setAmount(amount-1);
				}
				player.sendMessage("You have successfully set this spawner to spawn "+ChatColor.GOLD+mobName.toLowerCase()+"s"+ChatColor.RESET+"!");
			}else{
				//NOTHING
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageByEntityEvent event){
		if (plugin.getConfig().getBoolean("Features.changeSpawners") == true){
			if (event.getDamager().getType() == EntityType.PLAYER){
				Player player = (Player) event.getDamager();
				if (player.getItemInHand().getEnchantmentLevel(Enchantment.DURABILITY) == 1 && player.getItemInHand().getItemMeta().getDisplayName().contains("Infused Egg")){
					List<String> spawnLore = new ArrayList<String>();
					spawnLore.add("This egg contains the spirit of a "+event.getEntityType().toString());
					spawnEgg = new ItemStack(Material.MONSTER_EGG);
					ItemMeta spawnMeta = spawnEgg.getItemMeta();
					spawnMeta.setDisplayName("Attuned Egg ("+event.getEntityType().toString()+")");
					spawnMeta.setLore(spawnLore);
					spawnEgg.setItemMeta(spawnMeta);
					int amount = player.getItemInHand().getAmount();
					if (amount == 1){
						player.setItemInHand(spawnEgg);
					}else{
						player.getItemInHand().setAmount(amount-1);
						player.getInventory().addItem(spawnEgg);
					}
					Location eLoc = event.getEntity().getLocation().clone();
					event.getEntity().getWorld().playEffect(eLoc, Effect.ENDER_SIGNAL, 0);
					event.getEntity().remove();
					event.getEntity().getWorld().playSound(eLoc, Sound.ENDERMAN_TELEPORT, 10, 1);//TODO change sound
					player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 0);
				}
			}
		}
	}
}
