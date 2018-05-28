package com.example.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Commune")
public class Commune implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commune_id")
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="region_id")
    private Region region;

    @OneToMany(mappedBy="comuna")
    @JsonIgnore
    private Set<Club> Club;

    public Commune() {
    }

    public void setClub(Set<com.example.Entities.Club> club) {
        Club = club;
    }

    public Set<com.example.Entities.Club> getClub() {
        return Club;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

}