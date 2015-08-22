package me.jonasxpx.xpx1.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLosesEvent extends Event {

	private final Player player;
	private HandlerList h = new HandlerList();
	
	public PlayerLosesEvent(Player p) {
		this.player = p;
	}
	
	public Player getLoser(){
		return player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return h;
	}

}
