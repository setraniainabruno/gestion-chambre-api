package com.gestion.gestionchambre.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.gestionchambre.model.Chambre;
import com.gestion.gestionchambre.model.ChambreStatus;
import com.gestion.gestionchambre.service.ChambreService;

@RestController
@RequestMapping("/api/chambres")
public class ChambreController {

    private final ChambreService chambreService;

    public ChambreController(ChambreService chambreService) {
        this.chambreService = chambreService;
    }

    @GetMapping
    public List<Chambre> getAllChambres() {
        return chambreService.getAllChambres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chambre> getChambreById(@PathVariable String id) {
        return chambreService.getChambreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Chambre createChambre(@RequestBody Chambre chambre) {
        return chambreService.createChambre(chambre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chambre> updateChambre(@PathVariable String id, @RequestBody Chambre chambreDetails) {
        try {
            Chambre updatedChambre = chambreService.updateChambre(id, chambreDetails);
            return ResponseEntity.ok(updatedChambre);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChambre(@PathVariable String id) {
        chambreService.deleteChambre(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponible")
    public List<Chambre> getAvailableChambres() {
        return chambreService.getAvailableChambres();
    }

    @GetMapping("/statut/{status}")
    public int getCountChambresByStatus(@PathVariable ChambreStatus status) {
        return chambreService.getCountChambresByStatus(status);
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Chambre> updateChambreStatus(@PathVariable String id, @RequestParam ChambreStatus statut) {
        try {
            Chambre updatedChambre = chambreService.updateChambreStatus(id, statut);
            return ResponseEntity.ok(updatedChambre);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
