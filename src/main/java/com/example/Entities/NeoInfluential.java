package com.example.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name="NeoInfluential")
public class NeoInfluential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "neo_id")
    @JsonIgnore
    private Long idUser;

    @Column(nullable = false)
    private Long statistic_x;

    @Column(nullable = false)
    private Long statistic_y;

    @Column(nullable = false)

    private Long statistic_r;

    @Column(nullable = false)
    private String firstNameUser;

    @Column(nullable = false)
    private String firstNameClub;

    @Column(nullable = false)
    private Long positive_value;

    @Column(nullable = false)
    private Long negative_value;

    @Column(name = "last_update", nullable = false)
    private Timestamp lastUpdate;

    public Long getIdUser() { return idUser;}

    public void setIdUser(Long idUser) { this.idUser = idUser;}

    public String getFirstNameUser() { return firstNameUser;}

    public Long getStatistic_x() {return statistic_x;}

    public void setStatistic_x(Long statistic_x) {this.statistic_x = statistic_x;}

    public Long getStatistic_y() {return statistic_y;}

    public void setStatistic_y(Long statistic_y) {this.statistic_y = statistic_y;}

    public Long getStatistic_r() { return statistic_r;}

    public void setStatistic_r(Long statistic_r) {this.statistic_r = statistic_r;}

    public void setFirstNameUser(String firstNameUser) {this.firstNameUser = firstNameUser;}

    public String getFirstNameClub() {return firstNameClub;}

    public void setFirstNameClub(String firstNameClub) {this.firstNameClub = firstNameClub;}

    public Long getPositive_value() {return positive_value;}

    public void setPositive_value(Long positive_value) {this.positive_value = positive_value;}

    public Long getNegative_value() {return negative_value;}

    public void setNegative_value(Long negative_value) {this.negative_value = negative_value; }

    public Timestamp getLastUpdate() {return lastUpdate;}

    public void setLastUpdate(Timestamp lastUpdate) {this.lastUpdate = lastUpdate;}
}
