package com.bsd.evaluation_java.controllers;

import com.bsd.evaluation_java.models.Entreprise;
import com.bsd.evaluation_java.dao.EntrepriseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/entreprises")
public class EntrepriseController {

    @Autowired
    private EntrepriseDao entrepriseDao;

    @GetMapping(path = "/")
    public List<Entreprise> getAllEntreprises() {
        return entrepriseDao.findAll();
    }

    @PostMapping(path = "/create")
    public Entreprise createEntreprise(@RequestBody Entreprise entreprise) {
        return entrepriseDao.save(entreprise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntreprise(@PathVariable Integer id) {
        Optional<Entreprise> entrepriseOptional = entrepriseDao.findById(id);

        if (entrepriseOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        entrepriseDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entreprise> updateEntreprise(@RequestBody Entreprise entreprise, @PathVariable Integer id) {
        Optional<Entreprise> existingEntreprise = entrepriseDao.findById(id);

        if (existingEntreprise.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        entreprise.setId(id);
        entrepriseDao.save(entreprise);

        return new ResponseEntity<>(entreprise, HttpStatus.OK);
    }
}
