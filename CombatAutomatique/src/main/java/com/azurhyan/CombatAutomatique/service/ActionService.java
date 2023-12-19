package com.azurhyan.CombatAutomatique.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azurhyan.CombatAutomatique.dto.ActionDto;
import com.azurhyan.CombatAutomatique.dto.AttaqueDto;
import com.azurhyan.CombatAutomatique.dto.BlessureDto;
import com.azurhyan.CombatAutomatique.dto.CibleDto;
import com.azurhyan.CombatAutomatique.dto.ComboDto;
import com.azurhyan.CombatAutomatique.dto.Degat;
import com.azurhyan.CombatAutomatique.model.ComboDB.Dgts;
import com.azurhyan.CombatAutomatique.model.EtatDB;
import com.azurhyan.CombatAutomatique.dto.PersoCompletDto;
import com.azurhyan.CombatAutomatique.model.ActionDB;
import com.azurhyan.CombatAutomatique.model.BlessureDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.repository.ActionRepository;
import com.azurhyan.CombatAutomatique.repository.BlessureRepository;
import com.azurhyan.CombatAutomatique.repository.EtatRepository;
import com.azurhyan.CombatAutomatique.repository.PersonnageRepository;

@Service
public class ActionService {

	@Autowired
	ActionRepository actionRepo;

	@Autowired
	PersonnageRepository persoRepo;
	
	@Autowired
	BlessureRepository blessRepo;
	
	@Autowired
	EtatRepository etatRepo;
	
	public void annulerDerniereAction() {
		// TODO
	}
	
	@Transactional
	public void effacerLesActions(String partie) {
		actionRepo.removeByPartie(partie);
	}

	public Iterable<ActionDB> listForPartie(String partie) {
		return actionRepo.findByPartie(partie);
	}

	public ActionDB lancerAttaque(AttaqueDto attaque) {
		if(attaque.getAttaquantList().size() > 1) {
			int groupTch = attaque.getAttaquantList().size();
			int groupFor = groupTch/2;
			attaque.getAttaquantList().forEach(perso -> {
				PersonnageDB persoDB = persoRepo.findById(perso.getCibleId()).get();
				PersoCompletDto attaquant = new PersoCompletDto(persoDB);
				AttaqueDto att = new AttaqueDto();
				att.setAttaquantId(attaquant.getPersoId());
				att.setAttaquantNom(attaquant.getNom());
				att.setAttaquantPdc(attaquant.getPdcCombat());
				ComboDto comb = attaquant.getComboTotal();
				comb.modifComboTF(groupTch, groupFor);
				att.setAttaquantCombo(comb);
				List<CibleDto> attList = new ArrayList<>();
				attList.add(perso);
				att.setAttaquantList(attList);
				att.setCibleList(attaque.getCibleList());
				att.setAutreList(attaque.getAutreList());
				att.setLePlusPossible(attaque.isLePlusPossible());
				att.setPartie(attaque.getPartie());
				
				lancerAttaque1vN(att);
			});
			return null;
		} else {
			return lancerAttaque1vN(attaque);
		}
	}
	
