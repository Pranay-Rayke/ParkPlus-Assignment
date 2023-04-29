package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String address;
    @OneToMany(mappedBy = "parkingLot",cascade = CascadeType.ALL)
    private List<Spot> spotList = new ArrayList<>();

    public ParkingLot(){}

    public ParkingLot(String name,String address){
        setAddress(address);
        setName(name);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSpotList(List<Spot> spotList) {
        this.spotList = spotList;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Spot> getSpotList() {
        return spotList;
    }

    public String getAddress() {
        return address;
    }


}