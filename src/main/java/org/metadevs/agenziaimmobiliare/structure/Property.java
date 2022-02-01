package org.metadevs.agenziaimmobiliare.structure;

import java.io.*;
import java.util.Base64;
import java.util.UUID;

public class Property implements Serializable {

    private String name;
    private String regionId;
    private String type;
    private State state;
    private UUID owner;
    private long price;


    public String getName() {
        return name;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getType() {
        return type;
    }

    public State getState() {
        return state;
    }

    public UUID getOwner() {
        return owner;
    }

    public long getPrice() {
        return price;
    }

    public Property(String name, String regionId, String type, State state, String proprietario, long price) {
        this.name = name;
        this.regionId = regionId;
        this.type = type;
        this.state = state;
        if (state == State.OWNED) {
            this.owner = UUID.fromString(proprietario);
        } else {
            this.owner = null;
        }
        this.price = price;

    }

    public String serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }


    public static Property deserialize(String serialized) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(serialized);
        ObjectInputStream ois = new ObjectInputStream(new java.io.ByteArrayInputStream(data));
        Property p = (Property) ois.readObject();
        ois.close();
        return p;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setOwner(UUID uniqueId) {
        this.owner = uniqueId;
    }
}
