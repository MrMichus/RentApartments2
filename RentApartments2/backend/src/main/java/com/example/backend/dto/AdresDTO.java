package com.example.backend.dto;

public class AdresDTO {
    
    private Long id;
    private String ulica;
    private String numer;
    private String kodPocztowy;
    private String miasto;

    public AdresDTO() {}

    public AdresDTO(Long id, String ulica, String numer, String kodPocztowy, String miasto) {
        this.id = id;
        this.ulica = ulica;
        this.numer = numer;
        this.kodPocztowy = kodPocztowy;
        this.miasto = miasto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUlica() { return ulica; }
    public void setUlica(String ulica) { this.ulica = ulica; }

    public String getNumer() { return numer; }
    public void setNumer(String numer) { this.numer = numer; }

    public String getKodPocztowy() { return kodPocztowy; }
    public void setKodPocztowy(String kodPocztowy) { this.kodPocztowy = kodPocztowy; }

    public String getMiasto() { return miasto; }
    public void setMiasto(String miasto) { this.miasto = miasto; }
}
