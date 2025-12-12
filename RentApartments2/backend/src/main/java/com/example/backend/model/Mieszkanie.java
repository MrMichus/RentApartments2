package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;



@Entity
@Table(name="mieszkanie")
public class Mieszkanie {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String tytul;

    @Column(nullable=false)
    private String opis;

    @Column(nullable=false)
    private Double cena_miesieczna;

    @Column(nullable=false)
    private Double powierzchnia;

    @Column(nullable=false)
    private Integer liczba_pokoi;

    @Column(nullable=false)
    private String status = "PENDING";

    //wlasciciel mieszkania
    @ManyToOne(optional = false)
    @JoinColumn(name="owner_id")
    private User owner;

    //adres mieszkania
    @ManyToOne(optional = false)
    @JoinColumn(name = "adres_id")
    private Adres adres;

    public Mieszkanie(){}

    public Long getId(){return id;}

    public void setId(Long id){this.id=id;}

    public String getTytul(){return tytul;}

    public void setTytul(String tytul){this.tytul=tytul;}

    public String getOpis(){return opis;}

    public void setOpis(String opis){this.opis=opis;}

    public Double getCena_miesieczna(){return cena_miesieczna;}

    public void setCena_miesieczna(Double cena_miesieczna){this.cena_miesieczna=cena_miesieczna; }

    public Double getPowierzchnia(){return powierzchnia;}

    public void setPowierzchnia(Double powierzchnia){this.powierzchnia=powierzchnia;}

    public Integer getLiczba_pokoi(){return liczba_pokoi;}

    public void setLiczba_pokoi(Integer liczba_pokoi){this.liczba_pokoi=liczba_pokoi;}

    public String getStatus(){return status;}

    public void setStatus(String status){this.status=status;}

    public User getOwner(){return owner;}

    public void setOwner(User owner){this.owner=owner;}

    public Adres getAdres(){return adres;}

    public void setAdres(Adres adres){this.adres=adres;}

}
