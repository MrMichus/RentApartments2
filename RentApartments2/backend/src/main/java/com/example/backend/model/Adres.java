package com.example.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "adresy")
public class Adres{


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String ulica;

    @Column(nullable=false)
    private String numer;

    @Column(name = "kod_pocztowy", nullable=false)
    private String kodPocztowy;

    @Column(nullable=false)
    private String miasto;


    @OneToMany(mappedBy = "adres")
    private List<Mieszkanie> mieszkania = new ArrayList<>();

    public Adres(){}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUlica() {
        return ulica;
    }
    public void setUlica(String ulica) {
        this.ulica = ulica;
    }
    public String getNumer() {
        return numer;
    }
    public void setNumer(String numer) {
        this.numer = numer;
    }
    public String getKodPocztowy() {
        return kodPocztowy;
    }
    public void setKodPocztowy(String kodPocztowy) {
        this.kodPocztowy = kodPocztowy;
    }
    public String getMiasto() {
        return miasto;
    }
    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public List<Mieszkanie> getMieszkania() {
        return mieszkania;
    }

    public void setMieszkania(List<Mieszkanie> mieszkania) {
        this.mieszkania = mieszkania;
    }








}
