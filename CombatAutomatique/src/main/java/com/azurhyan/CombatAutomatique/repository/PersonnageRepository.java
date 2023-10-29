package com.azurhyan.CombatAutomatique.repository;

import org.springframework.data.repository.CrudRepository;

import com.azurhyan.CombatAutomatique.model.PersonnageDB;

public interface PersonnageRepository extends CrudRepository<PersonnageDB, Integer> {

	Iterable<PersonnageDB> findByPartieAndVisible(String partie);

}
