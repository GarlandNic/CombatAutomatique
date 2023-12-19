package com.azurhyan.CombatAutomatique.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.azurhyan.CombatAutomatique.model.EtatDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;

@Repository
public interface EtatRepository extends CrudRepository<EtatDB, Integer> {

	Optional<EtatDB> findByPerso(PersonnageDB perso);

}
