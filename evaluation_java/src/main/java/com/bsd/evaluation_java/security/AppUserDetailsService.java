package com.bsd.evaluation_java.security;

import com.bsd.evaluation_java.dao.UtilisateurDao;
import com.bsd.evaluation_java.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    UtilisateurDao utilisateurDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurDao.findByEmail(email);
        if (utilisateur == null) {
            throw new UsernameNotFoundException("Email introuvable : " + email);
        }

        String role = utilisateur.getEntreprise() != null ? "ENTREPRISE" : "ADMINISTRATEUR";

        return new AppUserDetails(utilisateur, role);
    }
}
