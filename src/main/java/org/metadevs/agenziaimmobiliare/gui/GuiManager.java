package org.metadevs.agenziaimmobiliare.gui;

import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;
import org.metadevs.agenziaimmobiliare.gui.providers.*;
import org.metadevs.agenziaimmobiliare.structure.Property;
import org.metadevs.agenziaimmobiliare.utils.PlaceholderParser;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;

public class GuiManager {
    private final AgenziaImmobiliare plugin;
    private InventoryManager inventoryManager;

    public GuiManager(AgenziaImmobiliare plugin) {
        this.plugin = plugin;
        inventoryManager = new InventoryManager(plugin);
        inventoryManager.init();
    }


    public void openImmobiliareGui(Player player) {
        SmartInventory.builder()
                .title(color(plugin.getConfig().getString("guis.immobili.titolo-gui")))
                .provider(new ImmobiliareProvider(plugin))
                .manager(inventoryManager)
                .size(5,9)
                .build().open(player);

    }

    public void openTypeGui(SmartInventory parent, Player player, String type) {
        SmartInventory.builder()
                .title(PlaceholderParser.parse(color(plugin.getConfig().getString("guis.tipo-gui.titolo-gui")),"tipo", type))
                .provider(new TypeProvider(plugin, type))
                .parent(parent)
                .manager(inventoryManager)
                .size(5,9)
                .build().open(player);

    }

    public void openConfirmGui(Player player,Player seller, Property property, ItemStack key) {
        SmartInventory.builder()
                .title(color(plugin.getConfig().getString("guis.confirmation.titolo-gui")))
                .provider(new ConfirmationProvider(plugin,seller, property, key))
                .manager(inventoryManager)
                .size(5,9)
                .build().open(player);

    }

    public void openListGui(Player player) {
        SmartInventory.builder()
                .title(color(plugin.getConfig().getString("guis.list.titolo-gui")))
                .provider(new ListProvider(plugin))
                .manager(inventoryManager)
                .size(5,9)
                .build().open(player);

    }

    public void openTypeListGui(SmartInventory inventory, Player player, String type) {
        SmartInventory.builder()
                .title(PlaceholderParser.parse(color(plugin.getConfig().getString("guis.list.titolo-gui")),"tipo", type))
                .provider(new TypeListProvider(plugin, type))
                .parent(inventory)
                .manager(inventoryManager)
                .size(5,9)
                .build().open(player);

    }
}
