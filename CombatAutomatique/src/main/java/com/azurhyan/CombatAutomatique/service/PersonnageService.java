package com.azurhyan.CombatAutomatique.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azurhyan.CombatAutomatique.model.PersoPartie;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.repository.PersonnageRepository;

@Service
public class PersonnageService {
	
	@Autowired
	PersonnageRepository persoRepo;

	public Iterable<PersoPartie> listForPartie(String partieName) {
		persoRepo.findByPartieAndVisible(partieName);
		// TODO Auto-generated method stub
		return null;
	}

}
