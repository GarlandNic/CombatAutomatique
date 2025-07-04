package com.azurhyan.CombatAutomatique.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azurhyan.CombatAutomatique.dto.ComboMltDto;
import com.azurhyan.CombatAutomatique.model.ComboDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.repository.ComboRepository;
import com.azurhyan.CombatAutomatique.repository.PersonnageRepository;

@Service
public class ComboService {
	
	@Autowired
	PersonnageRepository persoRepo;

	@Autowired
	ComboRepository comboRepo;

	@Transactional
	public void affecterCombo(ComboMltDto cmbMlt) {
		// TODO Auto-generated method stub
		// boucle sur les perso
		// pour chacun créer combo, sauvegarder combo
		cmbMlt.getCibleList().forEach(cible -> {
			PersonnageDB perso = persoRepo.findById(cible.getCibleId()).get();
			ComboDB combo = cmbMlt.getCombo().comboToDB(perso);
			comboRepo.save(combo);
		});
	}

}
