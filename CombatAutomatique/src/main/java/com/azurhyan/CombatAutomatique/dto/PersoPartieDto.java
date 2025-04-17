package com.azurhyan.CombatAutomatique.dto;

import com.azurhyan.CombatAutomatique.model.ActionDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;

import lombok.Data;

@Data
public class PersoPartieDto {
	
	public PersoPartieDto() {
	}
	
	public PersoPartieDto(PersonnageDB perso) {
		this.id = perso.getPersoId();
		this.nom = perso.getNom();
		this.init = -perso.totalHandicaps();
		perso.getComboList().forEach(combo -> {
			if(combo.isActif()) this.init += combo.getInit();
		});
		this.visible = perso.isVisible();
		this.deDef = (null != perso.getEtat() ? perso.getEtat().getDeDef() : 0);
		this.bles360 = perso.totalDemiNiveauxBlessures()*360/(perso.getCON()*2);
		this.choc360 = perso.totalPtsDeChoc()*360/(perso.getCON()*2+1);
		this.hand = perso.totalHandicaps();
		this.incapacite = (null != perso.getEtat() ? perso.getEtat().getIncapacite() : 0);
		this.turnOrder = (null != perso.getEtat() ? perso.getEtat().getTurnOrder() : perso.getPersoId());
		this.joueur = perso.getJoueur();
		this.bouclier = perso.hasBouclier();
		this.nivBcl = ((float) perso.demiNiveauxBouclier())/2;
	}

	int id;
	
	String nom;
	String joueur;
	
	int init;
	
	boolean visible;
	
	int deDef;
	
	int bles360;
	int choc360;
	int hand;
	
	int incapacite;
	
	int turnOrder;
	
	boolean bouclier;
	float nivBcl;
	
	public String getCouleur() {
		return ActionDB.getCouleur(this.nom);
	}

}
