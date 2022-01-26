package org.metadevs.agenziaimmobiliare;

import fr.minuskube.inv.InventoryManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.metadevs.agenziaimmobiliare.commands.ImmobiliCmd;

public final class AgenziaImmobiliare extends JavaPlugin {


    private InventoryManager inventoryManager;
    private Economy econ;
    private Permission perms;
    private String pexGroup;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        pexGroup = getConfig().getString("options.pex-immobiliare")
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();
        if (!setupEconomy() ) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupMySql();

        registerCommands();
    }

    private void setupMySql() {

    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {
        getCommand("immobiliari").setExecutor(new ImmobiliCmd(this));

    }


    /*
     *  Vault dependency methods
     */
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    /*
     *  Getters
     */
    public Economy getEconomy() {
        return econ;
    }
    public Permission getPermissions() {
        return perms;
    }
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
    
    public String color(String toColor) {
        return pexGroup;
    }
}

