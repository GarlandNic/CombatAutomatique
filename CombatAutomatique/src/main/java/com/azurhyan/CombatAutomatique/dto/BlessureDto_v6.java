package com.azurhyan.CombatAutomatique.dto;

import com.azurhyan.CombatAutomatique.model.BlessureDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class BlessureDto_v6 {
	
	int refAction=0;
	
	float niveau=0;
	
	int ptDeChoc=0;
	
	String partieTouchee;
	
	public BlessureDto_v6(float niv, int pdc, String partie) {
		this.niveau = niv;
		this.ptDeChoc = pdc;
		this.partieTouchee = partie;
	}
	
	public BlessureDto_v6(float niv, int pdc) {
		this.niveau = niv;
		this.ptDeChoc = pdc;
		this.partieTouchee = "";
	}
	
	public BlessureDto_v6() {
	}

	public BlessureDto_v6(BlessureDB bl) {
		this.niveau = bl.getNiveau();
		this.ptDeChoc = bl.getPtDeChoc();
		this.partieTouchee = bl.getPartieTouchee();
		this.refAction = bl.getRefAction();
	}
	
	public BlessureDB blessureToDB(PersonnageDB perso) {
		BlessureDB bl = new BlessureDB(perso, this.niveau, this.ptDeChoc);
		bl.setPartieTouchee(partieTouchee);
		bl.setRefAction(refAction);
		return bl;
	}

	public int getDemiNiveau() {
		return Math.round(this.getNiveau()*2);
	}

	public void setDemiNiveau(int demiNiv) {
		this.setNiveau( ((float) demiNiv)/2 );
	}

}
