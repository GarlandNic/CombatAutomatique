package com.azurhyan.CombatAutomatique.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azurhyan.CombatAutomatique.model.ActionDB;
import com.azurhyan.CombatAutomatique.repository.ActionRepository;

@Service
public class ActionService {
	
	@Autowired
	ActionRepository actionRepo;
	
	public void annulerDerniereAction() {
		
	}

	public Iterable<ActionDB> listForPartie(String partie) {
		// TODO Auto-generated method stub
		return null;
	}

}
