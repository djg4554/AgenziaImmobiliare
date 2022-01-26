package org.metadevs.agenziaimmobiliare.structure;

public class Property {

    private String id;
    private String name;
    private String regionId;
    private String type;
    private State state;
    private long price;


    public Property(String id, String name, String regionId, String type, State state, long price) {
        this.id = id;
        this.name = name;
        this.regionId = regionId;
        this.type = type;
        this.state = state;
        this.price = price;
    }

}
