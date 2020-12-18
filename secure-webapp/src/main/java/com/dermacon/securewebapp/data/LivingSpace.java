package com.dermacon.securewebapp.data;


import org.hibernate.annotations.Cascade;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


@Entity
public class LivingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long livingSpaceId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bedroom_id")
    private Room bedroom;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kitchen_id")
    private Room kitchen;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bathroom_id")
    private Room bathroom;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "storage_id")
    private Room storage;

    public LivingSpace() {}

    public LivingSpace(Room bedroom, Room kitchen, Room bathroom, Room storage) {
        this.bedroom = bedroom;
        this.kitchen = kitchen;
        this.bathroom = bathroom;
        this.storage = storage;
    }

    public long getLivingSpaceId() {
        return livingSpaceId;
    }

    public void setLivingSpaceId(long livingSpaceId) {
        this.livingSpaceId = livingSpaceId;
    }

    public Room getBedroom() {
        return bedroom;
    }

    public void setBedroom(Room bedroom) {
        this.bedroom = bedroom;
    }

    public Room getKitchen() {
        return kitchen;
    }

    public void setKitchen(Room kitchen) {
        this.kitchen = kitchen;
    }

    public Room getBathroom() {
        return bathroom;
    }

    public void setBathroom(Room bathroom) {
        this.bathroom = bathroom;
    }

    public Room getStorage() {
        return storage;
    }

    public void setStorage(Room storage) {
        this.storage = storage;
    }

    @Override
    public String toString() {
        return "LivingSpace{" +
                "livingSpaceId=" + livingSpaceId +
                ", bedroom=" + bedroom +
                ", kitchen=" + kitchen +
                ", bathroom=" + bathroom +
                ", storage=" + storage +
                '}';
    }
}
