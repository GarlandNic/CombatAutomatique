package com.azurhyan.CombatAutomatique.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azurhyan.CombatAutomatique.model.PersoPartie;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.model.PersosVisiblesDto;
import com.azurhyan.CombatAutomatique.repository.PersonnageRepository;

@Service
public class PersonnageService {
	
	@Autowired
	PersonnageRepository persoRepo;

	public Iterable<PersoPartie> listForPartie(String partieName) {
		// liste des perso visibles avec init/nom/id à mettre 
		persoRepo.findByPartieAndVisible(partieName, true);
		// TODO Auto-generated method stub
		return null;
	}

	public PersosVisiblesDto withVisibilite(String partie) {
		// persoRepo.findByPartie(partie);
		// TODO Auto-generated method stub
		return null;
	}

	public void setAllVisibilite(PersosVisiblesDto persosVisibles) {
		// TODO Auto-generated method stub
		// persoRepo.
	}

}
