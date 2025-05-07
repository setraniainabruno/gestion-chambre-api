package com.gestion.gestionchambre.service;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import com.gestion.gestionchambre.model.Chambre;
import com.gestion.gestionchambre.model.ChambreStatus;
import com.gestion.gestionchambre.model.Client;
import com.gestion.gestionchambre.model.Reservation;
import com.gestion.gestionchambre.model.ReservationStatus;
import com.gestion.gestionchambre.repository.ChambreRepository;
import com.gestion.gestionchambre.repository.ClientRepository;
import com.gestion.gestionchambre.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ChambreRepository chambreRepository;

    
    public ReservationService(ReservationRepository reservationRepository,
            ClientRepository clientRepository,
            ChambreRepository chambreRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.chambreRepository = chambreRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public int getCountAllReservations() {
        return reservationRepository.findAll().size();
    }

    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(Reservation reservation, String clientId, String chambreId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

        Chambre chambre = chambreRepository.findById(chambreId)
                .orElseThrow(() -> new RuntimeException("Chambre not found with id: " + chambreId));

        if (chambre.getStatut() != ChambreStatus.Disponible) {
            throw new RuntimeException("Chambre is not available for reservation");
        }

        reservation.setClient(client);
        reservation.setChambre(chambre);

        // MAJ statut chambre
        chambre.setStatut(ChambreStatus.Réservée);
        chambreRepository.save(chambre);

        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(String id, Reservation reservationDetails) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));

        String chambreId = reservation.getChambre().getId();
        Chambre chambre = chambreRepository.findById(chambreId)
                .orElseThrow(() -> new RuntimeException("Chambre not found with id: " + chambreId));

        reservation.setDateArrivee(reservationDetails.getDateArrivee());
        reservation.setDateDepart(reservationDetails.getDateDepart());
        reservation.setNombrePersonnes(reservationDetails.getNombrePersonnes());
        reservation.setStatut(reservationDetails.getStatut());
        reservation.setCommentaires(reservationDetails.getCommentaires());
        reservation.setPrixTotal(reservationDetails.getPrixTotal());
        if (reservationDetails.getStatut().equals(ReservationStatus.Annulée) || reservationDetails.getStatut().equals(ReservationStatus.Terminée)) {
            chambre.setStatut(ChambreStatus.Disponible);
        } else {
            chambre.setStatut(ChambreStatus.Réservée);
        }

        chambreRepository.save(chambre);

        return reservationRepository.save(reservation);
    }

    public Reservation updateStatusReservation(String id, ReservationStatus statut) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));

        String chambreId = reservation.getChambre().getId();
        Chambre chambre = chambreRepository.findById(chambreId)
                .orElseThrow(() -> new RuntimeException("Chambre not found with id: " + chambreId));

        reservation.setStatut(statut);

        if (statut.equals(ReservationStatus.Annulée) || statut.equals(ReservationStatus.Terminée)) {
            chambre.setStatut(ChambreStatus.Disponible);
        }

        chambreRepository.save(chambre);

        return reservationRepository.save(reservation);
    }

    public void deleteReservation(String id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));

        // Update room status back to available
        Chambre chambre = reservation.getChambre();
        chambre.setStatut(ChambreStatus.Disponible);
        chambreRepository.save(chambre);

        reservationRepository.deleteById(id);
    }

    public List<Reservation> getReservationsByClient(String clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
        return reservationRepository.findByClient(client);
    }

    public int getCountReservationsByClient(String clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
        List<Reservation> res = reservationRepository.findByClient(client);
        int c = 0;
        for (Reservation reservation : res) {
            if (!reservation.getStatut().equals(ReservationStatus.Annulée)) {
                c++;
            }
        }
        return c;
    }

    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatut(status);
    }

   
}
