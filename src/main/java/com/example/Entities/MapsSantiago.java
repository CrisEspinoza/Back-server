package com.example.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="MapsSantiago")
public class MapsSantiago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapsSantiago_id")
    @JsonIgnore
    private Long idComuna;

    @Column(name="id_comuna",nullable = false)
    private int id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private Long positive_value;

    @Column(nullable = false)
    private Long negative_value;

    @Column(name = "last_update", nullable = false)
    private Timestamp lastUpdate;

    public Long getIdComuna() {
        return idComuna;
    }

    public void setIdComuna(Long idComuna) {
        this.idComuna = idComuna;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getPositive_value() {
        return positive_value;
    }

    public void setPositive_value(Long positive_value) {
        this.positive_value = positive_value;
    }

    public Long getNegative_value() {
        return negative_value;
    }

    public void setNegative_value(Long negative_value) {
        this.negative_value = negative_value;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