	public ActionDB lancerAttaque1vN(AttaqueDto attaque) {
		ActionDB act = new ActionDB();
		act.setActeurNom(attaque.getAttaquantNom());
		act.setPersoId(attaque.getAttaquantId());
		act.setPartie(attaque.getPartie());
		act.setActionTime(LocalDateTime.now());
		act.setDescription(attaque.getAttaquantNom().concat(" attaque "));
		
		ActionDto strDe = new ActionDto();
		int bonusDuDe = jetDAttaque(attaque.getAttaquantPdc(), strDe);
		ComboDto comboFinal = attaque.getAttaquantCombo();
		comboFinal.modifComboTF(bonusDuDe, bonusDuDe);
		act.addDescription("("+strDe.getDescription()+") ");
		
		int nbAdv = attaque.getCibleList().size();
		if(nbAdv==0) return null;
		if(nbAdv==1) {
			PersonnageDB defenseurDB = persoRepo.findById(attaque.getCibleList().get(0).getCibleId()).get();
			PersoCompletDto defenseur = new PersoCompletDto(defenseurDB);
			act.addDescription(attaqueUnique(comboFinal, defenseur));
		} else if(nbAdv>1) {
			int combatPlus = persoRepo.findById(attaque.getAttaquantId()).get().getCCcombatPlusieurs();
			
			if(attaque.isLePlusPossible()) {
				int malusTch = 0;
				int malusFor = 0;
				for(CibleDto cible : attaque.getCibleList()) {
					malusTch += 2;
					malusFor += 1;
					int malusTchTot = malusTch - combaPlusToucher(combatPlus);
					if(malusTchTot < 0) malusTchTot=0;
					int malusForTot = malusFor - combaPlusForce(combatPlus);
					if(malusForTot < 0) malusForTot=0;
					comboFinal.modifComboTF( -malusTch, -malusFor);
					
					PersonnageDB defenseurDB = persoRepo.findById(cible.getCibleId()).get();
					PersoCompletDto defenseur = new PersoCompletDto(defenseurDB);
					act.addDescription("- "+attaqueUnique(comboFinal, defenseur));
				}
			} else {
				int malusTch = combaPlusMalusToucher(nbAdv) - combaPlusToucher(combatPlus);
				if(malusTch < 0) malusTch=0;
				int malusFor = combaPlusMalusForce(nbAdv) - combaPlusForce(combatPlus);
				if(malusFor < 0) malusFor=0;
				comboFinal.modifComboTF( -malusTch, -malusFor);
				
				for(CibleDto cible : attaque.getCibleList()) {
					PersonnageDB defenseurDB = persoRepo.findById(cible.getCibleId()).get();
					PersoCompletDto defenseur = new PersoCompletDto(defenseurDB);
					act.addDescription("- "+attaqueUnique(comboFinal, defenseur));
				}
			}
		}
		return saveAction(act);
	}
	
