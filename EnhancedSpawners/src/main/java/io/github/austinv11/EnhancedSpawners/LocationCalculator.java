package io.github.austinv11.EnhancedSpawners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

//Yes, this code is taken from my pickaxetweak plugin
public class LocationCalculator {
	public Location getLoc(BlockFace bf, Location loc){
		if (bf == BlockFace.DOWN){
			Location newLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
			return newLoc;
		}else if (bf == BlockFace.EAST){
			Location newLoc = new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ());
			return newLoc;
		}else if (bf == BlockFace.NORTH){
			Location newLoc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1);
			return newLoc;
		}else if (bf == BlockFace.SOUTH){
			Location newLoc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1);
			return newLoc;
		}else if (bf == BlockFace.UP){
			Location newLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
			return newLoc;
		}else if (bf == BlockFace.WEST){
			Location newLoc = new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ());
			return newLoc;
		}else{
			Bukkit.getServer().getLogger().severe("ERROR: UNKNOWN BLOCKFACE");
			return null;
		}
	}
	public Location spawnerSearch(Location loc){
		for (double i = 0; i <= 4; i++){ //Y
			for (double j = 0; j <= 10; j++){ //Z
				for (double k = 0; k <= 10; k++){ //X
					double x, y, z;
					x = loc.getX();
					y = loc.getY();
					z = loc.getZ();
					Location tempLoc = loc.clone();
					tempLoc.setX(x + k);
					tempLoc.setY(y + i);
					tempLoc.setZ(z + j);
					if (tempLoc.getBlock().getType() == Material.MOB_SPAWNER){
						return tempLoc;
					}
				}
			}
		}
		return null;
	}
}