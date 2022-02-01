package org.metadevs.agenziaimmobiliare.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.metadevs.agenziaimmobiliare.structure.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.metadevs.agenziaimmobiliare.utils.Utils.color;

public class ItemParser {
    private Map<String, Object> values;
    private int customModelData;
    private Material material;
    private String name;
    List<String> lore;
    boolean broken = false;

    //parser for a generic type of property
    public ItemParser(Map<String, Object> values, String type, int numImmobili) {
        this.values = values;
        lore = new ArrayList<>();
        try {
            material = Material.valueOf((String) values.get("material"));
        } catch (IllegalArgumentException e) {
            material = Material.RED_WOOL;
            broken = true;
        }
        try {
            customModelData = (Integer) values.get("custom-model-data");
        } catch (ClassCastException e) {

            broken = true;
        }

        try {
            name = PlaceholderParser.parse(color((String) values.get("display")), "tipo", type);

        } catch (ClassCastException e) {
            broken = true;
            name = color("&c&lThe name is broken.");
        }
        try {
            List<String> lore = (List<String>) values.get("lore");
            lore.forEach(s -> this.lore.add(PlaceholderParser.parse(color(s),  "numero-immobili", String.valueOf(numImmobili))));

        } catch (ClassCastException e) {
            broken = true;
            lore.add(color("&c&lThe lore is broken."));
        }
    }

    //parser for a generic property
    public ItemParser(Map<String, Object> values, String nome, long prezzo, boolean flag) {
        this.values = values;
        lore = new ArrayList<>();
        try {
            material = Material.valueOf((String) values.get("material"));
        } catch (IllegalArgumentException e) {
            material = Material.RED_WOOL;
            broken = true;
        }
        try {
            customModelData = (Integer) values.get("custom-model-data");
        } catch (ClassCastException e) {

            broken = true;
        }

        try {
            name = PlaceholderParser.parse(color((String) values.get("display")), "nome_immobile" , nome);

        } catch (ClassCastException e) {
            broken = true;
            name = color("&c&lThe name is broken.");
        }
        try {
            List<String> lore = (List<String>) values.get("lore");
            lore.forEach(s -> this.lore.add(PlaceholderParser.parse(color(s),  "prezzo", String.valueOf(prezzo), "nome_immobile", nome)));

        } catch (ClassCastException e) {
            broken = true;
            lore.add(color("&c&lThe lore is broken."));
        }
    }

    //parser for a key
    public ItemParser(Map<String, Object> values, Property property) {
        this.values = values;
        lore = new ArrayList<>();
        try {
            material = Material.valueOf((String) values.get("material"));
        } catch (IllegalArgumentException e) {
            material = Material.RED_WOOL;
            broken = true;
        }
        try {
            customModelData = (Integer) values.get("custom-model-data");
        } catch (ClassCastException e) {

            broken = true;
        }

        try {
            name = PlaceholderParser.parse(color((String) values.get("display")), "nome_immobile" , property.getName());

        } catch (ClassCastException e) {
            broken = true;
            name = color("&c&lThe name is broken.");
        }
        try {
            List<String> lore = (List<String>) values.get("lore");
            lore.forEach(s -> this.lore.add(color(PlaceholderParser.parse(color(s), "nome_immobile" , property.getName()))));

        } catch (ClassCastException e) {
            broken = true;
            lore.add(color("&c&lThe lore is broken."));
        }
    }

    public ItemStack parse() {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}