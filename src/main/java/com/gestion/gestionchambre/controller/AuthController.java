package com.gestion.gestionchambre.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.gestionchambre.model.Utilisateur;
import com.gestion.gestionchambre.security.JwtUtils;
import com.gestion.gestionchambre.service.UtilisateurService;


@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String mdp = loginRequest.get("mdp");
        if (email == null || mdp == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Email et mot de passe sont requis"
            ));
        }

        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);

        if (utilisateur == null || !utilisateurService.verifyPassword(mdp, utilisateur.getMdp())) {
            System.err.println("Mot de passe incorrecte");
            return null;
        }

        String token = jwtUtils.generateToken(email);

        System.out.println("Connexion réussite");
        
        return ResponseEntity.ok(Map.of(
                "token", token,
                "tokenType", "Bearer",
                "expiresIn", jwtUtils.getExpiration(),
                "utilisateur", Map.of(
                        "id", utilisateur.getId(),
                        "nom", utilisateur.getNom(),
                        "prenom", utilisateur.getPrenom(),
                        "email", utilisateur.getEmail(),
                        "roles", utilisateur.getRoles()
                )
        ));
    }
}
