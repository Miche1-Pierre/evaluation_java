package com.bsd.evaluation_java.controllers;

import com.bsd.evaluation_java.dao.UtilisateurDao;
import com.bsd.evaluation_java.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurDao utilisateurDao;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping("/")
    public ResponseEntity<List<Utilisateur>> getAllUsers() {
        List<Utilisateur> utilisateurs = utilisateurDao.findAll();
        if (utilisateurs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);
        if (optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>("Utilisateur introuvable.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalUtilisateur.get(), HttpStatus.OK);
    }

    @PreAuthorize("(hasRole('ADMINISTRATEUR'))")
    @PostMapping("/")
    public ResponseEntity<Object> createUser(@RequestBody Utilisateur utilisateur) {
        // Vérification de l'existence de l'email
        Utilisateur utilisateurAvecEmail = utilisateurDao.findByEmail(utilisateur.getEmail());
        if (utilisateurAvecEmail != null) {
            return new ResponseEntity<>("Un utilisateur avec cet email existe déjà.", HttpStatus.CONFLICT);
        }

        // Encodage du mot de passe
        if (utilisateur.getPassword() == null || utilisateur.getPassword().isEmpty()) {
            return new ResponseEntity<>("Le mot de passe est obligatoire.", HttpStatus.BAD_REQUEST);
        }
        utilisateur.setPassword(encoder.encode(utilisateur.getPassword()));

        utilisateur.setId(null);
        Utilisateur savedUser = utilisateurDao.save(utilisateur);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PreAuthorize("(hasRole('ADMINISTRATEUR')) or (hasRole('ENTREPRISE'))")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody Utilisateur utilisateur, @PathVariable Integer id) {
        Optional<Utilisateur> existingUtilisateur = utilisateurDao.findById(id);
        if (existingUtilisateur.isEmpty()) {
            return new ResponseEntity<>("Utilisateur introuvable.", HttpStatus.NOT_FOUND);
        }

        // Si un mot de passe est fourni, le crypter, sinon garder l'ancien
        if (utilisateur.getPassword() != null && !utilisateur.getPassword().isEmpty()) {
            utilisateur.setPassword(encoder.encode(utilisateur.getPassword()));
        } else {
            utilisateur.setPassword(existingUtilisateur.get().getPassword());
        }

        utilisateur.setId(id);
        Utilisateur updatedUser = utilisateurDao.save(utilisateur);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PreAuthorize("(hasRole('ADMINISTRATEUR'))")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer id) {
        Optional<Utilisateur> existingUtilisateur = utilisateurDao.findById(id);
        if (existingUtilisateur.isEmpty()) {
            return new ResponseEntity<>("Utilisateur introuvable.", HttpStatus.NOT_FOUND);
        }
        utilisateurDao.deleteById(id);
        return new ResponseEntity<>("Utilisateur supprimé avec succès.", HttpStatus.NO_CONTENT);
    }
}
