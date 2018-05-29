package com.example.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Keyword")
public class Keyword implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long id;

    @Column(nullable = false)
    private String name_keyword;

    public Keyword() {
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName_keyword() {
        return name_keyword;
    }

    public void setName_keyword(String name_keyword) {
        this.name_keyword = name_keyword;
    }


}
