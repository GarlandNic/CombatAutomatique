package com.azurhyan.CombatAutomatique.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azurhyan.CombatAutomatique.model.ComboDB;
import com.azurhyan.CombatAutomatique.model.PersoPartie;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.model.PersosVisiblesDto;
import com.azurhyan.CombatAutomatique.repository.PersonnageRepository;

@Service
public class PersonnageService {
	
	@Autowired
	PersonnageRepository persoRepo;

	public Iterable<PersoPartie> listForPartie(String partieName) {
		Iterable <PersonnageDB> persoListBD = persoRepo.findByPartieAndVisible(partieName, true);
		List<PersoPartie> persosList = new ArrayList<PersoPartie>();
		persoListBD.forEach(perso -> persosList.add(new PersoPartie(perso)));
		return persosList;
	}

	public PersosVisiblesDto withVisibilite(String partie) {
		PersosVisiblesDto persosVisibles = new PersosVisiblesDto();
		Iterable<PersonnageDB> persoListBD = persoRepo.findByPartie(partie);
		persoListBD.forEach(perso -> persosVisibles.getPersoList().add(perso));
		return persosVisibles;
	}

	public void setAllVisibilite(PersosVisiblesDto persosVisibles) {
		persoRepo.saveAll(persosVisibles.getPersoList());
	}
	
	public ComboDB comboTotal(PersonnageDB perso) {
		ComboDB comboTot = comboHandicap(perso);
		perso.getComboList().forEach(combo -> {
			if(combo.getNom().equals("Base")) {
				comboTot.setCaC(combo.isCaC());
				comboTot.setTypeDgts(combo.getTypeDgts());
			}
			comboTot.setInit(comboTot.getInit()+combo.getInit());
			comboTot.setToucher(comboTot.getToucher()+combo.getToucher());
			comboTot.setPrdEnnemie(comboTot.getPrdEnnemie()+combo.getPrdEnnemie());
			comboTot.setForce(comboTot.getForce()+combo.getForce());
			comboTot.setIBatt(comboTot.getIBatt()+combo.getIBatt());
			comboTot.setDefense(comboTot.getDefense()+combo.getDefense());
			comboTot.setEsquive(comboTot.getEsquive()+combo.getEsquive());
			comboTot.setParade(comboTot.getParade()+combo.getParade());
			comboTot.setEndBouclier(comboTot.getEndBouclier()+combo.getEndBouclier());
			comboTot.setEndPerso(comboTot.getEndPerso()+combo.getEndPerso());
			comboTot.setIBdef(comboTot.getIBdef()+combo.getIBdef());
		});
		
		return comboTot;
	}

	public PersonnageDB findById(int persoId) {
		Optional<PersonnageDB> perso = persoRepo.findById(persoId);
		if(perso.isEmpty()) return null;
		return perso.get();
	}

	public ComboDB comboHandicap(PersonnageDB perso) {
		int demiH = Math.max(0, perso.getHfatigue() - perso.getCCinfatigable());
		demiH = demiH + Math.max(0, perso.getHmobilite() - perso.getCCincoercible());
		demiH = demiH + Math.max(0, perso.getHsens() - perso.getCCtoujoursPret());
		ComboDB comboH = new ComboDB("Handicap", perso, true);
		comboH.addHandicap(demiH);
		return comboH;
	}

	public PersonnageDB save(PersonnageDB perso) {
		return persoRepo.save(perso);
	}

}
