package io.github.austinv11.EnhancedSpawners;

import io.github.austinv11.InternalGUIAPI.Menu;

import java.io.File;
import java.io.IOException;

import net.gravitydevelopment.updater.Updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnhancedSpawners extends JavaPlugin implements Listener{
	String CURRENT_VERSION = this.getDescription().getVersion();
	String CURRENT_GAME_VERSION = Bukkit.getBukkitVersion().substring(0, 3);
	FileConfiguration config = getConfig();
	FileHandler fileHandler;
	File loginLogger = new File(getDataFolder(), "Data//loginTracker.yml");
	int id = 78473;
	MobProperties mobs;
	LocationCalculator locCalc;
	@Override
	public void onEnable(){
		configInit(false);
		if (config.getBoolean("Options.setToDefault") == true){
			configInit(true);
		}
		if (config.getBoolean("Options.autoUpdater") == true){
			Updater updater = new Updater(this, id, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
			if (updater.getLatestGameVersion().contains(CURRENT_GAME_VERSION)){
				Updater updaterAuto = new Updater(this, id, this.getFile(), Updater.UpdateType.DEFAULT, true);
			}
		}
		new RecipeHandler(this);
		new InventoryGlitchTweaker(this);
		if (config.getBoolean("Options.mcstatsDataCollection") == true){
			try {
			    MetricsLite metrics = new MetricsLite(this);
			    metrics.start();
			} catch (IOException e) {
			    getLogger().severe("Failed to connect to mcstats.org");
			}
		}
		fileHandler = new FileHandler(config);
		mobs = new MobProperties(this);
		locCalc = new LocationCalculator();
		this.getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Implementing internal version v"+Menu.getAPIVersion()+" of austinv11's GUIAPI");
		getLogger().info("Spawners on this server are now enhanced by EnhancedSpawners v"+CURRENT_VERSION);
	}
	public void configInit(boolean revert){
		if (revert == false){
			config.addDefault("Options.autoUpdater", true);
			config.addDefault("Options.mcstatsDataCollection", true);
			config.addDefault("Options.mobBlacklist", true);
			config.addDefault("Options.compassCleaner", true);
			config.addDefault("Options.spawnerFinderRadius", 50);
			config.addDefault("Options.firstLoginMessage", true);
			config.addDefault("Options.setToDefault", false);
			config.addDefault("Features.changeSpawners", true);
			config.addDefault("Features.silkTouchSpawners", true);
			config.addDefault("Features.attunedEggsEqualSpawnEggs", true);
			config.addDefault("Features.dungeonLoot", true);
			config.addDefault("Features.redstoneToggle(EXP)", false);
			config.options().copyDefaults(true);
			saveConfig();
		}else{
			config.set("Options.autoUpdater", true);
			config.set("Options.mcstatsDataCollection", true);
			config.set("Options.mobBlacklist", true);
			config.set("Options.compassCleaner", true);
			config.set("Options.spawnerFinderRadius", 50);
			config.set("Options.firstLoginMessage", true);
			config.set("Options.setToDefault", false);
			config.set("Features.changeSpawners", true);
			config.set("Features.silkTouchSpawners", true);
			config.set("Features.attunedEggsEqualSpawnEggs", true);
			config.set("Features.dungeonLoot", true);
			config.set("Features.redstoneToggle(EXP)", false);
			saveConfig();
		}
	}
	@Override
	public void onDisable(){
		getLogger().info("Baby come back!");
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent event){
		//Setting compasses to default
		if (config.getBoolean("Options.compassCleaner")){
			event.getPlayer().setCompassTarget(event.getPlayer().getLocation().getWorld().getSpawnLocation());
		}
		if (config.getBoolean("Options.firstLoginMessage")){
			boolean result = fileHandler.getBoolean(loginLogger, "firstTime."+event.getPlayer().getName());
			if (result != true){
				fileHandler.set(loginLogger, "firstTime."+event.getPlayer().getName(), true);
				event.getPlayer().sendMessage("Welcome to the server!") ;
				event.getPlayer().sendMessage("This server has"+ChatColor.GOLD+" EnhancedSpawners"+ChatColor.RESET+" installed! Visit "+ChatColor.BLUE+"http://bit.ly/1kN4UZO "+ChatColor.RESET+"for info about it.");
			}
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("set-delay")){
			if (sender.hasPermission("set-delay") && args.length == 1){
				Player player = (Player) sender;
				if (player.getTargetBlock(null, 10).getType() == Material.MOB_SPAWNER){
					BlockState state = player.getTargetBlock(null, 10).getState();//FIXME non-deprecated method
					CreatureSpawner spawner = (CreatureSpawner) state;
					spawner.setDelay(Integer.parseInt(args[0]));
					spawner.update();
				}else{
					sender.sendMessage(ChatColor.RED+"Error: You are not looking at a mob spawner");
				}
				return true;
			}else if (!(sender.hasPermission("set-delay"))){
				sender.sendMessage(ChatColor.RED+"Error: You need to be an OP to perform this command");
				return true;
			}else{
				return false;
			}
		}else if (cmd.getName().equalsIgnoreCase("set-mob")){
			if (sender.hasPermission("set-mob") && args.length >= 1){
				Player player = (Player) sender;
				if (player.getTargetBlock(null, 10).getType() == Material.MOB_SPAWNER){
					BlockState state = player.getTargetBlock(null, 10).getState();//FIXME non-deprecated method
					CreatureSpawner spawner = (CreatureSpawner) state;
					if (mobs.getAlias(args[0]) == null){
						spawner.setCreatureTypeByName(args[0].toUpperCase());
					}else{
						spawner.setSpawnedType(mobs.getAlias(args[0]));
					}
					if (args.length >=2){
						spawner.setDelay(Integer.parseInt(args[1]));
					}
					spawner.update();
				}else{
					sender.sendMessage(ChatColor.RED+"Error: You are not looking at a mob spawner");
				}
				return true;
			}else if (!(sender.hasPermission("set-mob"))){
				sender.sendMessage(ChatColor.RED+"Error: You need to be an OP to perform this command");
				return true;
			}else{
				return false;
			}
		}else if (cmd.getName().equalsIgnoreCase("new-spawner")){//FIXME valid block detection
			if (sender.hasPermission("new-spawner") && args.length > 0){
				if (sender instanceof Player){
					Player player = (Player) sender;
					BlockFace bF = locCalc.getDirection(player);
					Location loc = player.getTargetBlock(null, 10).getLocation().clone();
					Location spawnerLoc = locCalc.getLoc(bF, loc);
					if ((loc.getBlock().getType() != Material.AIR || loc.getBlock().getType() != null) && (spawnerLoc.getBlock().getType() != Material.AIR || spawnerLoc.getBlock().getType() != null)){
						spawnerLoc.getBlock().setType(Material.MOB_SPAWNER);
						BlockState state = spawnerLoc.getBlock().getState();
						CreatureSpawner spawner = (CreatureSpawner) state;
						if (mobs.getAlias(args[0]) == null){
							spawner.setCreatureTypeByName(args[0].toUpperCase());
						}else{
							spawner.setSpawnedType(mobs.getAlias(args[0]));
						}
						if (args.length >=2){
							spawner.setDelay(Integer.parseInt(args[1]));
						}
						spawner.update();
						sender.sendMessage("You have successfully a spawner spawning "+ChatColor.GOLD+args[0].toLowerCase()+"s"+ChatColor.RESET+"!");
					}else{
						sender.sendMessage(ChatColor.RED+"You are not looking at a valid block");
					}
				}else{
					sender.sendMessage(ChatColor.RED+"Error: You are not a player");
				}
				return true;
			}else if (!(sender.hasPermission("new-spawner"))){
				sender.sendMessage(ChatColor.RED+"Error: You need to be an OP to perform this command");
				return true;
			}else{
				return false;
			}
		}else if (cmd.getName().equalsIgnoreCase("give-spawner")){
			if (sender.hasPermission("give-spawner")){
				if (args.length > 1){
					Player player = Bukkit.getServer().getPlayer(args[0]);
					if (player != null){
						ItemStack spawner = new ItemStack(Material.MOB_SPAWNER);
						ItemMeta spawnerMeta = spawner.getItemMeta();
						spawnerMeta.setDisplayName("Mob Spawner ("+args[1]+")");
						spawner.setItemMeta(spawnerMeta);
						player.getInventory().addItem(spawner);
						sender.sendMessage("You have successfully given "+args[0]+" a spawner spawning "+ChatColor.GOLD+args[1].toLowerCase()+"s"+ChatColor.RESET+"!");
						player.sendMessage(sender.getName()+" has given you a spawner spawning "+ChatColor.GOLD+args[1].toLowerCase()+"s"+ChatColor.RESET+"!");
					}else{
						sender.sendMessage(ChatColor.RED+"Error: '"+args[0]+"' is not a valid player");
					}
				}else if (args.length == 1){
					if (sender instanceof Player){
						ItemStack spawner = new ItemStack(Material.MOB_SPAWNER);
						ItemMeta spawnerMeta = spawner.getItemMeta();
						spawnerMeta.setDisplayName("Mob Spawner ("+args[0]+")");
						spawner.setItemMeta(spawnerMeta);
						Player player = (Player) sender;
						player.getInventory().addItem(spawner);
					}else{
						sender.sendMessage(ChatColor.RED+"Error: You are not a player");
					}
				}else if (args.length == 0){
					if (sender instanceof Player){
						Player player = (Player) sender;
						if (player.getTargetBlock(null, 10).getType() == Material.MOB_SPAWNER){
							CreatureSpawner spawnr = (CreatureSpawner) player.getTargetBlock(null, 10).getState();
							String mobName = spawnr.getCreatureTypeName();
							ItemStack spawner = new ItemStack(Material.MOB_SPAWNER);
							ItemMeta spawnerMeta = spawner.getItemMeta();
							spawnerMeta.setDisplayName("Mob Spawner ("+mobName+")");
							spawner.setItemMeta(spawnerMeta);
							player.getInventory().addItem(spawner);
						}else{
							sender.sendMessage(ChatColor.RED+"Error: You are not looking at a spawner");
						}
					}else{
						sender.sendMessage(ChatColor.RED+"Error: You are not a player");
					}
				}
				return true;
			}else if (!(sender.hasPermission("give-spawner"))){
				sender.sendMessage(ChatColor.RED+"Error: You need to be an OP to perform this command");
				return true;
			}else{
				return false;
			}
		}else if (cmd.getName().equalsIgnoreCase("spawner-finder")){
			if (sender.hasPermission("spawner-finder")){
				if (args.length == 0){
					if (sender instanceof Player){
						new RecipeHandler(this).giveSpawnerFinder((Player) sender);
					}else{
						sender.sendMessage(ChatColor.RED+"Error: You are not a player");
					}
				}else if (args.length == 1){
					Player player = Bukkit.getServer().getPlayer(args[0]);
					if (player != null){
						new RecipeHandler(this).giveSpawnerFinder(player);
						sender.sendMessage("You have successfully given "+args[0]+" a Spawner Finder!");
						player.sendMessage(sender.getName()+" has given you a Spawner Finder!");
					}else{
						sender.sendMessage(ChatColor.RED+"Error: '"+args[0]+"' is not a valid player");
					}
				}
				return true;
			}else if (!(sender.hasPermission("spawner-finder"))){
				sender.sendMessage(ChatColor.RED+"Error: You need to be an OP to perform this command");
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
}
