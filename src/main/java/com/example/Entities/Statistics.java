package com.example.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="statistics")
public class Statistics implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistics_id")
    private Long id;

    @Column(nullable = false)
    private String name_statics;

    @Column(nullable = false)
    private int positive_value;

    @Column(nullable = false)
    private int negative_value;

    @Column(nullable = false)
    private int neutro_value;

    @Column(name = "last_update", nullable = false)
    private Timestamp lastUpdate;

    @ManyToOne
    @JoinColumn(name="club_id")
    private Club club;

    public Statistics() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName_statics(String name_statics) {
        this.name_statics = name_statics;
    }

    public void setPositive_value(int positive_value) {
        this.positive_value = positive_value;
    }

    public void setNegative_value(int negative_value) {
        this.negative_value = negative_value;
    }

    public void setNeutro_value(int neutro_value) {
        this.neutro_value = neutro_value;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getId() {
        return id;
    }

    public String getName_statics() {
        return name_statics;
    }

    public int getPositive_value() {
        return positive_value;
    }

    public int getNegative_value() {
        return negative_value;
    }

    public int getNeutro_value() {
        return neutro_value;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
}