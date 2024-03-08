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
import com.azurhyan.CombatAutomatique.dto.HandicapDto;
import com.azurhyan.CombatAutomatique.model.ComboDB.Bouclier;
import com.azurhyan.CombatAutomatique.model.ComboDB.Dgts;
import com.azurhyan.CombatAutomatique.model.ComboDB.Element;
import com.azurhyan.CombatAutomatique.model.EtatDB;
import com.azurhyan.CombatAutomatique.model.HandicapDB;
import com.azurhyan.CombatAutomatique.model.HandicapDB.TypeHand;
import com.azurhyan.CombatAutomatique.dto.PersoCompletDto;
import com.azurhyan.CombatAutomatique.model.ActionDB;
import com.azurhyan.CombatAutomatique.model.BlessureDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.repository.ActionRepository;
import com.azurhyan.CombatAutomatique.repository.BlessureRepository;
import com.azurhyan.CombatAutomatique.repository.EtatRepository;
import com.azurhyan.CombatAutomatique.repository.HandicapRepository;
import com.azurhyan.CombatAutomatique.repository.PersonnageRepository;

@Service
public class ActionService {
	
	private static int ROUND = 0;
	
	@Autowired
	ActionRepository actionRepo;

	@Autowired
	PersonnageRepository persoRepo;
	
	@Autowired
	BlessureRepository blessRepo;
	@Autowired
	HandicapRepository handiRepo;
	
	@Autowired
	EtatRepository etatRepo;
	
	public void annulerAttaque(int refAttaque) {
		Iterable<ActionDB> listActions = actionRepo.findByRefAttaque(refAttaque);
		listActions.forEach(act -> annulerAction(act));
	}

	@Transactional
	private void annulerAction(ActionDB act) {
		Iterable<HandicapDB> listHandicaps = handiRepo.findByRefAction(act.getActionId());
		listHandicaps.forEach(h -> annulerHandicap(h));
		Iterable<BlessureDB> listBlessures = blessRepo.findByRefAction(act.getActionId());
		listBlessures.forEach(bl -> annulerBlessure(bl));
		actionRepo.delete(act);
	}

	private void annulerBlessure(BlessureDB bl) {
		bl.getPerso().getBlessureList().remove(bl);
		persoRepo.save(bl.getPerso());
	}
	private void annulerHandicap(HandicapDB h) {
		h.getPerso().getHandicapList().remove(h);
		persoRepo.save(h.getPerso());
	}

	public AttaqueDto fetchAttaque(int refAttaque, String partie) {
		AttaqueDto attaque = new AttaqueDto();
		attaque.setPartie(partie);

		Iterable<ActionDB> listActions = actionRepo.findByRefAttaque(refAttaque);
		listActions.forEach(act -> {
			boolean nouveau = true;
			for(CibleDto attl : attaque.getAttaquantList()) {
				if(attl.getCibleId() == act.getPersoId()) nouveau = false;
			}
			if(nouveau) {
				CibleDto att = new CibleDto(act.getPersoId(), act.getActeurNom(), act.getActeurDe());
				attaque.getAttaquantList().add(att);				
			}
			nouveau = true;
			for(CibleDto defl : attaque.getCibleList()) {
				if(defl.getCibleId() == act.getCibleId()) nouveau = false;
			}
			if(nouveau) {
				CibleDto def = new CibleDto(act.getCibleId(), act.getCibleNom(), act.getCibleDe());
				attaque.getCibleList().add(def);
			}
		});
		
		Iterable<PersonnageDB> listPersos = persoRepo.findByPartieAndVisible(partie, true);
		listPersos.forEach(pers -> {
			boolean nouveau = true;
			for(CibleDto attl : attaque.getAttaquantList()) {
				if(attl.getCibleId() == pers.getPersoId()) nouveau = false;
			}
			for(CibleDto defl : attaque.getCibleList()) {
				if(defl.getCibleId() == pers.getPersoId()) nouveau = false;
			}
			if(nouveau) {
				CibleDto autre = new CibleDto(pers.getPersoId(), pers.getNom());
				attaque.getAutreList().add(autre);
			}
		});
		// comment je retrouve lePlusPossible ??? je ne fais pas
		return attaque;
	}
	
