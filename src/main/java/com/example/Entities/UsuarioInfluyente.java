package com.example.Entities;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Influyentes")
public class UsuarioInfluyente  implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_influyentes")
    private Long idInfluyente;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double followers;
    @Column(nullable = false)
    private double  cantidadPositivos;

    @Column(nullable = false)
    private double  cantidadNegativos;

    @Column(nullable = false)
    private double influencia;
    @Column(nullable = false)
    private String razon;

    public double getCantidadPositivos() {
        return cantidadPositivos;
    }

    public void setCantidadPositivos(double cantidadPositivos) {
        this.cantidadPositivos = cantidadPositivos;
    }

    public double getCantidadNegativos() {
        return cantidadNegativos;
    }

    public void setCantidadNegativos(double cantidadNegativos) {
        this.cantidadNegativos = cantidadNegativos;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
//    public UsuarioInfluyente(String name, double seguidores, double cantidad, double inf) {
//
//        this.name=name;
//        this.followers=seguidores;
//        this.cantidad=cantidad;
//        this.influencia=inf;
//
//    }


    public Long getIdInfluyente() {
        return idInfluyente;
    }

    public void setIdInfluyente(Long idInfluyente) {
        this.idInfluyente = idInfluyente;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFollowers() {
        return followers;
    }

    public void setFollowers(double followers) {
        this.followers = followers;
    }



    public double getInfluencia() {
        return influencia;
    }

    public void setInfluencia(double influencia) {
        this.influencia = influencia;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    private static final long serialVersionUID = 1L;
}
