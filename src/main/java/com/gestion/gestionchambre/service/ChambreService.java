package com.gestion.gestionchambre.service;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import com.gestion.gestionchambre.model.Chambre;
import com.gestion.gestionchambre.model.ChambreStatus;
import com.gestion.gestionchambre.repository.ChambreRepository;

@Service
public class ChambreService {

    private final ChambreRepository chambreRepository;

    
    public ChambreService(ChambreRepository chambreRepository) {
        this.chambreRepository = chambreRepository;
    }

    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    public Optional<Chambre> getChambreById(String id) {
        return chambreRepository.findById(id);
    }

    public int getCountChambresByStatus(ChambreStatus status) {
        return chambreRepository.findByStatut(status).size();
    }

    public List<Chambre> getAvailableChambres() {
        return chambreRepository.findByStatut(ChambreStatus.Disponible);
    }

    public Chambre createChambre(Chambre chambre) {
        if (chambre.getStatut().equals(ChambreStatus.Occupée) || chambre.getStatut().equals(ChambreStatus.Réservée)) {
            chambre.setStatut(ChambreStatus.Disponible);
        }
        return chambreRepository.save(chambre);
    }

    public Chambre updateChambre(String id, Chambre chambreDetails) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre not found with id: " + id));

        chambre.setNumero(chambreDetails.getNumero());
        chambre.setType(chambreDetails.getType());
        chambre.setEtage(chambreDetails.getEtage());
        chambre.setPrix(chambreDetails.getPrix());
        chambre.setCapacite(chambreDetails.getCapacite());
        chambre.setStatut(chambreDetails.getStatut());
        chambre.setDescription(chambreDetails.getDescription());

        return chambreRepository.save(chambre);
    }

    public Chambre updateChambreStatus(String id, ChambreStatus statut) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre not found with id: " + id));
        chambre.setStatut(statut);
        return chambreRepository.save(chambre);
    }

    public void deleteChambre(String id) {
        chambreRepository.deleteById(id);
    }

}
