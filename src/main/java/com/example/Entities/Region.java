package com.example.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Region")
public class Region implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @OneToMany(mappedBy="region")
    @JsonIgnore
    private List<Commune> Commune;

    @Column(nullable = false)
    private int idMaps;

    public int getIdMaps() {
        return idMaps;
    }

    public void setIdMaps(int idMaps) {
        this.idMaps = idMaps;
    }

    public Region() {
    }

    public List<Commune> getCommune() {
        return Commune;
    }

    public void setCommune( List<Commune> commune) {
        Commune = commune;
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
