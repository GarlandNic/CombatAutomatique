package com.azurhyan.CombatAutomatique.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.azurhyan.CombatAutomatique.model.ComboDB;

@Repository
public interface ComboRepository extends CrudRepository<ComboDB, Integer> {

}
