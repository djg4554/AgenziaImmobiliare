package org.metadevs.agenziaimmobiliare.commands.agim;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;
import org.metadevs.agenziaimmobiliare.utils.PlaceholderParser;

import static org.metadevs.agenziaimmobiliare.utils.Utils.getMessage;


public class DeleteCmd {
    public DeleteCmd(AgenziaImmobiliare plugin, @NotNull CommandSender sender, String[] args) {
        String subCommandSection =  "agim.delete-subcommand.";;
        if (args.length < 2) {
            sender.sendMessage("§cUtilizzo: /agim delete <nome>");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1) {
                sb.append(" ");
            }
        }

        String nome = sb.toString();

        plugin.getPropertyManager().removeProperty(nome).whenComplete((result, error) -> {
            if (error != null) {

                sender.sendMessage(PlaceholderParser.parse(getMessage(subCommandSection + "sql-error", "§cErrore durante la rimozione della proprietà con id " + nome + ": " + error.getMessage()), "nome", nome));
            } else {
                if (result) {
                    sender.sendMessage(PlaceholderParser.parse(getMessage(subCommandSection + "property-deleted", "§aProprieta' " + nome + " rimossa con successo"), "nome", nome));
                } else {
                    sender.sendMessage(PlaceholderParser.parse(getMessage(subCommandSection + "property-not-exists", "Proprieta' non esistente"), "nome", nome));
                }
            }

        });
    }
}
