package com.gestion.gestionchambre.repository;

import com.gestion.gestionchambre.model.Utilisateur;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UtilisateurRepository extends MongoRepository<Utilisateur, String> {
    Optional<Utilisateur> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<Utilisateur> findByNomAndPrenom(String nom, String prenom);
}