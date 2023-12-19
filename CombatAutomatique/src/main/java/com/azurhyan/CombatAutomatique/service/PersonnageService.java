package com.azurhyan.CombatAutomatique.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azurhyan.CombatAutomatique.dto.PersoCompletDto;
import com.azurhyan.CombatAutomatique.dto.PersoPartieDto;
import com.azurhyan.CombatAutomatique.dto.PersosVisiblesDto;
import com.azurhyan.CombatAutomatique.model.BlessureDB;
import com.azurhyan.CombatAutomatique.model.ComboDB;
import com.azurhyan.CombatAutomatique.model.EtatDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.repository.EtatRepository;
import com.azurhyan.CombatAutomatique.repository.PersonnageRepository;

@Service
public class PersonnageService {
	
	@Autowired
	PersonnageRepository persoRepo;
	
	@Autowired
	EtatRepository etatRepo;

	public Iterable<PersoPartieDto> listForPartie(String partieName) {
		Iterable <PersonnageDB> persoListBD = persoRepo.findByPartieAndVisible(partieName, true);
		List<PersoPartieDto> persosList = new ArrayList<PersoPartieDto>();
		persoListBD.forEach(perso -> persosList.add(new PersoPartieDto(perso)));
		return persosList;
	}

	public PersosVisiblesDto withVisibilite(String partie) {
		Iterable<PersonnageDB> persoListBD = persoRepo.findByPartie(partie);
		PersosVisiblesDto persosVisibles = new PersosVisiblesDto();
		persoListBD.forEach(perso -> persosVisibles.getPersoList().add(new PersoPartieDto(perso)));
		return persosVisibles;
	}

	public void setAllVisibilite(PersosVisiblesDto persosVisibles) {
		persosVisibles.getPersoList().forEach(persoPartie -> {
			Optional<PersonnageDB> persoDB = persoRepo.findById(persoPartie.getId());
			if(persoDB.isPresent()) {
				PersonnageDB perso = persoDB.get();
				if(perso.isVisible() != persoPartie.isVisible()) {
					perso.setVisible(persoPartie.isVisible());
					persoRepo.save(perso);
				}
				EtatDB etat = perso.getEtat();
				if(etat != null) {
					etat.setDeDef(persoPartie.getDeDef());
					etatRepo.save(etat);
				}
				if(etat == null && persoPartie.getDeDef() != 0) {
					EtatDB newEtat = new EtatDB(perso);
					newEtat.setDeDef(persoPartie.getDeDef());
					perso.setEtat(newEtat);
					persoRepo.save(perso);
				}
			}
		});
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
	
	public PersoCompletDto findPersoCompletDto(int persoId) {
		Optional<PersonnageDB> perso = persoRepo.findById(persoId);
		if(perso.isEmpty()) return null;
		
		return (new PersoCompletDto(perso.get()));
	}

	public PersonnageDB saveDto(PersoCompletDto perso) {
		PersonnageDB pp = perso.persoToDB();
		if(pp.getEtat() == null) {
			Optional<EtatDB> etat = etatRepo.findByPerso(pp);
			if(etat.isPresent()) pp.setEtat(etat.get());
		}
		return persoRepo.save(pp);
	}

	public void saveCopy(PersonnageDB oldPerso) {
	    PersonnageDB newPerso = oldPerso.copy();
	    newPerso.setNom(newPerso.getNom().concat("_bis"));
		persoRepo.save(newPerso);
	}

	public void resetDefense(String partie) {
		Iterable<PersonnageDB> persoListBD = persoRepo.findByPartieAndVisible(partie, true);
		List<EtatDB> etatListBD = new ArrayList<>();
		persoListBD.forEach(perso -> {
			EtatDB etat = perso.getEtat();
			if(etat != null && etat.getDeDef() !=0) {
				etat.setDeDef(0);
				etatListBD.add(etat);
			}
		});
		etatRepo.saveAll(etatListBD);		
	}

}
