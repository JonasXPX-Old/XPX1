package me.jonasxpx.xpx1;

import me.jonasxpx.xpx1.Enums.Locations;
import me.jonasxpx.xpx1.Listener.PlayerLosesEvent;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerManager {

	private Player desafiado;
	private Player mandante;
	public boolean status = false;
	public boolean aceito = false;
	public Player win;
	private boolean delayFull = true;
	private Xpx1 plugin;
	private String msgFim;
	private String msgAccept;
	private String msgArregou;
	private String msgDesafia;
	private BukkitTask ag;
	public LocationsManager loc;
	private BukkitTask ag2;
	
	/**
	 * 
	 * @param plugin - Main plugin
	 */
	public PlayerManager(Xpx1 plugin)
	{
		this.plugin = plugin;
		loc = new LocationsManager(plugin);
		loadMensagens();
	}
	
	/**
	 * 
	 * @param player1 mandante
	 * @param player2 desafiado
	 */
	public void desafiar(Player player1, Player player2){
		if(player1 == player2){
			player1.sendMessage("§cVocê nao pode se auto-desafiar");
			return;
		}
		if(status){
			player1.sendMessage("§cVocê nao pode desafiar, outro X1 esta aconteçendo.");
			return;
		}
		if(!Manager.moneyValid(player1, player2)){
			player1.sendMessage("§cUm dos jogadores não tem dinheiro suficiennte para ir X1!.");
			return;
		}
		Manager.regirsterListener();
		this.aceito = false;
		this.status = true;
		Manager.players.add(player1); Manager.players.add(player2);
		Manager.inDispute = true;
		this.mandante = player1;
		this.desafiado = player2;
		player2.playSound(player2.getLocation(), Sound.DIG_SAND, 1.0F, 1.5F);
		timeDown();
		Bukkit.broadcastMessage(msgDesafia.replaceAll("@desafiante", player1.getName()).replaceAll("@desafiado", player2.getName()));
	}
	
	
	/**
	 * 
	 * @param player desafiado
	 */
	public void aceitarX1(Player player){
		if(!status){
			player.sendMessage("§6Nada para aceitar!.");
			return;
		}
		if(this.desafiado == null || player != desafiado){
			player.sendMessage("§6Você nao pode aceitar este X1");
			return;
		}
		if(aceito){
			player.sendMessage("§cVoce não pode aceitar esse X1");
			return;
		}
		if(delayFull){
			if(delayFull){
				delayFull = false;
				player.sendMessage("\n§bCerteza? Você vs "+mandante.getName());
				player.sendMessage("§bCaso sim, digite novamente.\n");
				return;
			}
		}
		if(Manager.cta != null){
			if(Manager.cta.isInCombat(desafiado)){
				player.sendMessage("§cVocê nao pode aceitar porque esta em combate.");
				return;
			}
			if(Manager.cta.isInCombat(mandante)){
				player.sendMessage("§cO outro jogador ainda está em combate!.");
				return;
			}
		}
		aceitou();
	}
	
	
	public void aceitou(){
		if(getPlugin().clanManager.getClanPlayer(desafiado) != null && getPlugin().clanManager.getClanPlayer(mandante) != null){
			if(!getPlugin().clanManager.getClanPlayer(desafiado).isFriendlyFire())
				getPlugin().clanManager.getClanPlayer(desafiado).setFriendlyFire(true);
			if(!getPlugin().clanManager.getClanPlayer(mandante).isFriendlyFire())
				getPlugin().clanManager.getClanPlayer(mandante).setFriendlyFire(true);
		}
		Manager.setInvisible(mandante, desafiado, true);
		String format1 = mandante.getName();
		String format2 = desafiado.getName();
		Manager.withdrawPlayer(mandante);Manager.withdrawPlayer(desafiado);
		if(format1.length() >= 16)
			format1 = format1.substring(0, 15);
		if(format2.length() >= 16)
			format2 = format2.substring(0, 15);
		
		Bukkit.broadcastMessage(msgAccept.replaceAll("@desafiante", mandante.getName()).replaceAll("@desafiado", desafiado.getName()));
		Bukkit.broadcastMessage(
				"§d§m>-------------------§dX1§m-------------------<"
				+ "\n\n§6   "+format1+"§4       VS       §6"+ format2
				+ "\n\n§d§m>-------------------§dX1§m-------------------<");
		ag.cancel();
		this.aceito = true;
		this.desafiado.teleport(loc.getLocation(Locations.LOC1));
		this.mandante.teleport(loc.getLocation(Locations.LOC2));
		for(Player play : Bukkit.getOnlinePlayers())
			play.playSound(play.getLocation(), Sound.BLAZE_HIT, 1.0F, 1.0F);
		fimDoTempo();
		
	}
	
	
	public void arregar()
	{
		Bukkit.broadcastMessage(msgArregou.replaceAll("@desafiante", mandante.getName()).replaceAll("@desafiado", desafiado.getName()));
		clearAndStop();
	}
	
	public void forceStop(){
		Bukkit.broadcastMessage("§b[X1] §bX1 foi cancelado!.");
		Manager.cta.removePlayerTag(desafiado);
		Manager.cta.removePlayerTag(mandante);
		desafiado.teleport(loc.getLocation(Locations.SAIDA));
		mandante.teleport(loc.getLocation(Locations.SAIDA));
		Manager.payDraw(mandante, desafiado);
		Manager.setInvisible(desafiado, mandante, false);
		clearAndStop();
	}
	
	public void clearAndStop()
	{
		status = false;
		delayFull = true;
		aceito = false;
		desafiado = null;
		mandante = null;
		Manager.inDispute = false;
		Manager.players.clear();
		Manager.unregisterListener();
		ag.cancel();
		if(ag2 != null)
			ag2.cancel();
		win = null;
	}
	
	
	public void setWinner(String player)
	{
		win = Bukkit.getPlayer(player).getKiller();
		if(mandante == win)
			win = desafiado;
		else
			win = mandante;
		Manager.setInvisible(desafiado, mandante, false);
		getPlugin().getServer().getPluginManager().callEvent(new PlayerLosesEvent( Bukkit.getPlayer(player)));
		exitTeleportDelayed(win, Locations.SAIDA);
		String sFinal = "";
		if(win != null){
			sFinal = msgFim.replace("@winner",win.getName()).replace("@loser",  Bukkit.getPlayer(player).getName());
		} else {
			sFinal = msgFim.replace("@winner", "Jogador").replace("@loser", Bukkit.getPlayer(player).getName());
		}
		Bukkit.broadcastMessage(sFinal);
		Manager.cta.removePlayerTag(win);
	}
	
	
	public void loadMensagens()
	{
		this.msgAccept = plugin.getConfig().getString("Mensagens.msgAccept").replace('&', '§');
		this.msgArregou = plugin.getConfig().getString("Mensagens.msgArregou").replace('&', '§');
		this.msgDesafia = plugin.getConfig().getString("Mensagens.msgDesafia").replace('&', '§');
		this.msgFim = plugin.getConfig().getString("Mensagens.msgFim").replace('&', '§');
	} 
	
	
	public void forceStopQuit(Player player)
	{
		if(player == desafiado){
			Manager.setInvisible(desafiado, mandante, false);
			desafiado = null;
			Bukkit.broadcastMessage("§d"+player.getName()+" deu DC durante o X1, " + mandante.getName()+ " ganhou o X1");
			Manager.players.remove(player);
			player.setHealth(0);
			exitTeleportDelayed(mandante, Locations.SAIDA);
		}else{
			Manager.setInvisible(desafiado, mandante, false);
			mandante = null;
			Manager.players.remove(player);
			player.setHealth(0);
			Bukkit.broadcastMessage("§d"+player.getName()+" deu DC durante o X1, " + desafiado.getName() + " ganhou o X1");
			exitTeleportDelayed(desafiado, Locations.SAIDA);
		}
	}
	
	
	public void timeDown()
	{
		ag = new BukkitRunnable() {
			@Override
			public void run() {
				arregar();
			}
		}.runTaskLater(plugin, 20 * 60);
	}
	
	public void fimDoTempo(){
		ag2 = new BukkitRunnable() {
			@Override
			public void run() {
				forceStop();
			}
		}.runTaskLater(plugin, 20 * 420);
	}
	
	private void exitTeleportDelayed(final Player player, Locations loca)
	{
		new BukkitRunnable() {
			@Override
			public void run() {
				try{
						if(player.isOnline()){
						player.teleport(loc.getLocation(Locations.SAIDA));
					}
					Manager.cta.removePlayerTag(player);
				}catch(Exception ex){
					clearAndStop();
					if(mandante != null)
						mandante.teleport(loc.getLocation(Locations.SAIDA));
					if(desafiado != null)
						desafiado.teleport(loc.getLocation(Locations.SAIDA));
				}finally{
					Manager.payWinner(player);
					clearAndStop();
					System.out.println(player.getName() + "Foi pago");
				}
			}
		}.runTaskLater(plugin, 20 * 25);
	}
	
	public Xpx1 getPlugin(){
		return plugin;
	}
	
}
