package org.metadevs.agenziaimmobiliare.key;

import org.metadevs.agenziaimmobiliare.structure.Property;

import java.util.UUID;

public class Key {
    private Property property;

    public Key(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }

    public boolean isOwner(UUID uniqueId) {
        return property.getOwner()!= null && property.getOwner().equals(uniqueId);
    }
}
