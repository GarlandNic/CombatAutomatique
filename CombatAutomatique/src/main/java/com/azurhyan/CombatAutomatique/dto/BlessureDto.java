package com.azurhyan.CombatAutomatique.dto;

import com.azurhyan.CombatAutomatique.model.BlessureDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class BlessureDto {
	
	float niveau=0;
	
	int ptDeChoc=0;
	
	String partieTouchee;
	
	public BlessureDto(float niv, int pdc) {
		this.niveau = niv;
		this.ptDeChoc = pdc;
		this.partieTouchee = "";
	}
	
	public BlessureDto() {
	}

	public BlessureDto(BlessureDB bl) {
		this.niveau = bl.getNiveau();
		this.ptDeChoc = bl.getPtDeChoc();
		this.partieTouchee = bl.getPartieTouchee();
	}
	
	public BlessureDB blessureToDB(PersonnageDB perso) {
		BlessureDB bl = new BlessureDB(perso, this.niveau, this.ptDeChoc);
		bl.setPartieTouchee(partieTouchee);
		return bl;
	}

}
