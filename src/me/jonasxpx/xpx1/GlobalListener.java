package me.jonasxpx.xpx1;

import me.jonasxpx.xpx1.Enums.Locations;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GlobalListener implements Listener{

	@EventHandler
	public void playerDisconnect(PlayerQuitEvent e){
		if(Manager.inDispute){
			if(Manager.players.contains(e.getPlayer())){
				e.getPlayer().teleport(Manager.getInstance().loc.getLocation(Locations.SAIDA));
			}
		}
	}
	
}
