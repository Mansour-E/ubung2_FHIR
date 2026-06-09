package de.medipolis.qualitaet.repository;

import de.medipolis.qualitaet.model.Medikament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedikamentRepository extends JpaRepository<Medikament, Long> {
    List<Medikament> findByPatientId(String patientId);
    List<Medikament> findByStatus(Medikament.Status status);
}
