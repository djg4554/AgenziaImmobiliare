package org.metadevs.agenziaimmobiliare.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;
import org.metadevs.agenziaimmobiliare.key.Key;
import org.metadevs.agenziaimmobiliare.structure.Property;
import org.metadevs.agenziaimmobiliare.structure.State;
import org.metadevs.agenziaimmobiliare.utils.Utils;

import java.io.IOException;
import java.util.Arrays;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;
import static org.metadevs.agenziaimmobiliare.utils.Utils.getMessage;

public class PlayerListener implements Listener {

    private AgenziaImmobiliare plugin;

    public PlayerListener(AgenziaImmobiliare plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSellerRightClickBuyer(PlayerInteractEntityEvent event) {
        if (plugin.isActive()) {
            if (event.getRightClicked() instanceof Player ) {


                if (event.getHand().equals(EquipmentSlot.HAND)) {
                    Player player = event.getPlayer();
                    ItemStack mainHand = player.getInventory().getItemInMainHand();
                    ItemStack offHand = player.getInventory().getItemInOffHand();
                    if (offHand == null && mainHand == null) {
                        return;
                    }
                    Key key = null;
                    ItemStack item = null;
                    if (plugin.getKeyManager().isKey(mainHand)) {
                        try {
                            key = plugin.getKeyManager().getKey(mainHand);
                            item = mainHand;
                        } catch (IOException | ClassNotFoundException e) {
                            player.sendMessage(color("Internal Error "));
                        }
                    } else if (plugin.getKeyManager().isKey(offHand)) {
                        try {
                            key = plugin.getKeyManager().getKey(offHand);
                            item = offHand;
                        } catch (IOException | ClassNotFoundException e) {
                            player.sendMessage(color("Internal Error "));
                        }
                    }
                    if (key != null) {
                        if (key.getProperty().getState().equals(State.SALE)) {
                            if (Arrays.stream(plugin.getPermissions().getPlayerGroups(player)).anyMatch(plugin.getPexGroup()::equals)) {
                                plugin.getGuiManager().openConfirmGui((Player) event.getRightClicked(), player, key.getProperty(), item);
                                player.sendMessage(getMessage("await-confirmation", key.getProperty().getName()));
                            } else {

                                player.sendMessage(Utils.getMessage("no-permission", "&cNon hai il permesso di vendere un immobile"));
                            }
                        }

                    }


                }
            }
        }
    }


    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (!plugin.isActive()) {
            return;
        }
        if (event.getPlayer().isOp())
            return;


        Block block = event.getBlockPlaced();
        if (block == null || Material.AIR.equals(block.getType())) {
            return;
        }

        Property property = null;
        if ((property = plugin.getPropertyManager().getPropertyFromRegion(block.getLocation())) != null) {
            if (!plugin.getWorldGuardManager().isOwnerInteracting(event.getPlayer(), property)) {
                event.getPlayer().sendMessage(getMessage("property.not-owner", "&cNon sei il proprietario di questo immobile"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.isActive()) {
            return;
        }
        if (event.getPlayer().isOp())
            return;

        Block block = event.getBlock();
        if (block == null || Material.AIR.equals(block.getType())) {
            return;
        }

        Property property = null;
        if ((property = plugin.getPropertyManager().getPropertyFromRegion(block.getLocation())) != null) {
            if (!plugin.getWorldGuardManager().isOwnerInteracting(event.getPlayer(), property)) {
                event.getPlayer().sendMessage(getMessage("property.not-owner", "&cNon sei il proprietario di questo immobile"));
                event.setCancelled(true);
            }
        }
    }



    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.isActive()) {
            return;
        }
        if (event.getPlayer().isOp())
            return;


        Block block = event.getClickedBlock();
        if (block == null || Material.AIR.equals(block.getType()))
            return;
        Property property = null;
        if ((property = plugin.getPropertyManager().getPropertyFromRegion(block.getLocation())) != null) {
            if (!plugin.getWorldGuardManager().isOwnerInteracting(event.getPlayer(), property)) {
                event.getPlayer().sendMessage(getMessage("property.not-owner", "&cNon sei il proprietario di questo immobile"));
                event.setCancelled(true);
            }
        }
    }
}



