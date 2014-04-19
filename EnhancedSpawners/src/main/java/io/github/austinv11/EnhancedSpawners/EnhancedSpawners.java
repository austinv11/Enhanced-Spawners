package io.github.austinv11.EnhancedSpawners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public class EnhancedSpawners extends JavaPlugin{
	String CURRENT_VERSION = "1.0.0"; //TODO remember to update
	FileConfiguration config = getConfig();
	@Override
	public void onEnable(){
		configInit(false);
		if (config.getBoolean("Options.setToDefault") == true){
			configInit(true);
		}
		new RecipeHandler(this);
		getLogger().info("Spawners on this server are now enhanced by EnhancedSpawners v"+CURRENT_VERSION);
	}
	public void configInit(boolean revert){
		if (revert == false){
			config.addDefault("Options.setToDefault", false);
			config.addDefault("Features.changeSpawners", true);
			config.addDefault("Features.silkTouchSpawners", true);
			config.options().copyDefaults(true);
			saveConfig();
		}else{
			config.set("Options.setToDefault", false);
			config.set("Features.changeSpawners", true);
			config.set("Features.silkTouchSpawners", true);
			saveConfig();
		}
	}
	@Override
	public void onDisable(){
		getLogger().info("Baby come back!");
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("set-delay")){
			if (sender.isOp() && args.length == 1){
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
			if (sender.isOp() && args.length == 1){
				Player player = (Player) sender;
				if (player.getTargetBlock(null, 10).getType() == Material.MOB_SPAWNER){
					BlockState state = player.getTargetBlock(null, 10).getState();//FIXME non-deprecated method
					CreatureSpawner spawner = (CreatureSpawner) state;
					spawner.setCreatureTypeByName(args[0].toUpperCase());
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
