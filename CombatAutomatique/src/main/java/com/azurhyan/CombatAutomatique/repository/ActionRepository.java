package com.azurhyan.CombatAutomatique.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.azurhyan.CombatAutomatique.model.ActionDB;

@Repository
public interface ActionRepository extends CrudRepository<ActionDB, Integer>{
	
	Iterable<ActionDB> findByPartieOrderByActionTimeDesc(String partie);

	Iterable<ActionDB> removeByPartie(String partie);

	Iterable<ActionDB> findByRefAttaque(int refAttaque);

}
