package de.medipolis.review.repository;

import de.medipolis.review.model.Rezept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RezeptRepository extends JpaRepository<Rezept, Long> {

    List<Rezept> findByPatientId(String patientId);

    // ❌ REVIEW-PROBLEM #5: N+1 Query Problem versteckt sich hier.
    // Diese Methode wird im Service in einer Schleife aufgerufen.
    // Finde heraus wo und wie man es fixt.
    List<Rezept> findByStatus(String status);

    @Query("SELECT r FROM Rezept r WHERE r.arztName = ?1")
    List<Rezept> findByArzt(String arztName);

    @Query("SELECT r FROM Rezept r WHERE r.arztName IN ?1")
    List<Rezept> findByArztIn(List<String> arztNamen);
}