	@Transactional
	public void effacerLesActions(String partie) {
		actionRepo.removeByPartie(partie);
		ROUND = 0;
	}

	public Iterable<ActionDB> listForPartie(String partie) {
		return actionRepo.findByPartieOrderByActionTimeDesc(partie);
	}

	public void lancerAttaque(AttaqueDto attaque) {
		int refAttaque = 0;
		int na = attaque.getAttaquantList().size();
		int nd = attaque.getCibleList().size();
		
		List<ComboDto> comboAttList = new ArrayList<>();
		List<ComboDto> comboDefList = new ArrayList<>();
		int modTch = 0;
		int modFor = 0;
		List<String> descrList = new ArrayList<>();
		for(int i=0; i<na; i++) {
			PersonnageDB attaquant = persoRepo.findById(attaque.getAttaquantList().get(i).getCibleId()).get();
			PersoCompletDto attaquantDto = new PersoCompletDto(attaquant);
			ActionDto strDeAtt = new ActionDto();
			int bonusDuDeAtt = jetDAttaque(attaquant.getPdcCombat(), strDeAtt);
			if(attaque.getAttaquantList().get(i).getDe() != 0) {
				bonusDuDeAtt = jetDAttaquef(attaquant.getPdcCombat(), strDeAtt, attaque.getAttaquantList().get(i).getDe());
			}
			attaque.getAttaquantList().get(i).setDe(strDeAtt.getDe20());
			
			if(attaquant.getEtat() == null) {
				Optional<EtatDB> etatOpt = etatRepo.findById(attaquant.getPersoId());
				if(etatOpt.isPresent()) attaquant.setEtat(etatOpt.get());
				else attaquant.setEtat(new EtatDB(attaquant));
			}
			EtatDB etat = attaquant.getEtat();
			etat.setIncapacite(1);
			etatRepo.save(etat);
			
			for(int j=0; j<nd; j++) {
				PersonnageDB defenseur = persoRepo.findById(attaque.getCibleList().get(j).getCibleId()).get();
				PersoCompletDto defenseurDto = new PersoCompletDto(defenseur);
				Optional<EtatDB> etatDef = etatRepo.findById(defenseur.getPersoId());
				EtatDB etatget;
				if(etatDef.isPresent()) etatget = etatDef.get();
				else etatget = new EtatDB(persoRepo.findById(defenseur.getPersoId()).get());
				etatget.setDeDef(attaque.getCibleList().get(j).getDe());
				etatRepo.save(etatget);

				// mod tch/for
				if(na > 1) {
					modTch = na;
					modFor = na/2;
				} else if(nd > 1) {
					if(attaque.isLePlusPossible()) {
						modTch = -2 -2*j + combaPlusToucher(attaquant.getCCcombatPlusieurs());
						modFor = -1 -j + combaPlusForce(attaquant.getCCcombatPlusieurs());
					} else {
						modTch = -2 -(nd/2)*2 + combaPlusToucher(attaquant.getCCcombatPlusieurs());
						modFor = -(nd+1)/2 + combaPlusForce(attaquant.getCCcombatPlusieurs());
					}
					modTch = (modTch > 0 ? 0 : modTch);
					modFor = (modFor > 0 ? 0 : modFor);
				}
				//
				ComboDto comboDef = defenseurDto.getComboTotal();
				ActionDto strDeDef = new ActionDto();
				int bonusDef = jetDeDefense(defenseurDto, strDeDef);
				if(attaque.getCibleList().get(j).getDe() != 0) {
					bonusDef = jetDeDefensef(defenseurDto, strDeDef, attaque.getCibleList().get(j).getDe());
				}
				attaque.getCibleList().get(j).setDe(strDeDef.getDe20());		
				comboDef.appliqueBonusDefense(bonusDef);
				// combo
				ComboDto comboAtt = attaquantDto.getComboTotal();
				comboAtt.modifComboTF(bonusDuDeAtt, bonusDuDeAtt);
				comboAtt.modifComboTF(modTch, modFor);
				
				if(comboAtt.isGlobaux()) {
					comboDef.setEndPerso(comboDef.getEndPerso() + comboDef.getBouclier().BonusEndGlb());
					comboDef.setBouclier(Bouclier.Pas_de_bouclier);
				}
				
				comboAttList.add(comboAtt);
				comboDefList.add(comboDef);
				descrList.add(attaquantDto.getNom()+" attaque ("+strDeAtt.getDescription()+
						") "+defenseurDto.getNom()+" ("+strDeDef.getDescription()+") : ");
			}
		}
		
		int n = 0;
		for(int i=0; i<na; i++) {
			for(int j=0; j<nd; j++) {
				Degat dgt = attaqueUnique(comboAttList.get(n), comboDefList.get(n), attaque);
				PersonnageDB defenseur = persoRepo.findById(attaque.getCibleList().get(j).getCibleId()).get();
				if(comboAttList.get(n).isGlobaux()) dgt = diviseBlGlobaux(dgt, defenseur);
				ActionDB act = appliqueAttaque(dgt, attaque.getAttaquantList().get(i), defenseur, 
						attaque.getCibleList().get(j).getDe(), descrList.get(n), refAttaque, attaque);
				refAttaque = act.getRefAttaque();
				n++;
			}
		}
	}
	
