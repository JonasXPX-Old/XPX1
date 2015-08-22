package me.jonasxpx.xpx1;

import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.trc202.CombatTag.CombatTag;
import com.trc202.CombatTagApi.CombatTagApi;


public class Xpx1 extends JavaPlugin{
	
	private Comandos cmd = new Comandos(this);
	public static PlayerManager pm;
	public ClanManager clanManager = null;
	public static Economy economy = null;
	@Override
	public void onEnable() {
		getCommand("x1").setExecutor(cmd);
		getConfig().options().copyDefaults(true);
		pm = new PlayerManager(this);
		saveConfig();
		setupEconomy();
		if(!clan()){
			getServer().getLogger().log(Level.WARNING, "SimpleClans nao encontrado");
		}
		if(getServer().getPluginManager().getPlugin("CombatTag") != null){
			Manager.cta = new CombatTagApi((CombatTag) getServer().getPluginManager().getPlugin("CombatTag"));
			getServer().getLogger().log(Level.WARNING, "Plugin CombatTag Encontrado!!!.");
		}
	}

	private boolean clan(){
	        try {
	            for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
	                if (plugin instanceof SimpleClans) {
	                	clanManager = ((SimpleClans) plugin).getClanManager();
	                	return true;
	                }
	            }
	        } catch (NoClassDefFoundError e) {
	            return false;
	        }
	        return false;
		}
	
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
	@Override
	public void onDisable() {
		if(Manager.inDispute)
			Manager.getInstance().forceStop();
	}
}
