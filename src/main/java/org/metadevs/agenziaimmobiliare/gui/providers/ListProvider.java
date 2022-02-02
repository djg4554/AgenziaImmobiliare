package org.metadevs.agenziaimmobiliare.gui.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;
import org.metadevs.agenziaimmobiliare.utils.ItemBuilder;
import org.metadevs.agenziaimmobiliare.utils.ItemParser;

import java.util.Iterator;
import java.util.Set;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;

public class ListProvider implements InventoryProvider {

    private AgenziaImmobiliare plugin;

    public ListProvider(AgenziaImmobiliare plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("&0").build()));
        Set<String> types = plugin.getPropertyManager().getTypes();
        if (types.isEmpty()) {
            player.sendMessage(color("Non ci sono proprieta' al momento"));
            return;
        }

        int itemsPerPage = 21;

        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(itemsPerPage);

        ClickableItem[] items = new ClickableItem[types.size()];
        Iterator<String> it = types.iterator();
        int i = 0;
        while (it.hasNext()) {
            String type = it.next();
            items[i] = ClickableItem.of(new ItemParser(plugin.getConfig().getConfigurationSection("guis.immobili.items.tipo-generico").getValues(false), type, plugin.getPropertyManager().getProperties(type).size()).parse(), e -> {
                plugin.getGuiManager().openTypeListGui(contents.inventory(), player, type);
            });
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




    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void setItems(InventoryContents contents, ClickableItem[] items) {

        int i = 0;
        int x = 1;
        while (i < items.length) {

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
