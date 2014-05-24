package io.github.austinv11.EnhancedSpawners;

import org.bukkit.entity.EntityType;

public class MobProperties {
	EnhancedSpawners plugin;
	EntityType[] mBlacklist;
	public MobProperties(EnhancedSpawners eS) {
		plugin = eS;
		EntityType[] tempBlacklist = {EntityType.COMPLEX_PART, EntityType.DROPPED_ITEM, EntityType.ENDER_CRYSTAL, EntityType.ENDER_DRAGON, EntityType.ENDER_PEARL, EntityType.ENDER_SIGNAL, EntityType.FALLING_BLOCK, EntityType.FIREBALL, EntityType.FIREWORK, EntityType.FISHING_HOOK, EntityType.GIANT, EntityType.ITEM_FRAME, EntityType.LEASH_HITCH, EntityType.LIGHTNING, EntityType.MINECART_COMMAND, EntityType.MINECART_MOB_SPAWNER, EntityType.PAINTING, EntityType.PLAYER, EntityType.PRIMED_TNT, EntityType.SMALL_FIREBALL, EntityType.SPLASH_POTION, EntityType.UNKNOWN, EntityType.WEATHER, EntityType.WITHER, EntityType.WITHER_SKULL};
		mBlacklist = tempBlacklist;
	}
	public boolean checkBlacklist(String mobName){
		if (plugin.getConfig().getBoolean("Options.mobBlacklist")){
			if (mobName.equalsIgnoreCase("ENDER_DRAGON") || mobName.equalsIgnoreCase("IRON_GOLEM") || mobName.equalsIgnoreCase("WITHER") || mobName.equalsIgnoreCase("SNOWMAN") || mobName.equalsIgnoreCase("PIG_ZOMBIE")){
				return true;
			}
		}
		return false;
	}
	public EntityType getAlias(String mobName){
		if (mobName.equalsIgnoreCase("ENDER_DRAGON")){
			return EntityType.ENDER_DRAGON;
		}else if (mobName.equalsIgnoreCase("CAVE_SPIDER")){
			return EntityType.CAVE_SPIDER;
		}else if (mobName.equalsIgnoreCase("OCELOT")){
			return EntityType.OCELOT;
		}else if (mobName.equalsIgnoreCase("MUSHROOM_COW")){
			return EntityType.MUSHROOM_COW;
		}else if (mobName.equalsIgnoreCase("HORSE")){
			return EntityType.HORSE;
		}else if (mobName.equalsIgnoreCase("IRON_GOLEM")){
			return EntityType.IRON_GOLEM;
		}else if (mobName.equalsIgnoreCase("WITHER")){
			return EntityType.WITHER;
		}else if (mobName.equalsIgnoreCase("PIG_ZOMBIE")){
			return EntityType.PIG_ZOMBIE;
		}else if (mobName.equalsIgnoreCase("MAGMA_CUBE")){
			return EntityType.MAGMA_CUBE;
		}else if (mobName.equalsIgnoreCase("SNOWMAN")){
			return EntityType.SNOWMAN;
		}
		return null;
	}
	public boolean mysteryBlacklist(EntityType mob){
		for (int i = 0; i < mBlacklist.length; i++){
			if (mob == mBlacklist[i]){
				return true;
			}
		}
		return false;
	}
	public int getMysteryBlacklistLength(){
		return mBlacklist.length;
	}
}
