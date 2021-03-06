package org.metadevs.agenziaimmobiliare.gui.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;
import org.metadevs.agenziaimmobiliare.structure.Property;
import org.metadevs.agenziaimmobiliare.structure.State;
import org.metadevs.agenziaimmobiliare.utils.ItemBuilder;
import org.metadevs.agenziaimmobiliare.utils.ItemParser;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;

public class TypeListProvider implements InventoryProvider {

    private final AgenziaImmobiliare plugin;
    private final String type;

    public TypeListProvider(AgenziaImmobiliare plugin, String type) {
        this.plugin = plugin;
        this.type = type;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("&0").build()));
        TreeMap<String, Property> properties = plugin.getPropertyManager().getProperties(type);
        if (properties.isEmpty()) {
            player.sendMessage(color("Non ci sono proprieta' al momento"));
            return;
        }

        int itemsPerPage = 21;

        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(itemsPerPage);

        ClickableItem[] items = new ClickableItem[properties.size()];
        Iterator<Property> it = properties.values().stream().filter(property -> property.getState().equals(State.SALE)).collect(Collectors.toList()).iterator();
        int i = 0;
        while (it.hasNext()) {
            Property property = it.next();
            //todo parse sistemare
            items[i] = ClickableItem.empty(new ItemParser(plugin.getConfig().getConfigurationSection("guis.list.items.immobile-generico").getValues(false), property.getName(), property.getType(), Bukkit.getOfflinePlayer(property.getOwner()).getName()).parse());
            i++;
        }
        pagination.setItems(items);
        setItems(contents, pagination.getPageItems());





        if (!pagination.isFirst()) {
            contents.set(4, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).name(color(plugin.getConfig().getString(("guis.pagination.previous-page")))).build(), e -> contents.inventory().open(player, pagination.previous().getPage())));
        }

        if (!pagination.isLast()) {
            contents.set(4, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).name(color(plugin.getConfig().getString(("guis.pagination.next-page")))).build(), e -> contents.inventory().open(player, pagination.next().getPage())));
        }

        contents.set(4,4 , ClickableItem.of(new ItemBuilder(Material.NETHER_STAR).name(color(plugin.getConfig().getString("guis.pagination.back"))).build(), e -> contents.inventory().getParent().ifPresent(inventory -> inventory.open(player))));



    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void setItems(InventoryContents contents, ClickableItem[] items) {

        int i = 0;
        int x = 1;
        while (x < 4 && i < items.length) {
            int y = 1;
            while (y < 8 && i < items.length) {
                contents.set(x, y, items[i]);
                i++;
                y++;
            }
            x++;
        }

    }
}
