package org.metadevs.agenziaimmobiliare.key;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;
import org.metadevs.agenziaimmobiliare.structure.Property;
import org.metadevs.agenziaimmobiliare.utils.ItemParser;
import org.metadevs.agenziaimmobiliare.utils.PlaceholderParser;
import org.metadevs.agenziaimmobiliare.utils.Utils;
import org.bukkit.Material;

import java.io.IOException;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;

public class KeyManager {
    private AgenziaImmobiliare plugin;
    private Material keyMaterial;

    public KeyManager(AgenziaImmobiliare plugin) {
        this.plugin = plugin;
        keyMaterial = Material.valueOf(plugin.getConfig().getString("key.material"));
    }

    public void giveKey(Player player , Property property){
        ItemStack itemStack = new ItemParser(plugin.getConfig().getConfigurationSection("key").getValues(false), property).parse();
        NBTItem nbtItem = new NBTItem(itemStack);
        try {
            nbtItem.setString("property", property.serialize());
        } catch (IOException e) {
            player.sendMessage(color("&cErrore durante la generazione della chiave per la proprietà &6" + property.getName() + "&c."));
            Bukkit.getLogger().severe("Error while serializing property"+ property.getName());
            return;
        }

        if (!player.getInventory().addItem(nbtItem.getItem()).isEmpty()) {
            player.sendMessage(Utils.getMessage("key.give.error", "&cIl tuo inventario è pieno!."));
            return;
        }

        player.sendMessage(Utils.getMessage("key.give.success", "&aHai ricevuto la chiave per la proprietà &6" + property.getName() + "&a."));

    }

    public void giveLostKey(Player player, Property property) {
        ItemStack itemStack = new ItemParser(plugin.getConfig().getConfigurationSection("key").getValues(false), property).parse();
        NBTItem nbtItem = new NBTItem(itemStack);
        try {
            nbtItem.setString("property", property.serialize());
        } catch (IOException e) {
            player.sendMessage(color("&cErrore durante la generazione della chiave per la proprietà &6" + property.getName() + "&c."));
            Bukkit.getLogger().severe("Error while serializing property"+ property.getName());
            return;
        }
        ItemStack key = nbtItem.getItem();
        ItemMeta meta = key.getItemMeta();

        meta.getLore().add(PlaceholderParser.parse(plugin.getConfig().getString("key.owner-lore", "&cbroken lore owner name"), "proprietario",player.getName()));
        key.setItemMeta(meta);
        if (!player.getInventory().addItem(key).isEmpty()) {
            player.sendMessage(Utils.getMessage("key.give.error", "&cIl tuo inventario è pieno!."));
            return;
        }

        player.sendMessage(Utils.getMessage("key.give.success", "&aHai ricevuto la chiave per la proprietà &6" + property.getName() + "&a."));

    }

    public boolean isKey(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasKey("property");
    }

    public Key getKey(ItemStack mainHand) throws IOException, ClassNotFoundException {


        return new Key(Property.deserialize(new NBTItem(mainHand).getString("property")));
    }

    public Material getKeyMaterial() {
        return keyMaterial;
    }

    public boolean hasKeyInInventory(Player player, Property property) {

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || !(keyMaterial.equals(itemStack.getType()))) continue;
                NBTItem nbtItem = new NBTItem(itemStack);
                if (nbtItem.hasKey("property")) {
                    try {
                        if (Property.deserialize(nbtItem.getString("property")).getName().equals(property.getName())) return true;
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

        return false;
    }
}