	private Degat diviseBlGlobaux(Degat degats, PersonnageDB defenseur) {
		if(degats.getBlessList().isEmpty()) return degats;
		float nivMin = defenseur.getMinPourMineure();
		float nivToRep = 0;
		for(BlessureDto bl : degats.getBlessList()) {
			if(!bl.getPartieTouchee().equals("Bouclier") && !bl.getPartieTouchee().equals("Armure")) {
				nivToRep += bl.getNiveau();
				bl.setNiveau(0);
				bl.setPartieTouchee("points de choc");
			}
		}
		
		float[] repartition = new float[] {0, 0, 0, 0, 0, 0};
		String[] parties = new String[] {"Tête", "Tronc", "Bras droit", "Bras gauche", "Jambe droite", "Jambe gauche"};
		
		repartition = diviseNivGlob(repartition, nivToRep, nivMin);
		
		for(int i=0; i<repartition.length; i++) {
			if(repartition[i] > 0) {
				BlessureDto bl = new BlessureDto(repartition[i], 0);
				bl.setPartieTouchee(parties[i]);
				degats.getBlessList().add(bl);
			}
		}
		return degats;
	}

	private float[] diviseNivGlob(float[] repartition, float nivToRep, float nivMin) {
		int N = repartition.length;
		int i =new Random().nextInt(N);
		if(nivToRep <= nivMin) {
			repartition[i] += nivToRep;
		} else {
			repartition[i] += nivMin;
			repartition = diviseNivGlob(repartition, nivToRep - nivMin, nivMin);
		}
		return repartition;
	}

