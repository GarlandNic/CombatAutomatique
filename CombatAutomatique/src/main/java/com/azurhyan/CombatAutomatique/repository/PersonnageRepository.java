package com.azurhyan.CombatAutomatique.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.azurhyan.CombatAutomatique.model.PersonnageDB;

@Repository
public interface PersonnageRepository extends CrudRepository<PersonnageDB, Integer> {

	Iterable<PersonnageDB> findByPartieAndVisible(String partie, boolean visible);

	Iterable<PersonnageDB> findByPartie(String partie);

}
