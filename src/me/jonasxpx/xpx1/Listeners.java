package me.jonasxpx.xpx1;

import me.jonasxpx.xpx1.Enums.Locations;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener{

	@EventHandler
	public void disconnectCheck(PlayerQuitEvent e){
		if(Manager.getInstance().win != e.getPlayer()){
			if(Manager.getInstance().aceito){
				if(Manager.players.contains(e.getPlayer()) && Manager.players.size() >= 2){
					Manager.getInstance().forceStopQuit(e.getPlayer());
					return;
				}
			}
		}
		if(Manager.players.contains(e.getPlayer()))
			if(!Manager.getInstance().aceito)
				Manager.getInstance().arregar();
		if(Manager.getInstance().status)
		{
			if(Manager.players.contains(e.getPlayer())){
				Manager.cta.removePlayerTag(e.getPlayer());
				e.getPlayer().teleport(Manager.getInstance().loc.getLocation(Locations.SAIDA));
			}
		}
	}
	
	@EventHandler
	public void deathEvent(PlayerDeathEvent death){
		if(Manager.getInstance().aceito){
			if(Manager.players.contains(death.getEntity())){
				Manager.getInstance().setWinner(new String(death.getEntity().getName()));
			}
		}
	}
	
	
}
