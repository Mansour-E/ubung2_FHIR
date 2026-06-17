package de.medipolis.fhir.repository;

import de.medipolis.fhir.model.Rezept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RezeptRepository extends JpaRepository<Rezept, Long> {
}
