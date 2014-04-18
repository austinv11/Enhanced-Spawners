package io.github.austinv11.EnhancedSpawners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EnhancedSpawners extends JavaPlugin{
	String CURRENT_VERSION = "1.0.0"; //TODO remember to update
	FileConfiguration config = getConfig();
	String[] entities = {"hello", "world"}; //TODO update
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
			config.options().copyDefaults(true);
			saveConfig();
		}else{
			config.set("Options.setToDefault", false);
			saveConfig();
		}
	}
	@Override
	public void onDisable(){
		getLogger().info("Baby come back!");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("spawner-list")) {
			String msg = "";
			for (int i = 0; i < entities.length; i++){
				msg = msg+entities[i];
			}
			sender.sendMessage(msg);
			return true;
		}
		return false;
	}
}
