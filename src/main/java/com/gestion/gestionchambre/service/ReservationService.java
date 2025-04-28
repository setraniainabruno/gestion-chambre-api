package com.gestion.gestionchambre.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Autowired
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

    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(Reservation reservation, String clientId, String chambreId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
        
        Chambre chambre = chambreRepository.findById(chambreId)
                .orElseThrow(() -> new RuntimeException("Chambre not found with id: " + chambreId));
        
        if (chambre.getStatut() != ChambreStatus.AVAILABLE) {
            throw new RuntimeException("Chambre is not available for reservation");
        }
        
        // Calculate total price based on number of days and room price
        long diffInMillies = Math.abs(reservation.getDateDepart().getTime() - reservation.getDateArrivee().getTime());
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        double totalPrice = diffInDays * chambre.getPrix();
        
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setPrixTotal(totalPrice);
        reservation.setStatut(ReservationStatus.CONFIRMED);
        
        // Update room status
        chambre.setStatut(ChambreStatus.RESERVED);
        chambreRepository.save(chambre);
        
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(String id, Reservation reservationDetails) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        
        reservation.setDateArrivee(reservationDetails.getDateArrivee());
        reservation.setDateDepart(reservationDetails.getDateDepart());
        reservation.setNombrePersonnes(reservationDetails.getNombrePersonnes());
        reservation.setStatut(reservationDetails.getStatut());
        reservation.setCommentaires(reservationDetails.getCommentaires());
        
        // Recalculate price if dates changed
        if (!reservation.getDateArrivee().equals(reservationDetails.getDateArrivee()) || 
            !reservation.getDateDepart().equals(reservationDetails.getDateDepart())) {
            long diffInMillies = Math.abs(reservation.getDateDepart().getTime() - reservation.getDateArrivee().getTime());
            long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
            double totalPrice = diffInDays * reservation.getChambre().getPrix();
            reservation.setPrixTotal(totalPrice);
        }
        
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(String id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        
        // Update room status back to available
        Chambre chambre = reservation.getChambre();
        chambre.setStatut(ChambreStatus.AVAILABLE);
        chambreRepository.save(chambre);
        
        reservationRepository.deleteById(id);
    }

    public List<Reservation> getReservationsByClient(String clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
        return reservationRepository.findByClient(client);
    }

    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatut(status);
    }

    public List<Reservation> getReservationsBetweenDates(Date start, Date end) {
        return reservationRepository.findByDateArriveeBetween(start, end);
    }
}
