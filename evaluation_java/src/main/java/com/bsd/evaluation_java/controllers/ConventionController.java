package com.bsd.evaluation_java.controllers;

import com.bsd.evaluation_java.models.Convention;
import com.bsd.evaluation_java.dao.ConventionDao;
import com.bsd.evaluation_java.models.Entreprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/conventions")
public class ConventionController {

    @Autowired
    private ConventionDao conventionDao;

    @GetMapping(path = "/")
    public List<Convention> getAllConventions() {
        return conventionDao.findAll();
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PostMapping(path = "/create")
    public Convention createConvention(@RequestBody Convention convention) {
        return conventionDao.save(convention);
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConvention(@PathVariable Integer id) {
        Optional<Convention> conventionOptional = conventionDao.findById(id);

        if (conventionOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        conventionDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMINISTRATEUR') or (hasRole('ENTREPRISE') and #convention.entreprise.id == authentication.principal.entreprise.id)")
    @PutMapping(path = "/{id}")
    public ResponseEntity<Convention> updateConvention(@PathVariable Integer id, @RequestBody Convention convention) {
        Optional<Convention> conventionOptional = conventionDao.findById(id);

        if (conventionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        convention.setId(id);
        conventionDao.save(convention);

        return ResponseEntity.noContent().build();
    }
}
