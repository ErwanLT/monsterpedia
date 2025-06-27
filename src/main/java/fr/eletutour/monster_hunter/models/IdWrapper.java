package fr.eletutour.monster_hunter.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdWrapper {
    @JsonProperty("$oid")
    private String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
