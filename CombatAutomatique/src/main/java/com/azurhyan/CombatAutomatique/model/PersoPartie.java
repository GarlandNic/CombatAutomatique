package com.azurhyan.CombatAutomatique.model;

public class PersoPartie {
	
	public PersoPartie(PersonnageDB perso) {
		this.id = perso.getPersoId();
		this.nom = perso.getNom();
		this.init = 0;
		perso.getComboList().forEach(combo -> this.init = this.init + combo.getInit());
	}

	int id;
	
	String nom;
	
	int init;

}
