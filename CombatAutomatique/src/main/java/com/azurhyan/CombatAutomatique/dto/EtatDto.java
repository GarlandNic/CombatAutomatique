package com.azurhyan.CombatAutomatique.dto;

import com.azurhyan.CombatAutomatique.model.EtatDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;

import lombok.Data;

@Data
public class EtatDto {
	
	int deDef;
	int turnOrder;
	int incapacite;
	
	public EtatDB etatToDB(PersonnageDB perso) {
		EtatDB etat;
		if(perso.getEtat() != null) {
			etat = perso.getEtat();
		} else {
			etat = new EtatDB(perso);
		}
		etat.setDeDef(this.deDef);
		etat.setIncapacite(this.incapacite);
		if(this.turnOrder == 0) this.turnOrder = perso.getPersoId();
		etat.setTurnOrder(this.turnOrder);
		etat.setPerso(perso);
		return etat;
	}
	
	public EtatDto() {
	}
	
	public EtatDto(EtatDB etat) {
		if(etat != null) {
			this.deDef = etat.getDeDef();
			this.incapacite = etat.getIncapacite();
			this.turnOrder = etat.getTurnOrder();
		}
	}

}
