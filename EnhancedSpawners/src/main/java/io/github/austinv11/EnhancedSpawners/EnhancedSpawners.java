package io.github.austinv11.EnhancedSpawners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class EnhancedSpawners extends JavaPlugin{
	
	@Override
	public void onEnable(){
		//TODO
	}
	@Override
	public void onDisable(){
		//TODO
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("TODO")) {
			
			return true;
		}
		return false;
	}
}