	public ActionDB appliqueAttaque(Degat degats, CibleDto attaquant, PersonnageDB defenseur, int de, String descr, int refAttaque, final AttaqueDto attaque) {
		ActionDB act = new ActionDB();
		act.setPersoId(attaquant.getCibleId());
		act.setActeurNom(attaquant.getCibleNom());
		act.setActeurDe(attaquant.getDe());
		act.setCibleId(defenseur.getPersoId());
		act.setCibleNom(defenseur.getNom());
		act.setCibleDe(de);
		act.setPartie(defenseur.getPartie());
		act.setActionTime(LocalDateTime.now());
		if(refAttaque == 0) {
			act = actionRepo.save(act);
			refAttaque = act.getActionId();
		}
		act.setRefAttaque(refAttaque);
		act = actionRepo.save(act);
		int refAction = act.getActionId();
		
		if(degats.getBlessList().isEmpty()) {
			if(degats.isPare()) {
				descr += "paré ("+modifToString(degats.getMargeToucher())+"), pas de dégâts ("+modifToString(degats.getMargeBlesser())+").";								
			} else if(degats.getMargeToucher() >= 0) {
				descr += "touché ("+modifToString(degats.getMargeToucher())+"), pas de dégâts ("+modifToString(degats.getMargeBlesser())+").";				
			} else {
				descr += "raté ("+modifToString(degats.getMargeToucher())+") !";				
			}
		} else {
			if(degats.isPare())
				descr += "paré ("+modifToString(degats.getMargeToucher())+"), dégâts ("+modifToString(degats.getMargeBlesser())+") : ";
			else
				descr += "touché ("+modifToString(degats.getMargeToucher())+"), dégâts ("+modifToString(degats.getMargeBlesser())+") : ";
			for(BlessureDto bl : degats.getBlessList()) {
				BlessureDB blToSave = bl.blessureToDB(defenseur);
				blToSave.setRefAction(refAction);
				blessRepo.save(blToSave);
				descr += blToSave.blessureToString()+" ; ";
				if(blToSave.getHandicap() > 0) {
//					defenseur.addHandicap(refAction, blToSave.getHandicap(), TypeHand.FATIGUE, "Blessure");
					if(defenseur.getEtat() == null) {
						Optional<EtatDB> etatOpt = etatRepo.findById(defenseur.getPersoId());
						if(etatOpt.isPresent()) defenseur.setEtat(etatOpt.get());
						else defenseur.setEtat(new EtatDB(defenseur));
					}
					EtatDB etat = defenseur.getEtat();
					switch(etat.getIncapacite()) {
						case 0: etat.setIncapacite(2); break;
						case 1: etat.setIncapacite(3); break;
						case 2: etat.setIncapacite(2); break;
						case 3: etat.setIncapacite(3); break;
					}
					persoRepo.save(defenseur);
				}
			}
		}
		
		for(HandicapDto h : degats.getHandList()) {
			descr += "("+h.getNomHand()+" : "+h.getNombre()+" H-) ";
			defenseur.replaceHandicap(refAction, h.getNombre(), h.getTypeHand(), h.getNomHand());
			persoRepo.save(defenseur);
		}
		
		act.setDescription(descr);
		act = actionRepo.save(act);
		return act;
	}
	
	private String modifToString(int modif) {
		return ""+(modif >= 0 ? "+" : "")+modif+"";
	}

	public Degat attaqueUnique(ComboDto comboAtt, ComboDto comboDef, final AttaqueDto attaque) {
		Degat result = new Degat();
	
		int margeTch = comboAtt.getToucher() - (comboAtt.isCaC() ? comboDef.getDefense() : comboDef.getEsquive());
		int endu = 0;
		result.setMargeToucher(margeTch);
		if(margeTch < 0) {
			return result;
		} else {
			if(!attaque.isCoupDansLeBouclier())
				margeTch -= (comboDef.getBouclier() != Bouclier.Pas_de_bouclier ? comboDef.getParade() + comboAtt.getPrdEnnemie() : 0);
			result.setMargeToucher(margeTch);
			int bonusForce = (margeTch-1)/3;
			bonusForce = (bonusForce > 0 ? bonusForce : 0);
			boolean isPare = (comboDef.getBouclier() != Bouclier.Pas_de_bouclier) && (margeTch <= 0);
			if(attaque.isCoupDansLeBouclier()) isPare = true;
			result.setPare(isPare);
			String partieTouchee ="";
			if(isPare) {
				BlessureDto blBcl = calculDegats(bonusForce+comboAtt.getForce(), comboDef.getEndBouclier(), 
						comboAtt.getTypeDgts(),	comboAtt.isGlobaux(), comboAtt.getElement(), true);
				result.setMargeBlesser(bonusForce+comboAtt.getForce() - comboDef.getEndBouclier());

				if(blBcl != null) {
					partieTouchee = "Bouclier";
					blBcl.setPartieTouchee(partieTouchee);
					blBcl.setPtDeChoc(0);
					if(comboAtt.getTypeDgts()==Dgts.PRF) blBcl.setDemiNiveau(blBcl.getDemiNiveau()/2);
					if(blBcl.getNiveau() > 0) result.getBlessList().add(blBcl);
				}
				
				partieTouchee = "Bras-bouclier";
				endu = 4 + (comboDef.getEndPerso() > comboDef.getEndBouclier() ? comboDef.getEndPerso() : comboDef.getEndBouclier());
			} else {
				partieTouchee = partieTouchee();
				endu = comboDef.getEndPerso();
			}
			BlessureDto bl = calculDegats(bonusForce+comboAtt.getForce(), endu, 
					comboAtt.getTypeDgts(),	comboAtt.isGlobaux(), comboAtt.getElement(), false);
			if(!isPare)
				result.setMargeBlesser(bonusForce+comboAtt.getForce() - endu);
			
			if(bl != null) {
				bl.setPartieTouchee(partieTouchee);
				result.getBlessList().add(bl);
				BlessureDto blArmure = calculDegats(bonusForce+comboAtt.getForce(), endu, 
						comboAtt.getTypeDgts(),	comboAtt.isGlobaux(), comboAtt.getElement(), true);
				blArmure.setPartieTouchee("Armure");
				if(blArmure.getNiveau() > 0) result.getBlessList().add(blArmure);
			}

			if(attaque.isBousculade()) {
				int demiH = (result.getMargeBlesser() + comboAtt.getIBatt() - comboDef.getIBdef())/2;
				if(demiH > 0)	result.getHandList().add(new HandicapDto((float) demiH/2, TypeHand.MOBILITE, "Bousculade"));
			}
			
			return result;
		}
	}
	
