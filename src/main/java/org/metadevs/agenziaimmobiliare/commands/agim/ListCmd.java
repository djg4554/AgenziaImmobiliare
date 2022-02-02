package org.metadevs.agenziaimmobiliare.commands.agim;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;

import static org.metadevs.agenziaimmobiliare.utils.Utils.getMessage;

public class ListCmd {
    public ListCmd(AgenziaImmobiliare plugin, @NotNull CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessage("player-only", "&cQuesto comando è disponibile solo per i giocatori."));
            return;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("agim.list")|| !player.isOp()) {
            sender.sendMessage(getMessage("no-permission", "&cNon hai il permesso per eseguire questo comando."));
            return;
        }

        plugin.getGuiManager().openListGui(player);
    }
}
