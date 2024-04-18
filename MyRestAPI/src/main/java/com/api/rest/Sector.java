package com.api.rest;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "sector")
@XmlType(propOrder = {"id", "building"})
public class Sector {

    private int id;

    private String building;

    public Sector(int id, String building) {
    	super();
        this.id = id;
        
        this.building = building;
    }

    public Sector() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    @Override
    public String toString() {
        return "Sector [id=" + id + ", name=" + ", building=" + building + "]";
    }
}
