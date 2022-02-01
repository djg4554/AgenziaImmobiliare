package org.metadevs.agenziaimmobiliare;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.metadevs.agenziaimmobiliare.commands.ImmobiliCmd;
import org.metadevs.agenziaimmobiliare.commands.agim.AgimCmd;
import org.metadevs.agenziaimmobiliare.gui.GuiManager;
import org.metadevs.agenziaimmobiliare.key.KeyManager;
import org.metadevs.agenziaimmobiliare.listener.PlayerListener;
import org.metadevs.agenziaimmobiliare.mysql.SqlHandler;
import org.metadevs.agenziaimmobiliare.structure.PropertyManager;
import org.metadevs.agenziaimmobiliare.worldguard.WorldGuardManager;

import java.sql.SQLException;

public final class AgenziaImmobiliare extends JavaPlugin {


    private Economy econ;
    private Permission perms;

    private SqlHandler sqlHandler;
    private GuiManager guiManager;

    private String pexGroup;
    private boolean active = true;
    private PropertyManager propertyManager;
    private WorldGuardManager worldGuardManager;
    private KeyManager keyManager;

    @Override
    public void onEnable() {
        //controlla se il plugin e' stato installato per la prima volta, e crea il file config
        if (!getDataFolder().exists())
            saveDefaultConfig();
        load();
    }

    //Attiva il plugin
    public void load() {
        reloadConfig();
        pexGroup = getConfig().getString("options.pex-immobiliare");
        setupVaultDependency();
        setupWorldGuardDependency();
        setupMySql();
        System.out.println(active);
        if (active) {
            guiManager = new GuiManager(this);
            propertyManager = new PropertyManager(this);
            propertyManager.init();
            keyManager = new KeyManager(this);
        }

        registerListener();
        registerCommands();

    }

    private void registerListener() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void setupWorldGuardDependency() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null) {
            active = false;
            System.out.println("WorldGuard non trovato, plugin disattivato");
            return;
        }
        worldGuardManager = new WorldGuardManager(this);
    }

    private void setupVaultDependency() {
        if (!setupEconomy() ) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            active = false;
        }
        if (!setupPermissions()) {
            active = false;
        }
    }

    private void setupMySql() {
        sqlHandler = new SqlHandler(this);
        try {
            if (!sqlHandler.init()) {
                Bukkit.getLogger().severe("[AgenziaImmobiliare] - MySQL connection failed!");
                active = false;
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[AgenziaImmobiliare] - MySQL connection failed!");
            active = false;
        }
        sqlHandler.loadDatabase().whenComplete((result, error) -> {
            if (error != null) {
                Bukkit.getLogger().severe("[AgenziaImmobiliare] - MySQL connection failed!");
                active = false;
            } else {
                Bukkit.getLogger().info("[AgenziaImmobiliare] - MySQL connection successful!");
            }
        });

    }

    @Override
    public void onDisable() {

    }

    private void registerCommands() {
        getCommand("agim").setExecutor(new AgimCmd(this));
        getCommand("immobili").setExecutor(new ImmobiliCmd(this));



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

    public String getPexGroup() {
        return pexGroup;
    }

    public SqlHandler getSqlHandler() {
        return sqlHandler;
    }

    public boolean isActive() {
        return active;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public WorldGuardManager getWorldGuardManager() {
        return worldGuardManager;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }


}


