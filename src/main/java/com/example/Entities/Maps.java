package com.example.Entities;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="Maps")
public class Maps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maps_id")
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private int idRegion;

    @Column(nullable = false)
    private Long positive_value;

    @Column(nullable = false)
    private Long negative_value;

    @Column(name = "last_update", nullable = false)
    private Timestamp lastUpdate;

    public Maps(String firstName, int idRegion, Long positive_value, Long negative_value) {
        this.firstName = firstName;
        this.idRegion = idRegion;
        this.positive_value = positive_value;
        this.negative_value = negative_value;
    }

    public Maps() {

    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public void setPositive_value(Long positive_value) {
        this.positive_value = positive_value;
    }

    public void setNegative_value(Long negative_value) {
        this.negative_value = negative_value;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public Long getPositive_value() {
        return positive_value;
    }

    public Long getNegative_value() {
        return negative_value;
    }
}
