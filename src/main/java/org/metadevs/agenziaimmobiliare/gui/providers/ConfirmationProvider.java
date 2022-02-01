package org.metadevs.agenziaimmobiliare.gui.providers;

import de.tr7zw.changeme.nbtapi.NBTItem;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;
import org.metadevs.agenziaimmobiliare.structure.Property;
import org.metadevs.agenziaimmobiliare.structure.State;
import org.metadevs.agenziaimmobiliare.utils.ItemBuilder;
import org.metadevs.agenziaimmobiliare.utils.ItemParser;
import org.metadevs.agenziaimmobiliare.utils.PlaceholderParser;

import java.io.IOException;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;
import static org.metadevs.agenziaimmobiliare.utils.Utils.getMessage;

public class ConfirmationProvider implements InventoryProvider {

    private final Property property;
    private final AgenziaImmobiliare plugin;
    private ItemStack key;
    private Player seller;

    public ConfirmationProvider(AgenziaImmobiliare plugin,Player seller, Property property, ItemStack key) {
        this.plugin = plugin;
        this.property = property;
        this.seller = seller;
        this.key = key;

    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("&0").build()));
        contents.fillColumn(4, ClickableItem.empty(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("&0").build()));
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                contents.set(i, j, ClickableItem.of(
                        new ItemParser(plugin.getConfig().getConfigurationSection("guis.confirmation.items.conferma").getValues(false), property.getName(), property.getPrice(), false).parse(), e ->
                        {
                            if (plugin.getEconomy().getBalance(player) > property.getPrice()) {
                                plugin.getEconomy().withdrawPlayer(player, property.getPrice());
                                Property propertyFromTree = plugin.getPropertyManager().getPropertyByRegion(property.getType(), property.getName());

                                propertyFromTree.setState(State.OWNED);
                                propertyFromTree.setOwner(player.getUniqueId());
                                plugin.getPropertyManager().updateProperty(player, propertyFromTree);
                                player.sendMessage(color(getMessage("property-bought", "Acquisto effettuato con successo!")));
                                player.closeInventory();
                                seller.getInventory().remove(key);
                                NBTItem nbtItem = new NBTItem(key);
                                try {
                                    nbtItem.setString("property", propertyFromTree.serialize());
                                } catch (IOException ex) {
                                    Bukkit.getLogger().severe("Error while serializing property");
                                }
                                key = nbtItem.getItem();
                                ItemMeta meta = key.getItemMeta();

                                meta.getLore().add(PlaceholderParser.parse(plugin.getConfig().getString("key.owner-lore", "&cbroken lore owner name"), "proprietario",player.getName()));
                                key.setItemMeta(meta);
                                if (!player.getInventory().addItem(key).isEmpty()) {
                                    player.getWorld().dropItem(player.getLocation(), key);
                                }


                                seller.sendMessage(PlaceholderParser.parse(getMessage("property-sold", "Hai venduto la proprieta'"), "nome", property.getName(), "prezzo", plugin.getEconomy().format(property.getPrice())));

                            } else {
                                player.sendMessage(color(plugin.getConfig().getString("messages.not-enough-money").replace("%prezzo%", plugin.getEconomy().format(property.getPrice())).replaceAll("%nome%", property.getName())));
                            }
                        }));
            }
        }
        for (int i = 1; i < 4; i++) {
            for (int j = 5; j < 8; j++) {
                contents.set(i, j, ClickableItem.of(
                        new ItemParser(plugin.getConfig().getConfigurationSection("guis.confirmation.items.annulla").getValues(false), property.getName(), property.getPrice(), false).parse(), e ->
                        {
                            player.closeInventory();
                            seller.sendMessage(color(getMessage("property.sale-cancelled", "Hai annullato la vendita della proprieta'")));

                        }));
            }
        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
