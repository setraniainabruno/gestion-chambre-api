package com.gestion.gestionchambre.service;


import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import com.gestion.gestionchambre.model.Client;
import com.gestion.gestionchambre.repository.ClientRepository;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(String id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(String id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        
        client.setNom(clientDetails.getNom());
        client.setPrenom(clientDetails.getPrenom());
        client.setEmail(clientDetails.getEmail());
        client.setTelephone(clientDetails.getTelephone());
        client.setAdresse(clientDetails.getAdresse());
        client.setNumeroPieceIdentite(clientDetails.getNumeroPieceIdentite());
        client.setTypePieceIdentite(clientDetails.getTypePieceIdentite());
        
        return clientRepository.save(client);
    }

    public void deleteClient(String id) {
        clientRepository.deleteById(id);
    }

}