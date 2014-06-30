package io.github.austinv11.EnhancedSpawners;

import io.github.austinv11.InternalGUIAPI.Menu;
import io.github.austinv11.InternalGUIAPI.MenuInteractEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class MobSpawnerGUI implements Listener{
	private static Plugin plugin = Bukkit.getPluginManager().getPlugin("EnhancedSpawners");
	private Block block;
	private Player player;
	private Menu menu;
	private MobProperties mobs = new MobProperties((EnhancedSpawners) plugin);
	private int entities[] = {50,51,52,54,55,56,/*TODO Update, guardian 68,*/57,58,59,60,61,62,65,66,90,91,92,93,94,95,96,98,120,100/*,TODO update, endermite 67*/};//http://minecraft.gamepedia.com/Spawn_Egg
	public MobSpawnerGUI (Block block2, Player player2){
		Bukkit.getPluginManager().registerEvents(this, plugin);
		block = block2;
		player = player2;
		menu = new Menu(player, "Select a Mob to Spawn", 27);
		for (int i = 0; i < entities.length; i++){
			menu.addButton(getMobEgg(entities[i]));
		}
		menu.openMenu();
	}
	@EventHandler
	public void onMenuInteract(MenuInteractEvent event){
		if (menu.isMenuOpen() && event.getPlayer() == menu.getPlayer()){
			CreatureSpawner spawner = (CreatureSpawner) block.getState();
			String name = EntityType.fromId((int) event.getButtonItem().getDurability()).toString();//FIXME
			if (mobs.getAlias(name) == null){
				spawner.setCreatureTypeByName(name.toUpperCase());
			}else{
				spawner.setSpawnedType(mobs.getAlias(name));
			}
			spawner.update();
			menu.closeMenu();//FIXME
			player.sendMessage(ChatColor.GOLD+"Successfully changed spawner type!");
		}
	}
	private static ItemStack getMobEgg(int entityID){
		ItemStack egg = new ItemStack(Material.MONSTER_EGG);
		egg.setDurability((short) entityID);
		//ItemMeta meta = egg.getItemMeta();
		//meta.setDisplayName(ChatColor.RESET+"Spawn "+EntityType.fromId(entityID).toString()+"s");//FIXME
		//egg.setItemMeta(meta);
		return egg;
	}
}
