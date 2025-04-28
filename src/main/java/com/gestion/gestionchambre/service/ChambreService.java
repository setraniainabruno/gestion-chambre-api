package com.gestion.gestionchambre.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.gestion.gestionchambre.model.Chambre;
import com.gestion.gestionchambre.model.ChambreStatus;
import com.gestion.gestionchambre.model.ChambreType;
import com.gestion.gestionchambre.repository.ChambreRepository;

@Service
public class ChambreService {
    private final ChambreRepository chambreRepository;

    @Autowired
    public ChambreService(ChambreRepository chambreRepository) {
        this.chambreRepository = chambreRepository;
    }

    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    public Optional<Chambre> getChambreById(String id) {
        return chambreRepository.findById(id);
    }

    public Chambre createChambre(Chambre chambre) {
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

    public void deleteChambre(String id) {
        chambreRepository.deleteById(id);
    }

    public List<Chambre> getChambresByType(ChambreType type) {
        return chambreRepository.findByType(type);
    }

    public List<Chambre> getAvailableChambres() {
        return chambreRepository.findByStatut(ChambreStatus.AVAILABLE);
    }

    public Chambre updateChambreStatus(String id, ChambreStatus status) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre not found with id: " + id));
        chambre.setStatut(status);
        return chambreRepository.save(chambre);
    }
}
