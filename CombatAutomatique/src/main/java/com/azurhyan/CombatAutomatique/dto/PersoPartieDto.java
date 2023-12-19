package com.azurhyan.CombatAutomatique.dto;

import com.azurhyan.CombatAutomatique.model.PersonnageDB;

import lombok.Data;

@Data
public class PersoPartieDto {
	
	public PersoPartieDto() {
	}
	
	public PersoPartieDto(PersonnageDB perso) {
		this.id = perso.getPersoId();
		this.nom = perso.getNom();
		this.init = -(perso.getHfatigue() > perso.getCCinfatigable() ? (perso.getHfatigue() - perso.getCCinfatigable())/2 : 0) 
				-(perso.getHmobilite() > perso.getCCincoercible() ? (perso.getHmobilite() - perso.getCCincoercible())/2 : 0)
				-(perso.getHsens() > perso.getCCtoujoursPret() ? (perso.getHsens() - perso.getCCtoujoursPret())/2 : 0);
		perso.getComboList().forEach(combo -> this.init = this.init + combo.getInit());
		this.visible = perso.isVisible();
		this.deDef = (null != perso.getEtat() ? perso.getEtat().getDeDef() : 0);
	}

	int id;
	
	String nom;
	
	int init;
	
	boolean visible;
	
	int deDef;

}
