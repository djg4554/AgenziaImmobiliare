package org.metadevs.agenziaimmobiliare.commands.agim;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;
import org.metadevs.agenziaimmobiliare.utils.PlaceholderParser;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;
import static org.metadevs.agenziaimmobiliare.utils.Utils.getMessage;

public class CreateCmd {



    public CreateCmd(AgenziaImmobiliare plugin, @NotNull CommandSender sender, String... args) {

        String subcommandSection = "agim.create-subcommand.";
        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessage(subcommandSection + "player-only", "&cQuesto comando è disponibile solo per i giocatori."));
            return;
        }

        Player player = (Player) sender;

        if (args.length < 5) {
            sender.sendMessage(color("§c/agim create <tipo> <regionId> <prezzo> <nome>"));
            return;
        }

        String tipo = args[1];

        String regionId = args[2];
        if (!plugin.getWorldGuardManager().regionExists(player.getWorld(), regionId)) {

            sender.sendMessage(getMessage(subcommandSection+ "region-not-exists", "&cLa regione con id " + regionId + " non esiste"));
            return;
        }
        if (plugin.getPropertyManager().regionAlredyLinked(regionId)) {
            sender.sendMessage(getMessage(subcommandSection + "region-already-linked", "&cLa regione con id " + regionId + " è già collegata ad una proprietà"));
            return;
        }

        String prezzo = args[3];
        Long price;
        try {
            price = Long.parseLong(prezzo);
        } catch (NumberFormatException e) {
            sender.sendMessage(getMessage(subcommandSection + "invalid-price", "&cIl prezzo deve essere un numero"));
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1) {
                sb.append(" ");
            }
        }
        String nome  = sb.toString();

        if (plugin.getPropertyManager().existProperty(tipo, nome)) {
            sender.sendMessage(
                    PlaceholderParser.parse(getMessage(subcommandSection + "property-already-exists", "&cLa proprietà con nome " + nome + " e tipo " + tipo + " esiste già"), "nome", nome, "tipo", tipo));
            return;
        }


        plugin.getPropertyManager().createProperty(player, tipo, nome, regionId, price);


    }
}
