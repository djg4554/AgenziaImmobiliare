package org.metadevs.agenziaimmobiliare.commands.agim;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;
import org.metadevs.agenziaimmobiliare.structure.Property;
import org.metadevs.agenziaimmobiliare.utils.PlaceholderParser;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;
import static org.metadevs.agenziaimmobiliare.utils.Utils.getMessage;

public class GiveKeyCmd {

    public GiveKeyCmd(AgenziaImmobiliare plugin, @NotNull CommandSender sender, String... args) {
        String giveCommandSection = "agim.givekey.";
        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessage(giveCommandSection + "player-only", "&cQuesto comando è disponibile solo per i giocatori."));
            return;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage(color("§c/agim giveKey <property_name>"));

            return;
        }

        String nome = args[1];
        Property property;
        if ((property = plugin.getPropertyManager().getPropertyByName(nome)) != null) {
            plugin.getKeyManager().giveLostKey(player, property);
        } else {
            sender.sendMessage(PlaceholderParser.parse(getMessage(giveCommandSection + "property-not-exists", "L'immobile non esiste"), "nome", nome));
        }
    }
}
