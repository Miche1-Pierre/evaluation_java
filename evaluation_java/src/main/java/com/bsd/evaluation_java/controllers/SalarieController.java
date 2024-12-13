package com.bsd.evaluation_java.controllers;

import com.bsd.evaluation_java.dao.ConventionDao;
import com.bsd.evaluation_java.dao.SalarieDao;
import com.bsd.evaluation_java.models.Convention;
import com.bsd.evaluation_java.models.Salarie;
import com.bsd.evaluation_java.dao.UtilisateurDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/salaries")
public class SalarieController {

    @Autowired
    private SalarieDao salarieDao;

    @Autowired
    private ConventionDao conventionDao;

    @GetMapping(path = "/")
    public List<Salarie> getAllSalaries() {
        return salarieDao.findAll();
    }

    @PreAuthorize("(hasRole('ADMINISTRATEUR'))")
    @PostMapping(path = "/create")
    public ResponseEntity<Object> createSalarie(@RequestBody Salarie salarie) {
        // Validation du matricule
        if (salarie.getMatricule() == null || salarie.getMatricule().length() < 3 || salarie.getMatricule().length() > 10) {
            return new ResponseEntity<>("Le matricule doit avoir entre 3 et 10 caractères.", HttpStatus.BAD_REQUEST);
        }

        // Validation du code barre
        if (salarie.getCodeBarre() == null || salarie.getCodeBarre().isEmpty()) {
            return new ResponseEntity<>("Le code barre ne peut pas être vide.", HttpStatus.BAD_REQUEST);
        }

        // Vérification de l'existance du code barre
        if (salarieDao.findByCodeBarre(salarie.getCodeBarre()).isPresent()) {
            return new ResponseEntity<>("Un salarié avec ce code-barre existe déjà.", HttpStatus.CONFLICT);
        }

        // Vérification si une convention est associée
        if (salarie.getConvention() == null || salarie.getConvention().getId() == null) {
            return new ResponseEntity<>("La convention est obligatoire.", HttpStatus.BAD_REQUEST);
        }

        Optional<Convention> optionalConvention = conventionDao.findById(salarie.getConvention().getId());
        if (optionalConvention.isEmpty()) {
            return new ResponseEntity<>("La convention spécifiée n'existe pas.", HttpStatus.NOT_FOUND);
        }

        Convention convention = optionalConvention.get();

        // Vérification du nombre maximal de salariés dans la convention
        if (convention.getSalaries().size() >= convention.getSalarieMaximum()) {
            return new ResponseEntity<>("Le nombre maximum de salariés pour cette convention est atteint.", HttpStatus.FORBIDDEN);
        }

        // Ajout du salarié
        Salarie newSalarie = salarieDao.save(salarie);
        return new ResponseEntity<>(newSalarie, HttpStatus.CREATED);
    }

    @PreAuthorize("(hasRole('ADMINISTRATEUR'))")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSalarie(@PathVariable Integer id) {
        Optional<Salarie> salarieOptional = salarieDao.findById(id);
        if (salarieOptional.isEmpty()) {
            return new ResponseEntity<>("Salarié introuvable.", HttpStatus.NOT_FOUND);
        }

        Salarie salarie = salarieOptional.get();
        if (salarie.getConvention() != null) {
            Convention convention = salarie.getConvention();
            convention.getSalaries().remove(salarie);
            conventionDao.save(convention);
        }

        salarieDao.deleteById(id);
        return new ResponseEntity<>("Salarié supprimé avec succès.", HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("(hasRole('ADMINISTRATEUR'))")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSalarie(@RequestBody Salarie salarie, @PathVariable Integer id) {
        Optional<Salarie> existingSalarie = salarieDao.findById(id);
        if (existingSalarie.isEmpty()) {
            return new ResponseEntity<>("Salarié introuvable.", HttpStatus.NOT_FOUND);
        }

        // Validation de l'unicité du code-barre
        Optional<Salarie> salarieWithCodeBarre = salarieDao.findByCodeBarre(salarie.getCodeBarre());
        if (salarieWithCodeBarre.isPresent() && !salarieWithCodeBarre.get().getId().equals(id)) {
            return new ResponseEntity<>("Un salarié avec ce code-barre existe déjà.", HttpStatus.CONFLICT);
        }

        // Validation de la convention si modifiée
        if (salarie.getConvention() != null && salarie.getConvention().getId() != null) {
            Optional<Convention> optionalConvention = conventionDao.findById(salarie.getConvention().getId());
            if (optionalConvention.isEmpty()) {
                return new ResponseEntity<>("La convention spécifiée n'existe pas.", HttpStatus.NOT_FOUND);
            }
        }

        salarie.setId(id);
        Salarie updatedSalarie = salarieDao.save(salarie);
        return new ResponseEntity<>(updatedSalarie, HttpStatus.OK);
    }
}