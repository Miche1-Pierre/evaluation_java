package com.bsd.evaluation_java.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Convention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    @PositiveOrZero
    private float subvention;

    @Min(1)
    private int salarieMaximum;

    @OneToMany(mappedBy = "convention")
    private List<Salarie> salaries;

    @ManyToOne
    private Entreprise entreprise;


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

    public float getSubvention() {
        return subvention;
    }

    public void setSubvention(float subvention) {
        this.subvention = subvention;
    }

    public int getSalarieMaximum() {
        return salarieMaximum;
    }

    public void setSalarieMaximum(int salarieMaximum) {
        this.salarieMaximum = salarieMaximum;
    }

    public List<Salarie> getSalaries() {
        return salaries;
    }

    public void setSalaries(List<Salarie> salaries) {
        this.salaries = salaries;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }
}