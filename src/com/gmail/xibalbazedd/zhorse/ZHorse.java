package com.gmail.xibalbazedd.zhorse;

import java.io.IOException;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.xibalbazedd.zhorse.managers.CommandManager;
import com.gmail.xibalbazedd.zhorse.managers.ConfigManager;
import com.gmail.xibalbazedd.zhorse.managers.DataManager;
import com.gmail.xibalbazedd.zhorse.managers.EconomyManager;
import com.gmail.xibalbazedd.zhorse.managers.EventManager;
import com.gmail.xibalbazedd.zhorse.managers.HorseManager;
import com.gmail.xibalbazedd.zhorse.managers.LocaleManager;
import com.gmail.xibalbazedd.zhorse.managers.MessageManager;
import com.gmail.xibalbazedd.zhorse.managers.PermissionManager;
import com.gmail.xibalbazedd.zhorse.utils.Metrics;

public class ZHorse extends JavaPlugin {
	
	private CommandManager commandManager;
	private ConfigManager configManager;
	private DataManager dataManager;
	private EconomyManager economyManager;
	private EventManager eventManager;
	private HorseManager horseManager;
	private LocaleManager localeManager;
	private MessageManager messageManager;
	private PermissionManager permissionManager;
	
	@Override
	public void onEnable() {
		initDependencies();
		initMetrics();
		initManagers();
	}
	
	@Override
    public void onDisable() {
		horseManager.untrackHorses();
		dataManager.closeDatabase();
    }
	
	public void disable() {
		getServer().getPluginManager().disablePlugin(this);
	}
	
	private void initDependencies() {
		Plugin vault = getServer().getPluginManager().getPlugin("Vault");
		if (vault != null && !vault.isEnabled()) {
			getServer().getPluginManager().enablePlugin(vault);
		}
		else if (vault == null) {
			getLogger().severe(String.format("Vault is missing ! Disabling %s...", getDescription().getName()));
			disable();
		}
	}
    
    private void initMetrics() {
    	try {
    		Metrics metrics = new Metrics(this);
			metrics.start();
        } catch (IOException e) {
        	getLogger().severe("Failed to start Metrics !");
        }
    }
    
    private boolean initManagers() {
    	commandManager = new CommandManager(this);
		configManager = new ConfigManager(this);
		dataManager = new DataManager(this);
		economyManager = new EconomyManager(this);
		eventManager = new EventManager(this);
		horseManager = new HorseManager(this);
		localeManager = new LocaleManager(this);
		messageManager = new MessageManager(this);
		permissionManager = new PermissionManager(this);
		
		boolean conformConfig = configManager.checkConformity();
		boolean conformLocale = localeManager.checkConformity();
		
		dataManager.openDatabase();
		horseManager.trackHorses();
		
		return conformConfig && conformLocale;
	}
    
	public boolean reload() {
		horseManager.untrackHorses();
		dataManager.closeDatabase();
		return initManagers();
	}
	
	public ConfigManager getCM() {
    	return configManager;
    }
	
	public CommandManager getCmdM() {
		return commandManager;
	}
    
    public DataManager getDM() {
    	return dataManager;
    }
	
	public EconomyManager getEM() {
		return economyManager;
	}
	
	public EventManager getEvM() {
		return eventManager;
	}
	
	public HorseManager getHM() {
		return horseManager;
	}
	
	public LocaleManager getLM() {
		return localeManager;
	}
	
	public MessageManager getMM() {
		return messageManager;
	}
	
	public PermissionManager getPM() {
		return permissionManager;
	}

}