package org.metadevs.agenziaimmobiliare.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;

import java.util.Arrays;

public class ImmobiliCmd implements CommandExecutor {

    private final AgenziaImmobiliare plugin;

    public ImmobiliCmd(AgenziaImmobiliare agenziaImmobiliare) {
        this.plugin = agenziaImmobiliare;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (! (sender instanceof Player)) {
            sender.sendMessage("Solo i player possono utilizzare questo comando");
            return true;
        }
        Player player = (Player) sender;

        //controlla se il player ha il permesso "outlaws.immobili"
        if (Arrays.stream(plugin.getPermissions().getGroups()).anyMatch("").(player, "outlaws.immobili")) {

        return true;
    }
}
