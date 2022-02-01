package org.metadevs.agenziaimmobiliare.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;

import java.util.Arrays;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;

public class ImmobiliCmd implements CommandExecutor {

    private final AgenziaImmobiliare plugin;

    public ImmobiliCmd(AgenziaImmobiliare agenziaImmobiliare) {
        this.plugin = agenziaImmobiliare;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!plugin.isActive()) {

            sender.sendMessage(color("&7[&cAgenziaImmobiliare&7] Plugin is not active check your config.yml"));

            return true;
        }

        if (! (sender instanceof Player)) {
            sender.sendMessage("Solo i player possono utilizzare questo comando");
            return true;
        }
        Player player = (Player) sender;

        if (player.isOp() || Arrays.stream(plugin.getPermissions().getPlayerGroups(player)).anyMatch(s -> s.equalsIgnoreCase(plugin.getPexGroup()))) {
            //worldguard, gui di conferma
            plugin.getGuiManager().openImmobiliareGui(player);
        }

        return true;
    }
}
