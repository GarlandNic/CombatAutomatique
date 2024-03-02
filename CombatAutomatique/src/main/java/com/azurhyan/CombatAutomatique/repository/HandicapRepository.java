package com.azurhyan.CombatAutomatique.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.azurhyan.CombatAutomatique.model.BlessureDB;
import com.azurhyan.CombatAutomatique.model.HandicapDB;

@Repository
public interface HandicapRepository extends CrudRepository<HandicapDB, Integer> {

	Iterable<HandicapDB> findByRefAction(int actionId);

}
