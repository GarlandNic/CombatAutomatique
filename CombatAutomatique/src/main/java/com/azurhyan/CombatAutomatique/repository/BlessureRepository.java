package com.azurhyan.CombatAutomatique.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.azurhyan.CombatAutomatique.model.BlessureDB;

@Repository
public interface BlessureRepository extends CrudRepository<BlessureDB, Integer> {

	Iterable<BlessureDB> findByRefAction(int actionId);

}
