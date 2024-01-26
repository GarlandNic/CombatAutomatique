package com.azurhyan.CombatAutomatique.dto;

import java.util.ArrayList;
import java.util.List;

import com.azurhyan.CombatAutomatique.model.ActionDB;
import com.azurhyan.CombatAutomatique.model.BlessureDB;
import com.azurhyan.CombatAutomatique.model.ComboDB;
import com.azurhyan.CombatAutomatique.model.EtatDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;

import jakarta.persistence.Entity;

import lombok.Data;

@Data
public class PersoCompletDto {
	
	public PersoCompletDto() {
	}
	
	public PersoCompletDto(String partie) {
		this.partie = partie;
		this.comboList = new ArrayList<>();
		this.comboList.add(new ComboDto("Base", false));
		this.comboList.add(new ComboDto("Bonus permanent", true));
		this.comboList.add(new ComboDto("Bonus 1 round", true));
//		this.etat = new EtatDto();
	}
	
	public PersoCompletDto(PersonnageDB perso) {
		this.persoId = perso.getPersoId();
		this.nom = perso.getNom();
		this.joueur = perso.getJoueur();
		this.partie = perso.getPartie();
		this.visible = perso.isVisible();
		this.CON=perso.getCON();
		this.pdcCombat=perso.getPdcCombat();
		this.Hfatigue=perso.getHfatigue2();
		this.CCinfatigable=perso.getCCinfatigable2();
		this.Hmobilite=perso.getHmobilite2();
		this.CCincoercible=perso.getCCincoercible2();
		this.Hsens=perso.getHsens2();
		this.CCtoujoursPret=perso.getCCtoujoursPret2();
		this.CCcombatPlusieurs=perso.getCCcombatPlusieurs();
		
		this.comboList = new ArrayList<>();
		for(ComboDB combo : perso.getComboList()) {
			this.comboList.add(new ComboDto(combo));
		}
		this.blessureList = new ArrayList<>();
		for(BlessureDB bl : perso.getBlessureList()) {
			this.blessureList.add(new BlessureDto(bl));
		}
//		this.actionList = new ArrayList<>();
//		for(ActionDB action : perso.getActionList()) {
//			this.actionList.add(new ActionDto(action));
//		}
//		this.etat = new EtatDto(perso.getEtat());

	}
	
	public PersonnageDB persoToDB() {
		PersonnageDB perso = new PersonnageDB();
		perso.setPersoId(persoId);
		perso.setNom(nom);
		perso.setJoueur(joueur);
		perso.setPartie(partie);
		perso.setVisible(visible);
		perso.setCON(CON);
		perso.setPdcCombat(pdcCombat);
		perso.setHfatigue2(this.Hfatigue);
		perso.setCCinfatigable2(this.CCinfatigable);
		perso.setHmobilite2(this.Hmobilite);
		perso.setCCincoercible2(this.CCincoercible);
		perso.setHsens2(this.Hsens);
		perso.setCCtoujoursPret2(this.CCtoujoursPret);
		perso.setCCcombatPlusieurs(CCcombatPlusieurs);
		
//		List<ActionDB> actList = new ArrayList<>();
//		this.actionList.forEach(act -> actList.add(act.actionToDB(perso)));
//		perso.setActionList(actList);
		
		List<BlessureDB> blList = new ArrayList<>();
		this.blessureList.forEach(bl -> blList.add(bl.blessureToDB(perso)));
		perso.setBlessureList(blList);
		
		List<ComboDB> combList = new ArrayList<>();
		this.comboList.forEach(comb -> combList.add(comb.comboToDB(perso)));
		perso.setComboList(combList);
		
//		if(etat != null) perso.setEtat(etat.etatToDB(perso));
		
		return perso;
	}
	
	int persoId;
	String nom="Perso";
	String joueur="PNJ";
	String partie;
	boolean visible=false;

	List<ComboDto> comboList = new ArrayList<>();
	List<BlessureDto> blessureList = new ArrayList<>();
//	List<ActionDto> actionList = new ArrayList<>();
//	EtatDto etat;
	
	int CON=10;
	int pdcCombat=3;
	float Hfatigue=0;
	float CCinfatigable=0;
	float Hmobilite=0;
	float CCincoercible=0;
	float Hsens=0;
	float CCtoujoursPret=0;
	int CCcombatPlusieurs=0;
	
	public float totalBlessures() {
		float res = (float) 0.0;
		for(BlessureDto bl : blessureList) {
			if( !(bl.getPartieTouchee().equals("Bouclier") || bl.getPartieTouchee().equals("Armure")) )
				res += bl.getNiveau();
		}
		return res;
	}

	public int totalPtsDeChoc() {
		int res = 0;
		for(BlessureDto bl : blessureList) {
			res += bl.getPtDeChoc();
		}
		return res;
	}
	
	public float getHandicap(BlessureDto bl) {
		if(bl.getPartieTouchee().equals("Bouclier")) return 0;
		if(bl.getPartieTouchee().equals("Armure")) return 0;
		float niv = bl.getNiveau();
		int CON = this.CON;
		if(niv+0.5 > 0.7*CON) {
			return 10;
		}
		if(niv+0.5 > 0.5*CON) {
			return (float) 4.5;
		}
		if(niv+0.5 > 0.4*CON) {
			return 3;
		}
		if(niv+0.5 > 0.3*CON) {
			return 2;
		}
		if(niv+0.5 > 0.2*CON) {
			return 1;
		}
		if(niv+0.5 > 0.1*CON) {
			return (float) 0.5;
		}
		return 0;
	}

	public String getGravite(BlessureDto bl) {
		if(bl.getPartieTouchee().equals("Bouclier")) return "";
		if(bl.getPartieTouchee().equals("Armure")) return "";
		int demiHandicap = Math.max(0, Math.round(this.getHandicap(bl)*2));
		switch(demiHandicap) {
			case 0: return "égratignure";
			case 1: return "mineure (0,5 H-)";
			case 2: return "légère (1 H-)";
			case 4: return "moyenne (2 H-)";
			case 6: return "grave (3 H-)";
			case 9: return "mortelle (4,5 H-)";
			case 20: return "mort";
		}
		return "";
	}

//	public void addBlessure(BlessureDto bl) {
//		this.blessureList.add(bl);
//		this.Hfatigue += this.getHandicap(bl);
//	}
	
	public ComboDto getComboHandicap() {
		float handicap = Math.max(0, this.getHfatigue() - this.getCCinfatigable());
		handicap += Math.max(0, this.getHmobilite() - this.getCCincoercible());
		handicap += Math.max(0, this.getHsens() - this.getCCtoujoursPret());
		ComboDto comboH = new ComboDto("Handicap", true);
		comboH.addHandicap(handicap);
		return comboH;
	}

	public ComboDto getComboTotal() {
		ComboDto comboTot = this.getComboHandicap();
		this.getComboList().forEach(combo -> {
			if(combo.isActif()) {
				if(combo.getNom().contains("Base")) {
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

			}
		});
		
		return comboTot;
	}


}
