package io.github.austinv11.EnhancedSpawners;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CompassReset extends BukkitRunnable{
	Player player;
	public CompassReset(Player pPlayer){
		player = pPlayer;
	}
	@Override
    public void run(){
		player.setCompassTarget(player.getLocation().getWorld().getSpawnLocation());
		player.sendMessage("Times up!");
		this.cancel();
	}
}
