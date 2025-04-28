package com.gestion.gestionchambre.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import com.gestion.gestionchambre.model.Client;

public interface ClientRepository extends MongoRepository<Client, String> {
    List<Client> findByNom(String nom);
    List<Client> findByPrenom(String prenom);
    Client findByEmail(String email);
    Client findByTelephone(String telephone);
}