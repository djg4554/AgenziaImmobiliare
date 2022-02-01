package org.metadevs.agenziaimmobiliare.utils;

import org.bukkit.ChatColor;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;

public class Utils {

    public static String color(String toColor) {
        return ChatColor.translateAlternateColorCodes('&', toColor);
    }


    public static String getMessage(String key, String defaultMessage) {
        return color(AgenziaImmobiliare.getPlugin(AgenziaImmobiliare.class).getConfig().getString("messages." + key, defaultMessage));

    }
}
