package com.azurhyan.CombatAutomatique.dto;

import com.azurhyan.CombatAutomatique.model.PersonnageDB;

import lombok.Data;

@Data
public class PersoPartieDto {
	
	public PersoPartieDto(PersonnageDB perso) {
		this.id = perso.getPersoId();
		this.nom = perso.getNom();
		this.init = 0;
		perso.getComboList().forEach(combo -> this.init = this.init + combo.getInit());
	}

	public int id;
	
	public String nom;
	
	public int init;

}