	// renvoie : "defenseur (dé) : raté|paré|Touché ! {dégâts}." + modif BD défenseur
	public String attaqueUnique(ComboDto comboFinal, PersoCompletDto defenseur) {
		String descr = defenseur.getNom();

		ComboDto comboDef = defenseur.getComboTotal();
		ActionDto strDe = new ActionDto();
		int bonusDef = jetDeDefense(defenseur, strDe);
		comboDef.appliqueBonusDefense(bonusDef);
		descr += " ("+strDe.getDescription()+") : ";
		
		int margeTch = comboFinal.getToucher() - (comboFinal.isCaC() ? comboDef.getDefense() : comboDef.getEsquive());
		int endu = 0;
		
		if(margeTch < 0) {
			descr += "raté !";
			return descr.concat(" <br/> ");
		} else {
			int bonusForce = (margeTch-1 - (comboDef.getParade() + comboFinal.getPrdEnnemie()))/3;
			bonusForce = (bonusForce > 0 ? bonusForce : 0);
			boolean isPare = (comboDef.getParade() + comboFinal.getPrdEnnemie() >= margeTch);
			String partieTouchee ="";
			int bousculade = 0;
			if(isPare) {
				descr += "paré ! ";
				BlessureDto blBcl = calculDegats(bonusForce+comboFinal.getForce(), comboDef.getEndBouclier(), comboFinal.getTypeDgts() == Dgts.CTD);
				bousculade = bonusForce+comboFinal.getForce() - comboDef.getEndBouclier() + comboFinal.getIBatt() - comboDef.getIBdef();
				if(null == blBcl) {
					descr += "Pas de dégâts.";
					return descr.concat(" <br/> ");
				}
				partieTouchee = "Bouclier";
				blBcl.setPartieTouchee(partieTouchee);
				blBcl.setPtDeChoc(0);
				BlessureDB blToSave = blBcl.blessureToDB(defenseur.persoToDB());
				if(comboFinal.getTypeDgts()==Dgts.PRF) blToSave.setDemiNiveau(blToSave.getDemiNiveau()/2);
				blessRepo.save(blToSave);
				
				descr += "Bouclier : "+blToSave.getNiveau()+" nv ; ";
				
				partieTouchee = "bras-bouclier";
				endu = 4 + (comboDef.getEndPerso() > comboDef.getEndBouclier() ? comboDef.getEndPerso() : comboDef.getEndBouclier());
			} else {
				descr += "Touché ! ";
				partieTouchee = partieTouchee();
				endu = comboDef.getEndPerso();
			}
			BlessureDto bl = calculDegats(bonusForce+comboFinal.getForce(), endu, comboFinal.getTypeDgts() == Dgts.CTD);
			if(bousculade == 0)
				bousculade = bonusForce+comboFinal.getForce() - endu + comboFinal.getIBatt() - comboDef.getIBdef();
			if(null == bl) {
				descr += "Pas de dégâts.";
				return descr.concat(" <br/> ");
			}
			bl.setPartieTouchee(partieTouchee);
			PersonnageDB defDB = defenseur.persoToDB();
			BlessureDB blToSave = bl.blessureToDB(defDB);
			blessRepo.save(blToSave);

			descr += partieTouchee;
			descr += (" : "+blToSave.getNiveau()+" nv & "+blToSave.getPtDeChoc()+"#");
			descr += (" ("+blToSave.getGravite()+")");
			defDB.setHfatigue(defDB.getHfatigue() + (int) (blToSave.getHandicap()*2));
			if(bousculade > 1 && comboFinal.getTypeDgts() == Dgts.CTD) {
				descr += " (bousculade : "+(((float) (bousculade/2))/2.0)+" H-)";
				defDB.setHmobilite(defDB.getHmobilite() + bousculade/2);
			}			
			persoRepo.save(defDB);
			
			BlessureDB blArmure = new BlessureDB();
			blArmure.setPerso(defDB);
			blArmure.setDemiNiveau(comboFinal.getTypeDgts()==Dgts.PRF ? blToSave.getDemiNiveau()/2 : blToSave.getDemiNiveau());
			blArmure.setPtDeChoc(0);
			blArmure.setPartieTouchee("Armure");
			blessRepo.save(blArmure);
			
			return descr.concat(". <br/> ");
		}
	}
	
	public Degat attaqueUnique(ComboDto comboAtt, ComboDto comboDef) {
		Degat result = new Degat();
	
		int margeTch = comboAtt.getToucher() - (comboAtt.isCaC() ? comboDef.getDefense() : comboDef.getEsquive());
		int endu = 0;
		
		if(margeTch < 0) {
			return result;
		} else {
			int bonusForce = (margeTch-1 - (comboDef.getParade() + comboAtt.getPrdEnnemie()))/3;
			bonusForce = (bonusForce > 0 ? bonusForce : 0);
			boolean isPare = (comboDef.getParade() + comboAtt.getPrdEnnemie() >= margeTch);
			String partieTouchee ="";
			int bousculade = 0;
			if(isPare) {
				BlessureDto blBcl = calculDegats(bonusForce+comboAtt.getForce(), comboDef.getEndBouclier(), comboAtt.getTypeDgts() == Dgts.CTD);
				
				bousculade = bonusForce+comboAtt.getForce() - comboDef.getEndBouclier() + comboAtt.getIBatt() - comboDef.getIBdef();

				partieTouchee = "Bouclier";
				blBcl.setPartieTouchee(partieTouchee);
				blBcl.setPtDeChoc(0);
				if(comboAtt.getTypeDgts()==Dgts.PRF) blBcl.setDemiNiveau(blBcl.getDemiNiveau()/2);
				if(blBcl.getNiveau() > 0) result.getBlessList().add(blBcl);
				
				partieTouchee = "bras-bouclier";
				endu = 4 + (comboDef.getEndPerso() > comboDef.getEndBouclier() ? comboDef.getEndPerso() : comboDef.getEndBouclier());
			} else {
				partieTouchee = partieTouchee();
				endu = comboDef.getEndPerso();
			}
			BlessureDto bl = calculDegats(bonusForce+comboAtt.getForce(), endu, comboAtt.getTypeDgts() == Dgts.CTD);
			if(bousculade == 0)
				bousculade = bonusForce+comboAtt.getForce() - endu + comboAtt.getIBatt() - comboDef.getIBdef();

			bl.setPartieTouchee(partieTouchee);
			if(bl.getNiveau() > 0) {
				result.getBlessList().add(bl);
				
				BlessureDto blArmure = new BlessureDto();
				blArmure.setDemiNiveau((comboAtt.getTypeDgts()==Dgts.PRF ? bl.getDemiNiveau()/2 : bl.getDemiNiveau()));
				blArmure.setPtDeChoc(0);
				blArmure.setPartieTouchee("Armure");
				if(blArmure.getNiveau() > 0) result.getBlessList().add(blArmure);
			}

			if(bousculade > 1 && comboAtt.getTypeDgts() == Dgts.CTD) {
				result.setBousculadeH( ((float) (bousculade/2))/2 );
			}
			
			return result;
		}
	}
	
