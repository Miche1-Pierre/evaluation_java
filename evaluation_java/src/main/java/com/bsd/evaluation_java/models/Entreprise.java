package com.bsd.evaluation_java.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String nom;

    @OneToMany(mappedBy = "entreprise")
    private List<Utilisateur> utilisateurs;

    @OneToMany(mappedBy = "entreprise")
    private List<Salarie> salaries;

    @OneToMany(mappedBy = "entreprise")
    private List<Convention> conventions;


    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public List<Salarie> getSalaries() {
        return salaries;
    }

    public void setSalaries(List<Salarie> salaries) {
        this.salaries = salaries;
    }

    public List<Convention> getConventions() {
        return conventions;
    }

    public void setConventions(List<Convention> conventions) {
        this.conventions = conventions;
    }
}