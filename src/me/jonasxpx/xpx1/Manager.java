package me.jonasxpx.xpx1;

import java.util.ArrayList;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.trc202.CombatTagApi.CombatTagApi;

public class Manager {

	public static ArrayList<Player> players = new ArrayList<Player>();
	public static boolean inDispute = false;
	public static CombatTagApi cta;
	private static Listeners event = null;
	
	public static PlayerManager getInstance(){
		return Xpx1.pm;
	}

	public static void regirsterListener(){
		getInstance().getPlugin().getServer().getPluginManager().registerEvents(event = new Listeners(), getInstance().getPlugin());
	}
	
	public static void unregisterListener(){
		if(event != null)
		HandlerList.unregisterAll(event);
	}
	
	public static Economy getEconomy(){
		return Xpx1.economy;
	}
	
	public static boolean moneyValid(Player player, Player player1){
		if(getEconomy().getBalance(player) >= getInstance().getPlugin().getConfig().getDouble("ValorDesafiar")
				&& getEconomy().getBalance(player1) >= getInstance().getPlugin().getConfig().getDouble("ValorDesafiar")){
			return true;
		}
		return false;
	}
	
	public static void payWinner(Player player){
		try{
			getEconomy().depositPlayer(player, getInstance().getPlugin().getConfig().getDouble("ValorWinner"));
		}catch(NullPointerException e){
			player.sendMessage("§cERROR! Informe ao STAFF \"Payment failed\"");
		}
	}
	
	public static void payDraw(Player player1, Player player2){
		try{
			getEconomy().depositPlayer(player1, getInstance().getPlugin().getConfig().getDouble("ValorDesafiar"));
			getEconomy().depositPlayer(player2, getInstance().getPlugin().getConfig().getDouble("ValorDesafiar"));
		}catch(NullPointerException e){
			player1.sendMessage("§cERROR! Informe ao STAFF \"Payment failed\"");
		}
	}
	
	public static void withdrawPlayer(Player player){
		getEconomy().withdrawPlayer(player, getInstance().getPlugin().getConfig().getDouble("ValorDesafiar"));
	}
	
	public static void setInvisible(Player player1, Player player2, boolean invisible){
		for(Player oPlayer : Bukkit.getOnlinePlayers()){
			if(invisible){
				if(oPlayer != player1){
					if(oPlayer != player2){
						player1.hidePlayer(oPlayer);
						player2.hidePlayer(oPlayer);
					}
				}
			}else{
				if(oPlayer != player1){
					if(oPlayer != player2){
						player1.showPlayer(oPlayer);
						player2.showPlayer(oPlayer);
					}
				}
			}
		}
	}
}
