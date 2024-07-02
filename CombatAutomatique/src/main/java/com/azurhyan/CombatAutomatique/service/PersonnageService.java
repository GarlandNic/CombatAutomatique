package com.azurhyan.CombatAutomatique.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azurhyan.CombatAutomatique.dto.PersoCompletDto;
import com.azurhyan.CombatAutomatique.dto.PersoPartieDto;
import com.azurhyan.CombatAutomatique.dto.PersosVisiblesDto;
import com.azurhyan.CombatAutomatique.model.BlessureDB;
import com.azurhyan.CombatAutomatique.model.ComboDB;
import com.azurhyan.CombatAutomatique.model.EtatDB;
import com.azurhyan.CombatAutomatique.model.HandicapDB;
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
		persosList.sort(Comparator.comparing(PersoPartieDto::getInit).reversed());
		return persosList;
	}

	public PersosVisiblesDto withVisibilite(String partie) {
		Iterable<PersonnageDB> persoListBD = persoRepo.findByPartieOrderByEtatTurnOrder(partie);
		PersosVisiblesDto persosVisibles = new PersosVisiblesDto();
		persoListBD.forEach(perso -> persosVisibles.getPersoList().add(new PersoPartieDto(perso)));
		return persosVisibles;
	}

	public void setAllEtat(PersosVisiblesDto persosVisibles) {
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
					etat.setIncapacite(persoPartie.getIncapacite());
					etat.setTurnOrder(persoPartie.getTurnOrder());
					etatRepo.save(etat);
				}
				if(etat == null) {
					EtatDB newEtat = new EtatDB(perso);
					newEtat.setDeDef(persoPartie.getDeDef());
					newEtat.setIncapacite(persoPartie.getIncapacite());
					newEtat.setTurnOrder(persoPartie.getTurnOrder());
					perso.setEtat(newEtat);
					persoRepo.save(perso);
				}
			}
		});
	}
	
//	public ComboDB comboTotal(PersonnageDB perso) {
//		ComboDB comboTot = comboHandicap(perso);
//		perso.getComboList().forEach(combo -> {
//			if(combo.getNom().equals("Base")) {
//				comboTot.setCaC(combo.isCaC());
//				comboTot.setTypeDgts(combo.getTypeDgts());
//			}
//			comboTot.setInit(comboTot.getInit()+combo.getInit());
//			comboTot.setToucher(comboTot.getToucher()+combo.getToucher());
//			comboTot.setPrdEnnemie(comboTot.getPrdEnnemie()+combo.getPrdEnnemie());
//			comboTot.setForce(comboTot.getForce()+combo.getForce());
//			comboTot.setIBatt(comboTot.getIBatt()+combo.getIBatt());
//			comboTot.setDefense(comboTot.getDefense()+combo.getDefense());
//			comboTot.setEsquive(comboTot.getEsquive()+combo.getEsquive());
//			comboTot.setParade(comboTot.getParade()+combo.getParade());
//			comboTot.setEndBouclier(comboTot.getEndBouclier()+combo.getEndBouclier());
//			comboTot.setEndPerso(comboTot.getEndPerso()+combo.getEndPerso());
//			comboTot.setIBdef(comboTot.getIBdef()+combo.getIBdef());
//		});
//		
//		return comboTot;
//	}

	public PersonnageDB findById(int persoId) {
		Optional<PersonnageDB> perso = persoRepo.findById(persoId);
		if(perso.isEmpty()) return null;
		return perso.get();
	}
	
	public EtatDB findByPersonnage(int persoId) {
		Optional<EtatDB> etat = etatRepo.findById(persoId);
		if(etat.isEmpty()) return new EtatDB(persoId);
		return etat.get();
	}

	public ComboDB comboHandicap(PersonnageDB perso) {
		ComboDB comboH = new ComboDB("Handicap", perso, true);
		comboH.addHandicap(perso.totalHandicaps());
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

	public PersoCompletDto saveDto(PersoCompletDto perso) {
		PersonnageDB pp = convertPersoDtoToDB(perso);
		if(pp.getEtat() == null && perso.getPersoId() != 0) {
			Optional<EtatDB> etat = etatRepo.findByPerso(pp);
			if(etat.isPresent()) pp.setEtat(etat.get());
		}
		return (new PersoCompletDto(persoRepo.save(pp)));
	}

	private PersonnageDB convertPersoDtoToDB(PersoCompletDto perso) {
		Optional<PersonnageDB> optPerso = persoRepo.findById(perso.getPersoId());
		PersonnageDB newPerso;
		if(optPerso.isEmpty()) newPerso = new PersonnageDB();
		else newPerso = optPerso.get();
		
		newPerso.setNom(perso.getNom());
		newPerso.setJoueur(perso.getJoueur());
		newPerso.setPartie(perso.getPartie());
		newPerso.setVisible(perso.isVisible());
		newPerso.setCON(perso.getCON());
		newPerso.setPdcCombat(perso.getPdcCombat());
		newPerso.setCCinfatigable2(perso.getCCinfatigable());
		newPerso.setCCincoercible2(perso.getCCincoercible());
		newPerso.setCCtoujoursPret2(perso.getCCtoujoursPret());
		newPerso.setCCcombatPlusieurs(perso.getCCcombatPlusieurs());
		
		List<BlessureDB> blList = newPerso.getBlessureList();
		blList.clear();
		perso.getBlessureList().forEach(bl -> blList.add(bl.blessureToDB(newPerso)));
		
		List<ComboDB> combList = newPerso.getComboList();
		combList.clear();
		perso.getComboList().forEach(comb -> combList.add(comb.comboToDB(newPerso)));
		
		// TODO ??
		List<HandicapDB> hList = newPerso.getHandicapList();
		hList.clear();
		perso.getHandicapList().forEach(h -> hList.add(h.handicapToDB(newPerso)));
		
		return newPerso;
	}

	public void saveCopy(PersonnageDB oldPerso) {
	    PersonnageDB newPerso = oldPerso.copy();
	    newPerso.setNom(incrementeNom(newPerso.getNom()));
	    newPerso = persoRepo.save(newPerso);
		newPerso.setEtat(etatRepo.save(new EtatDB(newPerso)));
	}

	public void resetDefense(String partie, int newRd) {
		Iterable<PersonnageDB> persoListBD = persoRepo.findByPartieAndVisible(partie, true);
		List<EtatDB> etatListBD = new ArrayList<>();
		persoListBD.forEach(perso -> {
			EtatDB etat = perso.getEtat();
			if(etat != null) {
				if(etat.getDeDef() !=0) {
					etat.setDeDef(0);
					etatListBD.add(etat);
				}
				if(newRd % 2 ==0) {
					switch(etat.getIncapacite()) {
						case 0: break;
						case 1: etat.setIncapacite(0); break;
						case 2: etat.setIncapacite(0); break;
						case 3: etat.setIncapacite(1); break;
					}
				}
			}
		});
		etatRepo.saveAll(etatListBD);		
	}
	
	public String incrementeNom(String nom) {
		Pattern pattern = Pattern.compile("^(.*\\s)(\\d+)$");
		Matcher m = pattern.matcher(nom);
		if(m.matches()) {
			nom = m.group(1) + String.format("%d", Integer.parseInt(m.group(2)) + 1);
		} else {
			nom += " 1";			
		}
		return nom;
	}

	public void deleteById(Integer id) {
		persoRepo.deleteById(id);
	}

	public void switchArchivage(Integer id) {
		PersonnageDB perso = persoRepo.findById(id).get();
		perso.getEtat().setTurnOrder( - perso.getEtat().getTurnOrder());
		persoRepo.save(perso);
	}

}
