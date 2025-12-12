package com.example.backend.dto;

import java.util.List;

public class MieszkanieDTO {
    
    private Long id;
    private String tytul;
    private String opis;
    private Double cenaMiesieczna;
    private Double powierzchnia;
    private Integer liczbaPokoi;
    private String status;
    private AdresDTO adres;
    private UserDTO owner;
    private List<String> zdjeciaUrls;

    public MieszkanieDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTytul() { return tytul; }
    public void setTytul(String tytul) { this.tytul = tytul; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    public Double getCenaMiesieczna() { return cenaMiesieczna; }
    public void setCenaMiesieczna(Double cenaMiesieczna) { this.cenaMiesieczna = cenaMiesieczna; }

    public Double getPowierzchnia() { return powierzchnia; }
    public void setPowierzchnia(Double powierzchnia) { this.powierzchnia = powierzchnia; }

    public Integer getLiczbaPokoi() { return liczbaPokoi; }
    public void setLiczbaPokoi(Integer liczbaPokoi) { this.liczbaPokoi = liczbaPokoi; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public AdresDTO getAdres() { return adres; }
    public void setAdres(AdresDTO adres) { this.adres = adres; }

    public UserDTO getOwner() { return owner; }
    public void setOwner(UserDTO owner) { this.owner = owner; }

    public List<String> getZdjeciaUrls() { return zdjeciaUrls; }
    public void setZdjeciaUrls(List<String> zdjeciaUrls) { this.zdjeciaUrls = zdjeciaUrls; }
}
