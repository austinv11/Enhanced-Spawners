package io.github.austinv11.EnhancedSpawners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

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
}