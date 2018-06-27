package com.example.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="NeoInfluential")
public class NeoInfluential implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "neo_id")
    @JsonIgnore
    private Long id;

    @Column()
    private int statistic_x;

    @Column()
    private int statistic_y;

    @Column()

    private float statistic_r;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name ="influyente_id")
    private List<UsuarioInfluyente> usuariosInfluyentes;

    public NeoInfluential() {
        this.usuariosInfluyentes = new ArrayList<UsuarioInfluyente>();
    }

    @Column(name = "last_update")
    private Timestamp lastUpdate;

    public Long getIdNeo() {
        return id;
    }

    public void setIdNeo(Long idNeo) {
        this.id = idNeo;
    }

    public int getStatistic_x() {
        return statistic_x;
    }

    public void setStatistic_x(int statistic_x) {
        this.statistic_x = statistic_x;
    }

    public int getStatistic_y() {
        return statistic_y;
    }

    public void setStatistic_y(int statistic_y) {
        this.statistic_y = statistic_y;
    }

    public float getStatistic_r() {
        return statistic_r;
    }

    public void setStatistic_r(float statistic_r) {
        this.statistic_r = statistic_r;
    }

    public List<UsuarioInfluyente> getUsuariosInfluyentes() {
        return usuariosInfluyentes;
    }

    public void setUsuariosInfluyentes(List<UsuarioInfluyente> usuariosInfluyentes) {
        this.usuariosInfluyentes = usuariosInfluyentes;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
