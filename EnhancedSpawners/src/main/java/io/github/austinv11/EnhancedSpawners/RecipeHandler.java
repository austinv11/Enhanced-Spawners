package io.github.austinv11.EnhancedSpawners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeHandler implements Listener{
	ItemStack temperedEgg;
	ItemStack infusedEgg;
	ItemStack spawnEgg;
	ItemStack spawner;
	ItemStack mysteryEgg;
	EnhancedSpawners plugin;
	LocationCalculator lC;
	MobProperties mobs;
	public RecipeHandler(EnhancedSpawners pluginN){//Inits items and events
		pluginN.getServer().getPluginManager().registerEvents(this, pluginN);
		plugin = pluginN;
		lC = new LocationCalculator();
		mobs = new MobProperties(pluginN);
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
		List<String> mysteryLore = new ArrayList<String>();
		mysteryLore.add("...It's a mystery");
		mysteryEgg = new ItemStack(Material.MONSTER_EGG);
		ItemMeta mysteryMeta = mysteryEgg.getItemMeta();
		mysteryMeta.setDisplayName("Attuned Egg (Mystery)");
		mysteryMeta.setLore(mysteryLore);
		mysteryEgg.setItemMeta(mysteryMeta);
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
	public void spawnRandom(Location loc){
		EntityType[] entityList = null;
		int j = 0;
		for (int i = 0; i < EntityType.values().length; i++){
			if (!mobs.mysteryBlacklist(EntityType.values()[i])){
				entityList[j] = EntityType.values()[i];
				j++;
			}
		}
		Random r = new Random();
		int randEntity = r.nextInt(entityList.length);
		loc.getWorld().spawnEntity(loc, entityList[randEntity]);
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
		if (plugin.getConfig().getBoolean("Features.silkTouchSpawners") == true && event.getPlayer().hasPermission("EnhancedSpawners.silkySpawners")){
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
		if (event.getPlayer().getItemInHand() != null && event.getClickedBlock() != null && event.getPlayer().getItemInHand().hasItemMeta()){
			if (plugin.getConfig().getBoolean("Features.changeSpawners") == true && event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()){
				if (event.getClickedBlock().getType() == Material.MOB_SPAWNER && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Attuned Egg (") && event.getPlayer().getItemInHand().getType() == Material.MONSTER_EGG){
					Player player = event.getPlayer();
					BlockState state = event.getClickedBlock().getState();
					CreatureSpawner spawner = (CreatureSpawner) state;
					String mobName = player.getItemInHand().getItemMeta().getDisplayName().substring(13, player.getItemInHand().getItemMeta().getDisplayName().length()).replace(")", "");
					if (mobs.getAlias(mobName) == null){
						spawner.setCreatureTypeByName(mobName);
					}else{
						spawner.setSpawnedType(mobs.getAlias(mobName));
					}
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
					return;
				}
			}
			if (plugin.getConfig().getBoolean("Features.attunedEggsEqualSpawnEggs") == true && event.getPlayer().getItemInHand().getItemMeta().hasDisplayName() && event.getClickedBlock().getType() != Material.MOB_SPAWNER){
				if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Attuned Egg (") && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getItemInHand().getType() == Material.MONSTER_EGG && !event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Attuned Egg (Mystery)")){
					Player player = event.getPlayer();
					String mobName = player.getItemInHand().getItemMeta().getDisplayName().substring(13, player.getItemInHand().getItemMeta().getDisplayName().length()).replace(")", "");
					Location eLoc = event.getClickedBlock().getLocation().clone();
					Location mobLoc = lC.getLoc(event.getBlockFace(), eLoc);
					int amount = player.getItemInHand().getAmount();
					if (mobLoc != null){
						mobLoc.getWorld().spawnEntity(mobLoc, EntityType.valueOf(mobName.toUpperCase()));
						mobLoc.getWorld().playSound(mobLoc, Sound.ENDERMAN_TELEPORT, 10, 1);//TODO change sound
						mobLoc.getWorld().playEffect(mobLoc, Effect.ENDER_SIGNAL, 0);
						if (amount == 1){
							ItemStack clear = new ItemStack (Material.AIR);
							player.setItemInHand(clear);
						}else{
							player.getItemInHand().setAmount(amount-1);
						}
					}
				}
			}
			if (event.getPlayer().getItemInHand().getItemMeta().hasDisplayName() && event.getClickedBlock().getType() != Material.MOB_SPAWNER){
				if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("Attuned Egg (") && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getItemInHand().getType() == Material.MONSTER_EGG && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Attuned Egg (Mystery)")){
					Player player = event.getPlayer();
					Location eLoc = event.getClickedBlock().getLocation().clone();
					Location mobLoc = lC.getLoc(event.getBlockFace(), eLoc);
					int amount = player.getItemInHand().getAmount();
					if (mobLoc != null){
						spawnRandom(mobLoc);
						mobLoc.getWorld().playSound(mobLoc, Sound.ENDERMAN_TELEPORT, 10, 1);//TODO change sound
						mobLoc.getWorld().playEffect(mobLoc, Effect.ENDER_SIGNAL, 0);
						if (amount == 1){
							ItemStack clear = new ItemStack (Material.AIR);
							player.setItemInHand(clear);
						}else{
							player.getItemInHand().setAmount(amount-1);
						}
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageByEntityEvent event){
		if (plugin.getConfig().getBoolean("Features.changeSpawners") == true && !mobs.checkBlacklist(event.getEntityType().toString())){
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
	@EventHandler(priority = EventPriority.HIGH)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if (plugin.getConfig().getBoolean("Features.redstoneToggle(EXP)") == true){
			if (event.getSpawnReason() == SpawnReason.SPAWNER){
				Location spawnerLoc = lC.spawnerSearch(event.getEntity().getLocation().clone());
				if (spawnerLoc != null){
					//plugin.getLogger().info("Spawner found!");
					int rsPower = spawnerLoc.getBlock().getBlockPower();
					if (rsPower != 0){
						event.setCancelled(true);
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onGeneration(ChunkPopulateEvent event){
		if (plugin.getConfig().getBoolean("Features.dungeonLoot")){
			BlockState[] tileEnts = event.getChunk().getTileEntities();
			for (BlockState state : tileEnts) {
				if (state.getType() != Material.CHEST){
					continue;
				}else{
					Chest chest = (Chest) state;
					Inventory cInv = chest.getInventory();
					double lootPasses = Math.random();
					boolean cont = false;
					if (lootPasses <= 0.35){//35% of any loot
						cont = true;
					}
					if (cont){
						lootPasses = Math.random();
						int quantity;
						if (lootPasses <= 0.75){//75% for 1
							quantity = 1;
						}else if (lootPasses <= 0.95){//20% for 2
							quantity = 2;
						}else{//5% for 3
							quantity = 3;
						}
						lootPasses = Math.random();
						int lootType;
						if (lootPasses <= 0.65){//65% for tempered egg
							lootType = 1;
						}else{//35% for mystery egg
							lootType = 2;
						}
						if (lootType == 1){
							for (int i = 0; i < quantity; i++){
								cInv.addItem(temperedEgg);
							}
						}else{
							for (int i = 0; i < quantity; i++){
								cInv.addItem(mysteryEgg);
							}
						}
					}
				}
			}
		}
	}
}
