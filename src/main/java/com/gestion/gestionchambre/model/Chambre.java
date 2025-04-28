package com.gestion.gestionchambre.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chambres")
public class Chambre {

    @Id
    private String id;
    private String numero;
    private ChambreType type;
    private int etage;
    private double prix;
    private int capacite;
    private ChambreStatus statut;
    private String description;

    // Constructors, getters, and setters
    public Chambre() {
    }

    // Getters and setters for all fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public ChambreType getType() {
        return type;
    }

    public void setType(ChambreType type) {
        this.type = type;
    }

    public int getEtage() {
        return etage;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public ChambreStatus getStatut() {
        return statut;
    }

    public void setStatut(ChambreStatus statut) {
        this.statut = statut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
