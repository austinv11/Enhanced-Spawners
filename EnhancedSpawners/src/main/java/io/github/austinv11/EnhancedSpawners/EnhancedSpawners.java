package io.github.austinv11.EnhancedSpawners;

import java.io.File;
import java.io.IOException;

import net.gravitydevelopment.updater.Updater;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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

public class EnhancedSpawners extends JavaPlugin implements Listener{
	String CURRENT_VERSION = "1.2.0"; //TODO remember to update
	String CURRENT_GAME_VERSION = "CB 1.7.2-R0.3";
	FileConfiguration config = getConfig();
	FileHandler fileHandler;
	File loginLogger = new File(getDataFolder(), "Data//loginTracker.yml");
	int id = 78473;
	MobProperties mobs;
	@Override
	public void onEnable(){
		configInit(false);
		if (config.getBoolean("Options.setToDefault") == true){
			configInit(true);
		}
		if (config.getBoolean("Options.autoUpdater") == true){
			Updater updater = new Updater(this, id, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
			if (updater.getLatestGameVersion() == CURRENT_GAME_VERSION){
				Updater updaterAuto = new Updater(this, id, this.getFile(), Updater.UpdateType.DEFAULT, true);
			}
		}
		new RecipeHandler(this);
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
		this.getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Spawners on this server are now enhanced by EnhancedSpawners v"+CURRENT_VERSION);
	}
	public void configInit(boolean revert){
		if (revert == false){
			config.addDefault("Options.autoUpdater", true);
			config.addDefault("Options.mcstatsDataCollection", true);
			config.addDefault("Options.mobBlacklist", true);
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
		boolean result = fileHandler.getBoolean(loginLogger, "firstTime."+event.getPlayer().getName());
		if (result != true){
			fileHandler.set(loginLogger, "firstTime."+event.getPlayer().getName(), true);
			event.getPlayer().sendMessage("Welcome to the server! This server has "+ChatColor.GOLD+"EnhancedSpawners "+ChatColor.RESET+"installed! Visit "+ChatColor.BLUE+"http://bit.ly/1kN4UZO"+ChatColor.RESET+" for info about it.");
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
			}else if (!(sender.isOp())){
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
			}else if (!(sender.isOp())){
				sender.sendMessage(ChatColor.RED+"Error: You need to be an OP to perform this command");
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
}
