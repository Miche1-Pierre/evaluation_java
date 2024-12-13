package com.bsd.evaluation_java.dao;

import com.bsd.evaluation_java.models.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepriseDao extends JpaRepository<Entreprise, Integer> {
}