package com.bsd.evaluation_java.dao;

import com.bsd.evaluation_java.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurDao extends JpaRepository<Utilisateur, Integer> {
    Utilisateur findByEmail(String email);
}