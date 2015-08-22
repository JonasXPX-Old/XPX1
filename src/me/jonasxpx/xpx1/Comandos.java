package me.jonasxpx.xpx1;

import me.jonasxpx.xpx1.Enums.Locations;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Comandos implements CommandExecutor{
	
	protected Xpx1 plugin;
	private LocationsManager loc;
	public Comandos(Xpx1 plugin){
		this.plugin = plugin;
		loc = new LocationsManager(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		try{
		if(args.length == 0){
			commandos(sender);
		}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("camarote")){
				((Player)sender).teleport(loc.getLocation(Locations.ENTRADA));
				return true;
			}
			if(args[0].equalsIgnoreCase("aceitar")){
				Xpx1.pm.aceitarX1((Player)sender);
				return true;
			}
			commandos(sender);
			return true;
		}
		if(args.length == 2){
			if(args[0].equalsIgnoreCase("desafiar")){
				Xpx1.pm.desafiar((Player)sender, Bukkit.getPlayer(args[1]));
				return true;
			}
			if(sender.isOp()){
				if(args[0].equalsIgnoreCase("set")){
					Player player = (Player)sender;
					if(args[1].equalsIgnoreCase("loc1")){
						loc.setLocation(Locations.LOC1, player);
						player.sendMessage("§6Loc1 foi marcada!.");
						return true;
					}
					if(args[1].equalsIgnoreCase("loc2")){
						loc.setLocation(Locations.LOC2, player);
						player.sendMessage("§6Loc2 foi marcada");
						return true;
					}
					if(args[1].equalsIgnoreCase("saida")){
						loc.setLocation(Locations.SAIDA, player);
						player.sendMessage("§6Saida foi marcada!.");
						return true;
					}
					if(args[1].equalsIgnoreCase("entrada")){
						loc.setLocation(Locations.ENTRADA, player);
						player.sendMessage("§6Entrada foi marcada");
						return true;
					}
				}
			}
			commandos(sender);
			return true;
		}
		}catch(NullPointerException e){
			sender.sendMessage("§cNão encontrado!");
			e.printStackTrace();
		}
		return true;
	}
	
	
	private void commandos(CommandSender sender){
		sender.sendMessage("§d§m>-------------------------------------------------<");
		sender.sendMessage("\n§d§m>§6/x1 desafiar <nick> - para desafiar um jogador.");
		sender.sendMessage("§d§m>§6/x1 aceitar - para aceitar um desafio.");
		sender.sendMessage("§d§m>§6/x1 camarote - Vá ate o camarote");
		if(sender.isOp()){
			sender.sendMessage("§d§m>§6/x1 set <loc1,loc2,saida,entrada>");
		}
		sender.sendMessage("\n§d§m>-------------------------------------------------<");
	}
}
