package org.metadevs.agenziaimmobiliare.files;

import org.metadevs.agenziaimmobiliare.AgenziaImmobiliare;

public class StructureHandler extends FileHandler<AgenziaImmobiliare> {
    /**
     * Constructor that calls the setup method
     *
     * @param plugin
     *
     */
    public StructureHandler(AgenziaImmobiliare plugin) {
        super(plugin, "structures");

    }
}
