package me.jonasxpx.xpx1;

import me.jonasxpx.xpx1.Enums.Locations;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationsManager{

	public Xpx1 plugin;
	public LocationsManager(Xpx1 plugin) {
		this.plugin = plugin;
	}
	
	public void setLocation(Locations loc, Player player){
		Location lc = player.getLocation();
		switch (loc) {
		case LOC1:
			plugin.getConfig().set("Locations.LOC1", lc.getWorld().getName()+","+lc.getX()+","+lc.getY()+","+lc.getZ()+","+lc.getYaw()+","+lc.getPitch());
			plugin.saveConfig();
			break;
		case LOC2:
			plugin.getConfig().set("Locations.LOC2", lc.getWorld().getName()+","+lc.getX()+","+lc.getY()+","+lc.getZ()+","+lc.getYaw()+","+lc.getPitch());
			plugin.saveConfig();
			
			break;
		case ENTRADA:
			plugin.getConfig().set("Locations.ENTRADA", lc.getWorld().getName()+","+lc.getX()+","+lc.getY()+","+lc.getZ()+","+lc.getYaw()+","+lc.getPitch());
			plugin.saveConfig();
			
			break;
		case SAIDA:
			plugin.getConfig().set("Locations.SAIDA", lc.getWorld().getName()+","+lc.getX()+","+lc.getY()+","+lc.getZ()+","+lc.getYaw()+","+lc.getPitch());
			plugin.saveConfig();
			break;
		}
	}
	public Location getLocation(Locations loc){
		String[] lc;
		switch(loc){
		case LOC1:
			lc = plugin.getConfig().getString("Locations.LOC1").split(",");
			return new Location(Bukkit.getWorld(lc[0]), Double.parseDouble(lc[1]), Double.parseDouble(lc[2]), Double.parseDouble(lc[3]), Float.parseFloat(lc[4]), Float.parseFloat(lc[5]));
		case LOC2:
			lc = plugin.getConfig().getString("Locations.LOC2").split(",");
			return new Location(Bukkit.getWorld(lc[0]), Double.parseDouble(lc[1]), Double.parseDouble(lc[2]), Double.parseDouble(lc[3]), Float.parseFloat(lc[4]), Float.parseFloat(lc[5]));
		case ENTRADA:
			lc = plugin.getConfig().getString("Locations.ENTRADA").split(",");
			return new Location(Bukkit.getWorld(lc[0]), Double.parseDouble(lc[1]), Double.parseDouble(lc[2]), Double.parseDouble(lc[3]), Float.parseFloat(lc[4]), Float.parseFloat(lc[5]));
		case SAIDA:
			lc = plugin.getConfig().getString("Locations.SAIDA").split(",");
			return new Location(Bukkit.getWorld(lc[0]), Double.parseDouble(lc[1]), Double.parseDouble(lc[2]), Double.parseDouble(lc[3]), Float.parseFloat(lc[4]), Float.parseFloat(lc[5]));
		}
		return null;
	}
	
}
