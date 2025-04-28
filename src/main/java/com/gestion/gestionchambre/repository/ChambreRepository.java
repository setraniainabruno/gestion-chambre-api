package com.gestion.gestionchambre.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import com.gestion.gestionchambre.model.Chambre;
import com.gestion.gestionchambre.model.ChambreStatus;
import com.gestion.gestionchambre.model.ChambreType;

public interface ChambreRepository extends MongoRepository<Chambre, String> {
    List<Chambre> findByType(ChambreType type);
    List<Chambre> findByEtage(int etage);
    List<Chambre> findByStatut(ChambreStatus statut);
    Chambre findByNumero(String numero);
    List<Chambre> findByPrixBetween(double minPrix, double maxPrix);
}
