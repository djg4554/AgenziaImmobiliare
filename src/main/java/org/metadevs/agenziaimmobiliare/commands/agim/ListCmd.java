package org.metadevs.agenziaimmobiliare.commands.agim;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;

import static org.metadevs.agenziaimmobiliare.utils.Utils.getMessage;

public class ListCmd {
    public ListCmd(AgenziaImmobiliare plugin, @NotNull CommandSender sender, String[] args) {
        String listCommandSection = "agim.list";
        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessage(listCommandSection + "player-only", "&cQuesto comando è disponibile solo per i giocatori."));
            return;
        }

        Player player = (Player) sender;

        plugin.getGuiManager().openListGui(player);
    }
}
