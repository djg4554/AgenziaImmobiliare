package org.metadevs.agenziaimmobiliare.commands.agim;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;

public class DeleteCmd {
    public DeleteCmd(AgenziaImmobiliare plugin, @NotNull CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUtilizzo: /agim delete <id>");
            return;
        }
        String id = args[1];
        plugin.getPropertyManager().removeProperty(id).whenComplete((result, error) -> {
            if (error != null) {
                sender.sendMessage("§cErrore durante la rimozione della proprietà con id " + id + ": " + error.getMessage());
            } else {
                if (result) {
                    sender.sendMessage("§aProprietà con id " + id + " rimossa con successo");
                } else {
                    sender.sendMessage("§cProprietà con id " + id + " non trovata");
                }
            }

        });
    }
}
