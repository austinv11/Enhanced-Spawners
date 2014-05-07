package io.github.austinv11.EnhancedSpawners;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
//currently supports yml's with Strings, ints, booleans, and doubles
public class FileHandler {
	FileConfiguration config;
	public FileHandler(FileConfiguration pluginConfig){
		config = pluginConfig;
	}
	public void addDefault(File file, String destination, String value){//String ver
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		fileCon.addDefault(destination, value);
		fileCon.options().copyDefaults(true);
		save(file, fileCon);
	}
	public void addDefault(File file, String destination, int value){//int ver
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		fileCon.addDefault(destination, value);
		fileCon.options().copyDefaults(true);
		save(file, fileCon);
	}
	public void addDefault(File file, String destination, boolean value){//boolean ver
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		fileCon.addDefault(destination, value);
		fileCon.options().copyDefaults(true);
		save(file, fileCon);
	}
	public void addDefault(File file, String destination, double value){//double ver
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		fileCon.addDefault(destination, value);
		fileCon.options().copyDefaults(true);
		save(file, fileCon);
	}
	public void set(File file, String destination, String value){//String ver
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		fileCon.set(destination, value);
		fileCon.options().copyDefaults(true);
		save(file, fileCon);
	}
	public void set(File file, String destination, int value){//int ver
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		fileCon.set(destination, value);
		fileCon.options().copyDefaults(true);
		save(file, fileCon);
	}
	public void set(File file, String destination, boolean value){//boolean ver
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		fileCon.set(destination, value);
		fileCon.options().copyDefaults(true);
		save(file, fileCon);
	}
	public void set(File file, String destination, double value){//double ver
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		fileCon.set(destination, value);
		fileCon.options().copyDefaults(true);
		save(file, fileCon);
	}
	public String getString(File file, String destination){
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		return fileCon.getString(destination);
	}
	public int getInt(File file, String destination){
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		return fileCon.getInt(destination);
	}
	public boolean getBoolean(File file, String destination){
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		return fileCon.getBoolean(destination);
	}
	public double getdouble(File file, String destination){
		FileConfiguration fileCon = YamlConfiguration.loadConfiguration(file);
		return fileCon.getDouble(destination);
	}
	private void save(File file, FileConfiguration fileC){
		try{
			fileC.save(file);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
