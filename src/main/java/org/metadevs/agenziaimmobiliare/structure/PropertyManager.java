package org.metadevs.agenziaimmobiliare.structure;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;
import org.metadevs.agenziaimmobiliare.key.Key;
import org.metadevs.agenziaimmobiliare.utils.Utils;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;
import static org.metadevs.agenziaimmobiliare.utils.Utils.getMessage;

public class PropertyManager {
    private final AgenziaImmobiliare plugin;

    private TreeMap<String, TreeMap<String, Property>> propertiesTypes;

    public PropertyManager(AgenziaImmobiliare plugin) {
        this.plugin = plugin;
    }

    public void init() {
        propertiesTypes = new TreeMap<>();

        retrieveProperties().whenComplete((aVoid, throwable) -> {
            if (throwable != null) {
                Bukkit.getConsoleSender().sendMessage("§c[AgenziaImmobiliare] Errore durante il recupero degli Immobili dal database");
            } else {
                Bukkit.getConsoleSender().sendMessage("§a[AgenziaImmobiliare] Recupero degli Immobili dal database completato");
            }
        });
    }


    public Set<String> getPropertyRegions() {
        Set<String> regions = new TreeSet<>();
        for (TreeMap<String, Property> properties : propertiesTypes.values()) {
            for (Property property : properties.values()) {
                regions.add(property.getRegionId());
            }
        }
        return regions;
    }

    public void addProperty(Property property) {

        if (propertiesTypes.containsKey(property.getType())) {
            propertiesTypes.get(property.getType()).put(property.getName(), property);
        } else {
            TreeMap<String, Property> properties = new TreeMap<>();
            properties.put(property.getName(), property);
            propertiesTypes.put(property.getType(), properties);
        }
    }

    public Property getPropertyByRegion(String type, String name) {
        if (propertiesTypes.containsKey(type)) {
            return propertiesTypes.get(type).get(name);
        }
        return null;
    }

    public boolean regionAlredyLinked(String regionId) {
        for (TreeMap<String, Property> properties : propertiesTypes.values()) {
            for (Property property : properties.values()) {
                if (property.getRegionId().equals(regionId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public TreeMap<String, Property> getProperties(String type) {
        if (propertiesTypes.containsKey(type)) {
            return propertiesTypes.get(type);
        }
        return null;
    }

    public TreeMap<String, TreeMap<String, Property>> getPropertiesGroupByTypes() {
        return propertiesTypes;
    }

    public CompletableFuture<Void> retrieveProperties() {
        return CompletableFuture.runAsync(() -> {
            String query = "SELECT * FROM Immobile";
            try (Connection connection = plugin.getSqlHandler().getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

                statement.executeQuery();
                while (statement.getResultSet().next()) {
                    ResultSet resultSet = statement.getResultSet();
                    String nome = resultSet.getString("nome");
                    String regionId = resultSet.getString("regionId");
                    String tipo = resultSet.getString("tipo");
                    String proprietario = resultSet.getString("proprietario");
                    State stato = proprietario == null ? State.SALE : State.OWNED;
                    long prezzo = resultSet.getLong("prezzo");
                    Property property = new Property(nome, regionId, tipo, stato, proprietario, prezzo);
                    addProperty(property);

                }
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("§c[AgenziaImmobiliare] Errore durante il recupero degli Immobili dal database");
            }
        });


    }

    public boolean existType(String tipo) {
        return propertiesTypes.containsKey(tipo);
    }

    public boolean existProperty(String type, String name) {
        if (propertiesTypes.containsKey(type)) {
            return propertiesTypes.get(type).containsKey(name);
        }
        return false;
    }

    public void createType(String tipo) {
        TreeMap<String, Property> properties = new TreeMap<>();
        propertiesTypes.put(tipo, properties);
    }

    public void createProperty(Player creator, String tipo, String nome, String regionId, Long prezzo) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String query = "INSERT INTO Immobile (nome, regionId, tipo, prezzo) VALUES (?, ?, ?, ?);";
                try (
                        Connection connection = plugin.getSqlHandler().getDataSource().getConnection();
                        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, nome);
                    statement.setString(2, regionId);
                    statement.setString(3, tipo);
                    statement.setLong(4, prezzo);
                    statement.executeUpdate();

                    Property property = new Property(nome, regionId, tipo, State.SALE, null, prezzo);
                    addProperty(property);
                    creator.sendMessage(Utils.getMessage("agim.create-subcommand.property-created", "§aImmobile creato con successo"));
                } catch (SQLException e) {
                    creator.sendMessage(getMessage("agim.create-subcommand.sql-error" , "§cErrore durante la creazione dell'immobile"));
                }
            }

        }.runTaskAsynchronously(plugin);
    }

    public Set<String> getTypes() {
        return propertiesTypes.keySet();
    }

    public CompletableFuture<Boolean> removeProperty(String name) {
        return CompletableFuture.supplyAsync(() -> {

            for (String type : propertiesTypes.keySet()) {
                if (propertiesTypes.get(type).remove(name) != null) {
                    sqlRemoveProperty(name);
                    return true;
                }
            }
            return false;
        });
    }

    private void sqlRemoveProperty(String name) {
        String query = "DELETE FROM Immobile WHERE nome = ?;";
        try (Connection connection = plugin.getSqlHandler().getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("§c[AgenziaImmobiliare] Errore durante la cancellazione dell'immobile" + name);
        }
    }

    public CompletableFuture<Void> updateProperty(Player owner, Property property) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE Immobile set proprietario = ? where nome = ?;";
            try (Connection connection = plugin.getSqlHandler().getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, owner.getUniqueId().toString());
                statement.setString(2, property.getName());
                statement.executeUpdate();
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("§c[AgenziaImmobiliare] Errore durante l'aggiornamento dell'Immobile");
            }

        });
    }

    public boolean isPlayerProperty(Location location, Key key) {
        return plugin.getWorldGuardManager().getRegionsFromLocation(location).getRegions().stream().anyMatch(region -> region.getId().equals(key.getProperty().getRegionId()));
    }

    public boolean isOwnerInteracting(Player player, Location location, Property property) {
        Iterator<ItemStack> it = player.getInventory().iterator();
        while (it.hasNext()) {
            ItemStack item = it.next();
            if (plugin.getKeyManager().isKey(item)) {
                Key key = null;
                try {
                    key = plugin.getKeyManager().getKey(item);
                } catch (IOException | ClassNotFoundException e) {
                    player.sendMessage(color("Internal Error "));
                    return false;
                }
                if (key.isOwner(player.getUniqueId()) && key.getProperty().equals(property)) {
                    return true;
                }
            }
        }
        return false;
    }


    private Property getPropertyByRegion(String regionId) {
        for (String type : propertiesTypes.keySet()) {
            for (Property property : propertiesTypes.get(type).values()) {
                if (property.getRegionId().equals(regionId)) {
                    return property;
                }
            }
        }
        return null;
    }

    public Property getPropertyByName(String name) {
        for (String type : propertiesTypes.keySet()) {
            Property property;
            if ((property = propertiesTypes.get(type).get(name)) != null) {
                return property;
            }
        }
        return null;
    }

    public Property getPropertyFromRegion(Location location) {
        Property property = null;
        for (ProtectedRegion region : plugin.getWorldGuardManager().getRegionsFromLocation(location).getRegions()) {
            if ((property = getPropertyByRegion(region.getId())) != null)
                return property;

        }
        return null;
    }



}
