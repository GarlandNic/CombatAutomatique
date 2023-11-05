package com.azurhyan.CombatAutomatique.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.azurhyan.CombatAutomatique.model.ActionDB;

public interface ActionRepository extends CrudRepository<ActionDB, Integer>{
	
	Iterable<ActionDB> findByActeurPartie(String partie);

}