	private BlessureDto calculDegats(int force, int endu, Dgts dgts, boolean isGlb, Element element, boolean isObjet) {
		int margeBless = force - endu;
		if(margeBless <= 0) return null;
		int ptChoc = 0;
		float nvBl = (float) 0.0;
		if(!isObjet) ptChoc = dgts.calculPtChoc(margeBless);
		nvBl = dgts.calculNvBl(margeBless, isObjet);
		if(isGlb) nvBl += 1;
		ptChoc = element.modifPtChoc(ptChoc, (dgts == Dgts.CTD));
		nvBl = element.modifNvBl(nvBl, (dgts == Dgts.CTD), isObjet);

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
			case 16: return "Tibia droit";
			case 17: return "Tibia gauche";
			case 18: return "Bassin";
			case 19: return "Pied droit ou genou";
			case 20: return "Pied gauche ou genou";
			default: return "";
		}
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
		str.setDe20(de);
		str.setDescription(Integer.toString(de));
		if(de==1) str.setDescription("EC !");
		return bonusD20(bonusMin, de);
	}
	
	public int jetDAttaque(int pdc, ActionDto str) { return jetDAttaquef(pdc, str, 0); }
	public int jetDAttaquef(int pdc, ActionDto str, int forcage) {
		int bonusMin = -10;
		boolean comp = (pdc != 0);		
		int de = D20f(comp, forcage);
		str.setDe20(de);
		str.setDescription(Integer.toString(de));
		if(de==1) str.setDescription("EC !");
		return bonusD20(bonusMin, de);
	}
	
	public int jetDeDefense(PersoCompletDto defenseur, ActionDto str) { return jetDeDefensef(defenseur, str, 0); }
	public int jetDeDefensef(PersoCompletDto defenseur, ActionDto str, int forcage) {
		int pdc = defenseur.getPdcCombat();
		Optional<EtatDB> etat = etatRepo.findById(defenseur.getPersoId());
		if(forcage == 0) {
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

	public int nouveauRound(String partie) {
		if(ROUND != 0) 		actionRepo.save(new ActionDB(partie, "<pre>##########     Fin du round "+ROUND+"     ##########</pre>"));
		ROUND += 1;
		if(ROUND % 2 == 1)	actionRepo.save(new ActionDB(partie, "<pre>#         Round "+ROUND+" : faites vos ATR         #</pre>"));
		else 				actionRepo.save(new ActionDB(partie, "<pre>##########    Début du round "+ROUND+"    ##########</pre>"));
		return ROUND;
	}

}
