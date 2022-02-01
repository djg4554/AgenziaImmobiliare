package org.metadevs.agenziaimmobiliare.commands.agim;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metadevs.agenziaimmobiliare.utils.PlaceholderParser;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;
import static org.metadevs.agenziaimmobiliare.utils.Utils.getMessage;

public class CreateCmd {

    public CreateCmd(AgenziaImmobiliare plugin, @NotNull CommandSender sender, String... args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessage("player-only", "&cQuesto comando è disponibile solo per i giocatori."));
            return;
        }

        Player player = (Player) sender;

        if (args.length < 5) {
            sender.sendMessage(color("§c/agim create <tipo> <nome> <regionId> <prezzo>"));
            return;
        }

        String tipo = args[1];
        String nome = args[2];

        if (plugin.getPropertyManager().existProperty(tipo, nome)) {
            sender.sendMessage(
                    PlaceholderParser.parse(getMessage("property.already-exists", "&cLa proprietà con nome " + nome + " e tipo " + tipo + " esiste già"), "nome", nome, "tipo", tipo));
            return;
        }

        String regionId = args[3];
        if (!plugin.getWorldGuardManager().regionExists(player.getWorld(), regionId)) {

            sender.sendMessage(getMessage("region.not-exists", "&cLa regione con id " + regionId + " non esiste"));
            return;
        }
        if (plugin.getPropertyManager().regionAlredyLinked(regionId)) {
            sender.sendMessage(getMessage("region.already-linked", "&cLa regione con id " + regionId + " è già collegata ad una proprietà"));
            return;
        }

        String prezzo = args[4];
        Long price;
        try {
            price = Long.parseLong(prezzo);
        } catch (NumberFormatException e) {
            sender.sendMessage(getMessage("invalid-price", "&cIl prezzo deve essere un numero"));
            return;
        }

        plugin.getPropertyManager().createProperty(player, tipo, nome, regionId, price);


    }
}
