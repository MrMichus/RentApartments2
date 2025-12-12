package com.example.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MieszkanieRequest {
    
    @NotBlank(message = "Tytuł jest wymagany")
    private String tytul;
    
    @NotBlank(message = "Opis jest wymagany")
    private String opis;
    
    @NotNull(message = "Cena miesięczna jest wymagana")
    @Positive(message = "Cena musi być większa od 0")
    private Double cenaMiesieczna;
    
    @NotNull(message = "Powierzchnia jest wymagana")
    @Positive(message = "Powierzchnia musi być większa od 0")
    private Double powierzchnia;
    
    @NotNull(message = "Liczba pokoi jest wymagana")
    @Positive(message = "Liczba pokoi musi być większa od 0")
    private Integer liczbaPokoi;
    
    // Adres
    @NotBlank(message = "Ulica jest wymagana")
    private String ulica;
    
    @NotBlank(message = "Numer jest wymagany")
    private String numer;
    
    @NotBlank(message = "Kod pocztowy jest wymagany")
    private String kodPocztowy;
    
    @NotBlank(message = "Miasto jest wymagane")
    private String miasto;
    
    // Lista URL-i zdjęć (opcjonalna)
    private List<String> zdjeciaUrls;

    public MieszkanieRequest() {}

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

    public String getUlica() { return ulica; }
    public void setUlica(String ulica) { this.ulica = ulica; }

    public String getNumer() { return numer; }
    public void setNumer(String numer) { this.numer = numer; }

    public String getKodPocztowy() { return kodPocztowy; }
    public void setKodPocztowy(String kodPocztowy) { this.kodPocztowy = kodPocztowy; }

    public String getMiasto() { return miasto; }
    public void setMiasto(String miasto) { this.miasto = miasto; }

    public List<String> getZdjeciaUrls() { return zdjeciaUrls; }
    public void setZdjeciaUrls(List<String> zdjeciaUrls) { this.zdjeciaUrls = zdjeciaUrls; }
}
