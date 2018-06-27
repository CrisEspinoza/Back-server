package com.example.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Club")
public class Club implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private int trophies;

    @Column(name = "last_update", nullable = false)
    private Timestamp lastUpdate;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name ="club_id")
    private List<Statistics> statistics;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name ="club_id")
    private Set<Keyword> keywords;



    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval=true,optional=false)
    @JoinColumn(name ="neoInf_id")
    @JsonIgnore
    private NeoInfluential neonInfluential;



    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="commune_id")
    private Commune comuna;

    public Club() { }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public Commune getComuna() {
        return comuna;
    }

    public void setComuna(Commune comuna) {
        this.comuna = comuna;
    }

    public List<Statistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<Statistics> statistics) {
        this.statistics = statistics;
    }

    public void setKeywords(Set<Keyword> keywords) {
        this.keywords = keywords;
    }

    public Set<Keyword> getKeywords() {
        return keywords;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTrophies(int trophies) {
        this.trophies = trophies;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public int getTrophies() {
        return trophies;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    private static final long serialVersionUID = 1L;

    public NeoInfluential getNeonInfluential() {
        return neonInfluential;
    }

    public void setNeonInfluential(NeoInfluential neonInfluential) {
        this.neonInfluential = neonInfluential;
    }
}

