package io.github.austinv11.EnhancedSpawners;

import org.bukkit.Effect;
import org.bukkit.Sound;
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
		player.getLocation().clone().getWorld().playEffect(player.getLocation().clone(), Effect.CLICK1, 0);
        player.getLocation().clone().getWorld().playSound(player.getLocation().clone(), Sound.LEVEL_UP, 10, 1);
		this.cancel();
	}
}
