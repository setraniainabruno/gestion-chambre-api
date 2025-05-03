package com.gestion.gestionchambre.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.gestion.gestionchambre.model.Reservation;
import com.gestion.gestionchambre.model.ReservationStatus;
import com.gestion.gestionchambre.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/count")
    public int getCountAllReservations() {
        return reservationService.getCountAllReservations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable String id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservation,
            @RequestParam String clientId,
            @RequestParam String chambreId) {
        try {
            Reservation createdReservation = reservationService.createReservation(reservation, clientId, chambreId);
            return ResponseEntity.ok(createdReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable String id, @RequestBody Reservation reservationDetails) {
        try {
            Reservation updatedReservation = reservationService.updateReservation(id, reservationDetails);
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Reservation> updateStatusReservation(@PathVariable String id, @RequestParam ReservationStatus statut) {
        try {
            Reservation updatedReservation = reservationService.updateStatusReservation(id, statut);
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable String id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public List<Reservation> getReservationsByClient(@PathVariable String clientId) {
        return reservationService.getReservationsByClient(clientId);
    }

    @GetMapping("/nombre-client/{clientId}")
    public int getCountReservationsByClient(@PathVariable String clientId) {
        return reservationService.getCountReservationsByClient(clientId);
    }

    @GetMapping("/status/{status}")
    public List<Reservation> getReservationsByStatus(@PathVariable ReservationStatus status) {
        return reservationService.getReservationsByStatus(status);
    }
}
