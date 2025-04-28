package com.gestion.gestionchambre.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gestion.gestionchambre.model.Utilisateur;
import com.gestion.gestionchambre.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        utilisateur.setMdp(passwordEncoder.encode(utilisateur.getMdp()));
        return utilisateurRepository.save(utilisateur);
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur getUtilisateurById(String id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public Utilisateur getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public Utilisateur updateUtilisateur(String id, Utilisateur utilisateurDetails) {
        Utilisateur utilisateur = getUtilisateurById(id);
        utilisateur.setNom(utilisateurDetails.getNom());
        utilisateur.setPrenom(utilisateurDetails.getPrenom());
        utilisateur.setEmail(utilisateurDetails.getEmail());
        utilisateur.setRoles(utilisateurDetails.getRoles());
        return utilisateurRepository.save(utilisateur);
    }

    public void deleteUtilisateur(String id) {
        Utilisateur utilisateur = getUtilisateurById(id);
        utilisateurRepository.delete(utilisateur);
    }

    public Utilisateur updatePassword(String id, String newMdp, String ancienMdp) {
        Utilisateur utilisateur = getUtilisateurById(id);
        if (newMdp == null || ancienMdp == null) {
            throw new IllegalArgumentException("Les mots de passe ne peuvent pas être null");
        }
        if (utilisateur == null) {
            System.err.println("Utilisateur non trouvé");
            return null;
        }
        if (!verifyPassword(ancienMdp, utilisateur.getMdp())) {
            System.err.println("Ancien mot de passe incorrect");
            return null;
        }
        if (verifyPassword(newMdp, utilisateur.getMdp())) {
            System.err.println("Le nouveau mot de passe doit être différent de l'ancien");
            return null;
        }
        utilisateur.setMdp(passwordEncoder.encode(newMdp));
        System.err.println("Mot de passe changé");

        return utilisateurRepository.save(utilisateur);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
