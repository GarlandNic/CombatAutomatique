package com.azurhyan.CombatAutomatique.dto;

import com.azurhyan.CombatAutomatique.model.ComboDB;
import com.azurhyan.CombatAutomatique.model.ComboDB.Dgts;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;

import lombok.Data;

@Data
public class ComboDto {

	public ComboDto(String nom, boolean mod) {
		this.nom = nom;
		if(mod) {
			this.init = 0;
			this.toucher = 0;
			this.prdEnnemie = 0;
			this.force = 0;
			this.IBatt = 0;
			this.defense = 0;
			this.esquive = 0;
			this.parade = 0;
			this.endBouclier = 0;
			this.endPerso = 0;
			this.IBdef = 0;
		}
	}
	
	public ComboDto() {
	}
	
	public ComboDto(ComboDB combo) {
		this.nom = combo.getNom();
		this.init = combo.getInit();
		this.typeDgts = combo.getTypeDgts();
		this.CaC = combo.isCaC();
		this.toucher = combo.getToucher();
		this.prdEnnemie = combo.getPrdEnnemie();
		this.force = combo.getForce();
		this.IBatt = combo.getIBatt();
		this.defense = combo.getDefense();
		this.esquive = combo.getEsquive();
		this.parade = combo.getParade();
		this.endBouclier = combo.getEndBouclier();
		this.endPerso = combo.getEndPerso();
		this.IBdef = combo.getIBdef();
		this.actif = combo.isActif();
	}
	
	public ComboDB comboToDB(PersonnageDB perso) {
		ComboDB comb = new ComboDB(this.nom, perso, false);
		comb.setInit(init);
		comb.setTypeDgts(typeDgts);
		comb.setCaC(CaC);
		comb.setToucher(toucher);
		comb.setPrdEnnemie(prdEnnemie);
		comb.setForce(force);
		comb.setIBatt(IBatt);
		comb.setDefense(defense);
		comb.setEsquive(esquive);
		comb.setParade(parade);
		comb.setEndBouclier(endBouclier);
		comb.setEndPerso(endPerso);
		comb.setIBdef(IBdef);
		comb.setActif(actif);
		return comb;
	}

	String nom;
	
	int init=15;

	Dgts typeDgts=Dgts.NOR;
	
	boolean CaC=true;
	
	int toucher=15;
	int prdEnnemie=0;
	int force=15;
	int IBatt=15;
	int defense=15;
	int esquive=15;
	int parade=0;
	int endBouclier=15;
	int endPerso=15;
	int IBdef=15;
	boolean actif=true;
	
	public void addHandicap(int demiH) {
		int h = demiH/2;
		this.init = this.init - h;
		this.toucher = this.toucher - h;
		this.force = this.force - h;
		this.IBatt = this.IBatt - h;
		this.defense = this.defense - h;
		this.esquive = this.esquive - h;
		this.endBouclier = this.endBouclier - h;
		this.endPerso = this.endPerso - h;
		this.IBdef = this.IBdef - h;
	}
	
	public void addHandicap(float hp) {
		this.addHandicap(Math.round(hp*2));
	}
	
	public void modifComboTF(int toucher, int force) {
		this.toucher += toucher;
		this.force += force;
	}
	
	public void appliqueBonusDefense(int bonus) {
		this.defense += bonus;
		this.esquive += bonus;
		this.endBouclier += bonus;
		this.endPerso += bonus;
		this.IBdef += bonus;
	}

}
