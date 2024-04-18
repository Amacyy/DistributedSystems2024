package com.api.rest;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="employee")
@XmlType(propOrder = {"id", "name", "age", "sector","building"})
public class Employee {

    private int id,age,sector;
    private String name, building;
    
    public Employee(int id, String name, int age, int sector, String building) {
        super();
        this.id = id;
        this.name=name;
        this.age=age;
        this.sector=sector;
        this.building=building;
    }
    
    public Employee(int id, String name,int age, int sector) {
        this.id = id;
        this.name=name;
        this.age=age;
        this.sector=sector;
    }
    
    public Employee(String name,int age) {
        this.name=name;
        this.age=age;
    }
    
    public Employee() {
        
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id=id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public int getSector() {
        return sector;
    }
    
    public void setSector(int sector) {
        this.sector = sector;
    }
    
    public String getBuilding() {
        return building;
    }
    
    public void setBuilding(String building) {
        this.building = building;
    }
    
    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", age=" + age + ", sector=" + sector + ", building=" + building
                + "]";
    }
}