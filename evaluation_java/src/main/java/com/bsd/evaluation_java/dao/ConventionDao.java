package com.bsd.evaluation_java.dao;

import com.bsd.evaluation_java.models.Convention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConventionDao extends JpaRepository<Convention, Integer> {
}