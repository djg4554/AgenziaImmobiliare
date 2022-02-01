package org.metadevs.agenziaimmobiliare.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;
import org.metadevs.agenziaimmobiliare.structure.Property;

public class WorldGuardManager {

    private AgenziaImmobiliare plugin;
    private WorldGuard worldGuard;

    public WorldGuardManager(AgenziaImmobiliare plugin) {
        this.plugin = plugin;
        this.worldGuard = WorldGuard.getInstance();
    }


    public boolean regionExists(World world, String regionId) {
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(world);
        return worldGuard.getPlatform().getRegionContainer().get(adaptedWorld).hasRegion(regionId);


    }

    public ApplicableRegionSet getRegionsFromLocation(Location location) {
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(location.getWorld());
        return worldGuard.getPlatform().getRegionContainer().get(world).getApplicableRegions(BukkitAdapter.asBlockVector(location));



    }

    public boolean isOwnerInteracting(Player player, Property property) {
        return property.getOwner() != null &&  plugin.getKeyManager().hasKeyInInventory(player, property);
    }
}
