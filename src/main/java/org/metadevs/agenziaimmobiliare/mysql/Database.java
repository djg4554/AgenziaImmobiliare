package org.metadevs.agenziaimmobiliare.mysql;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;


public class Database<T extends JavaPlugin> {

    private final T plugin;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;


    public Database(T plugin) {
        this.plugin = plugin;
    }

    /**
     * load all the database info from the config
     */
    public boolean loadDatabase() {
        FileConfiguration config = plugin.getConfig();

        if (!config.getBoolean("mysql.active")) {
            Bukkit.getConsoleSender().sendMessage(color("&cError: Once set the database info, set enabled: to true"));
            return false;
        }

        ConfigurationSection dbSection = config.getConfigurationSection("mysql");

        if (dbSection == null) {
            Bukkit.getConsoleSender().sendMessage(color("&cError: Invalid database secion, check the file config.yml"));
            return false;
        }

        host = dbSection.getString("host", "INVALID");
        if (host.equals("INVALID")) {
            Bukkit.getConsoleSender().sendMessage(color("&cError: Invalid host , check the file config.yml"));
            return false;
        }

        port = dbSection.getInt("port", Integer.MIN_VALUE);
        if (port == Integer.MIN_VALUE) {
            Bukkit.getConsoleSender().sendMessage(color("&cError: Invalid port , check the file config.yml"));
            return false;
        }

        database = dbSection.getString("database", "INVALID");
        if (database.equals("INVALID")) {
            Bukkit.getConsoleSender().sendMessage(color("&cError: Invalid database name, check the file config.yml"));
            return false;
        }

        username = dbSection.getString("username", "INVALID");
        if (username.equals("INVALID")) {
            Bukkit.getConsoleSender().sendMessage(color("&cError: Invalid username, check the file config.yml"));
            return false;
        }

        password = dbSection.getString("password", "INVALID");
        if (password.equals("INVALID")) {
            Bukkit.getConsoleSender().sendMessage(color("&cError: Invalid password, check the file config.yml"));
            return false;
        }

        return true;


    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getDatabase() {
        return database;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

}