	private BlessureDto calculDegats(int force, int endu, boolean isCtd) {
		int margeBless = force - endu;
		if(margeBless <= 0) return null;
		int ptChoc = 0;
		float nvBl = (float) 0.0;
		if(isCtd) {
			ptChoc = margeBless;
			nvBl = (float) (margeBless/2.0) - 1;
			nvBl = (nvBl > 0 ? nvBl : 0);
		} else {
			ptChoc = (margeBless+1)/2;
			nvBl = (float) (margeBless/2.0);
		}
		BlessureDto bl = new BlessureDto(nvBl, ptChoc);
		return bl;
	}

	private String partieTouchee() {
		Random random = new Random();
		int nb;
		nb = 1+random.nextInt(19);
		switch(nb) {
			case 1: return "Face";
			case 2: return "Machoire/cou";
			case 3: return "Crâne";
			case 4: return "Epaule ou bras droit";
			case 5: return "Avant-bras droit";
			case 6: return "Main droite ou coude";
			case 7: return "Epaule ou bras gauche";
			case 8: return "Avant-bras gauche";
			case 9: return "Main gauche ou coude";
			case 10: return "Poitrine";
			case 11: return "Ventre";
			case 12: return "Dos";
			case 13: return "Entrejambe";
			case 14: return "Cuisse droite";
			case 15: return "Cuisse gauche";
			case 16: return "Tidia droit";
			case 17: return "Tibia gauche";
			case 18: return "Bassin";
			case 19: return "Pied droit ou genou";
			case 20: return "Pied gauche ou genou";
			default: return "";
		}
	}

	public ActionDB saveAction(ActionDB act) {
		return actionRepo.save(act);
	}
	
	private int D20(boolean comp) { return D20f(comp, 0); }
	private int D20f(boolean comp, int forcage) {
		if(forcage!=0) return forcage;
		Random random = new Random();
		int nb;
		nb = 1+random.nextInt(19);
		if(nb==10) return 10+D20f(comp,0);
		if(nb==20 && comp) return 20+D20f(comp,0);
		return nb;
	}
	
	private int bonusD20(int bonusMin, int de) {
		 switch(de) {
		 	case 1: return -10;
		 	case 2: return (bonusMin > -8 ? bonusMin : -8);
		 	case 3: return (bonusMin > -7 ? bonusMin : -7);
		 	case 4: return (bonusMin > -6 ? bonusMin : -6);
		 	case 5: return (bonusMin > -5 ? bonusMin : -5);
		 	case 6: return (bonusMin > -4 ? bonusMin : -4);
		 	case 7: return (bonusMin > -3 ? bonusMin : -3);
		 	case 8: return (bonusMin > -2 ? bonusMin : -2);
		 	case 9: return (bonusMin > -1 ? bonusMin : -1);
		 	case 10: case 11: return 0;
		 	case 12: case 13: return 1;
		 	case 14: case 15: return 2;
		 	case 16: case 17: return 3;
		 	case 18: case 19: return 4;
		 	case 20: case 21: case 22: case 23: return 5;
		 	case 24: case 25: case 26: case 27: return 6;
		 	case 28: case 29: case 30: case 31: return 7;
		 	case 32: case 33: case 34: case 35: return 8;
		 	case 36: case 37: case 38: case 39: case 40: return 9;
		 	default: return (1+bonusD20(bonusMin, de-5));
		 }
	}
	
