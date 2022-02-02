package org.metadevs.agenziaimmobiliare.commands.agim;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;
import static org.metadevs.agenziaimmobiliare.utils.Utils.getMessage;

public class AgimCmd implements CommandExecutor {

    private final AgenziaImmobiliare plugin;

    public AgimCmd(AgenziaImmobiliare plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage(getMessage("agim.not-enough-permission", "You don't have permission to execute this command"));
            return true;
        }
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        if (!plugin.isActive()) {
            if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(color("&7Plugin reloaded"));
                plugin.load();
            }
        }

        switch (args[0]) {
            case "reload" -> {
                sender.sendMessage(color("&7Plugin reloaded"));
                plugin.load();
            }
            case "create" -> new CreateCmd(plugin, sender, args);
            case "delete" -> new DeleteCmd(plugin, sender, args);
            case "giveKey" -> new GiveKeyCmd(plugin, sender, args);
            case "list" -> new ListCmd(plugin, sender, args);
            default -> sendHelp(sender);
        }


        return true;
    }


    private void sendHelp(CommandSender sender) {
        String commandName = "agim";
        sender.sendMessage(color("&8&m  =[&c " + commandName + " &8&m]=  "));
        sender.sendMessage(color("                            "));
        sender.sendMessage(color("&c/" + commandName + " reload "));
        sender.sendMessage(color(" - &7 Reload the plugin"));
        sender.sendMessage(color("                            "));
        sender.sendMessage(color("&c/" + commandName + " create <tipo> <regionId> <prezzo> <nome>"));
        sender.sendMessage(color(" - &7 Crea un nuovo immobile"));
        sender.sendMessage(color("                            "));
        sender.sendMessage(color("&c/" + commandName + " delete <nome>"));
        sender.sendMessage(color(" - &7 Elimina l'immobile <nome> "));
        sender.sendMessage(color("                            "));
        sender.sendMessage(color("&c/" + commandName + " giveKey <nome>"));
        sender.sendMessage(color(" - &7 Restituisce la chiave dell'immobile <nome> "));//da usare in caso di perdita
        sender.sendMessage(color("                            "));
        sender.sendMessage(color("&c/" + commandName + " list"));
        sender.sendMessage(color(" - &7 Mostra una lista di tutti gli immobili"));//da usare in caso di perdita
        sender.sendMessage(color("                            "));
        sender.sendMessage(color("&c/" + commandName + " help "));
        sender.sendMessage(color(" - &7 Show this page "));
        sender.sendMessage(color("                            "));
        sender.sendMessage(color("&8&m  =[&c " + commandName + " &8&m]=  "));
    }
}
