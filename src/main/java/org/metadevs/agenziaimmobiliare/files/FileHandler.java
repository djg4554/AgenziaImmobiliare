package org.metadevs.agenziaimmobiliare.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public abstract class FileHandler< T extends JavaPlugin> {

    private T plugin;
    private File file;
    private File pluginFolder;
    private final String fileName;
    private FileConfiguration config;

    /**
     * Constructor that calls the setup method
     * @param plugin
     */
    public FileHandler(T plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        setup();
    }

    /**
     * Create the plugin folder if it doesnt exists
     */
    private void setup() {
        pluginFolder = plugin.getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }
    }

    /**
     * Create the data file if doesnt exists
     */
    public void setupFile() {
        file = new File(pluginFolder, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Bukkit.getConsoleSender().sendMessage(new ComponentBuilder("File "+ fileName + " created correctly!").color(ChatColor.GREEN).create() );
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage(new ComponentBuilder("Could not create "+ fileName + "!").color(ChatColor.RED).create());
            }
        }
    }

    /**
     * this return everytime the configuration of the file
     * @return
     */
    public FileConfiguration getConfig() {
        if (!file.exists()) {
            setupFile();
        }
        if (config == null) {
            config = YamlConfiguration.loadConfiguration(file);
        }
        return config;
    }

    /**
     * save the config into the file, if the file exists and the config is not null
     * @return false if the file is not saved
     */
    public boolean save() {

        if (!file.exists() || config == null) {
            return false;
        }
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Could not save "+ fileName + "!");
            return false;
        }
    }



}