	public int jetDeComp(int pdc, ActionDto str) { return jetDeCompf(pdc, str, 0); }
	public int jetDeCompf(int pdc, ActionDto str, int forcage) {
		int bonusMin = -10;
		switch(pdc) {
			case 0: bonusMin = -10; break;
			case 1: bonusMin = -5; break;
			case 2: bonusMin = -4; break;
			case 3: bonusMin = -3; break;
			case 4: bonusMin = -2; break;
			case 5: bonusMin = -2; break;
			default: bonusMin = -2;
		}
		boolean comp = (pdc != 0);
		
		int de = D20f(comp, forcage);
		str.setDescription(Integer.toString(de));
		if(de==1) str.setDescription("EC !");
		return bonusD20(bonusMin, de);
	}
	
	public int jetDAttaque(int pdc, ActionDto str) { return jetDAttaquef(pdc, str, 0); }
	public int jetDAttaquef(int pdc, ActionDto str, int forcage) {
		int bonusMin = -10;
		boolean comp = (pdc != 0);		
		int de = D20f(comp, forcage);
		str.setDescription(Integer.toString(de));
		if(de==1) str.setDescription("EC !");
		return bonusD20(bonusMin, de);
	}
	
	public int jetDeDefense(PersoCompletDto defenseur, ActionDto str) {
		int forcage = 0;
		int pdc = defenseur.getPdcCombat();
		Optional<EtatDB> etat = etatRepo.findById(defenseur.getPersoId());
		if(etat.isPresent() && etat.get().getDeDef() != 0) {
			forcage = etat.get().getDeDef();
		} else {
			forcage = D20(pdc != 0);
			EtatDB etatget;
			if(etat.isPresent()) etatget = etat.get();
			else etatget = new EtatDB(persoRepo.findById(defenseur.getPersoId()).get());
			etatget.setDeDef(forcage);
			etatRepo.save(etatget);
		}
		int bonus = jetDeCompf(pdc, str, forcage);
		if(bonus == -10) {
			switch(pdc) {
				case 0: bonus = -10; break;
				case 1: bonus = -10; break;
				case 2: bonus = -8; break;
				case 3: bonus = -6; break;
				case 4: bonus = -4; break;
				case 5: bonus = -4; break;
				default: bonus = -4;
			}
		}
		return bonus;
	}
	
	public int combaPlusToucher(int CC) {
		return combaPlusToucherRec(CC, 0);
	}
	public int combaPlusToucherRec(int CC, int tch) {
		switch(CC) {
			case 0: return 0;
			case 1: return 2+tch;
			case 2: return 2+tch;
			default: return combaPlusToucherRec(CC-2, tch+2);
		}
	}

	public int combaPlusForce(int CC) {
		return combaPlusForceRec(CC, 0);
	}
	public int combaPlusForceRec(int CC, int force) {
		switch(CC) {
			case 0: return 0;
			case 1: return force;
			case 2: return 1+force;
			default: return combaPlusForceRec(CC-2, force+1);
		}
	}

	public int combaPlusMalusToucher(int nb) {
		return combaPlusMalusToucherRec(nb, 0);
	}
	public int combaPlusMalusToucherRec(int nb, int tch) {
		switch(nb) {
			case 1: return 2+tch;
			case 2: return 4+tch;
			default: return combaPlusMalusToucherRec(nb-2, tch+2);
		}
	}

	public int combaPlusMalusForce(int nb) {
		return combaPlusMalusForceRec(nb, 0);
	}
	public int combaPlusMalusForceRec(int nb, int force) {
		switch(nb) {
			case 1: return 1+force;
			case 2: return 1+force;
			default: return combaPlusMalusForceRec(nb-2, force+1);
		}
	}



}
