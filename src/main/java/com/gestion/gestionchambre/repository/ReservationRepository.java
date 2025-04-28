package com.gestion.gestionchambre.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

import com.gestion.gestionchambre.model.Client;
import com.gestion.gestionchambre.model.Reservation;
import com.gestion.gestionchambre.model.ReservationStatus;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByClient(Client client);
    List<Reservation> findByStatut(ReservationStatus statut);
    List<Reservation> findByDateArriveeBetween(Date start, Date end);
    List<Reservation> findByDateDepartBetween(Date start, Date end);
    List<Reservation> findByPrixTotalGreaterThan(double prix);
}