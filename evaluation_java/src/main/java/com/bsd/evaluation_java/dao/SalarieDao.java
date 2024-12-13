package com.bsd.evaluation_java.dao;

import com.bsd.evaluation_java.models.Salarie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalarieDao extends JpaRepository<Salarie, Integer> {
    Optional<Salarie> findByCodeBarre(String codeBarre);
}