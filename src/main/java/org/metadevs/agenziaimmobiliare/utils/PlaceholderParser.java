package org.metadevs.agenziaimmobiliare.utils;

import java.util.HashMap;
import java.util.Objects;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;

public class PlaceholderParser {

    public static String parse(String input, String... placeholderValues) {
        Objects.requireNonNull(input);
        if (placeholderValues.length == 0 || placeholderValues.length % 2 != 0) {
            return input;
        }
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < placeholderValues.length; i += 2) {
            map.put(placeholderValues[i], placeholderValues[i + 1]);
        }
        for (String key : map.keySet()) {
            input = input.replaceAll("%"+key+"%", map.get(key));
        }

        return color(input);
    }
}
