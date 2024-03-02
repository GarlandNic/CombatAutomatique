package com.azurhyan.CombatAutomatique.dto;

import com.azurhyan.CombatAutomatique.model.BlessureDB;
import com.azurhyan.CombatAutomatique.model.HandicapDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.model.HandicapDB.TypeHand;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class HandicapDto {
	
	int refAction=0;
	
	float nombre = 0;
	
	TypeHand typeHand = TypeHand.FATIGUE;
	
	String nomHand;
	
	public HandicapDto(float nb, TypeHand typ, String nom) {
		this.nombre = nb;
		this.typeHand = typ;
		this.nomHand = nom;
	}
	
	public HandicapDto() {
	}

	public HandicapDto(HandicapDB h) {
		this.nombre = h.getNombre();
		this.typeHand = h.getTypeHand();
		this.nomHand = h.getNomHand();
		this.refAction = h.getRefAction();
	}
	
	public HandicapDB handicapToDB(PersonnageDB perso) {
		HandicapDB h = new HandicapDB(perso, this.nombre, this.typeHand, this.nomHand);
		h.setRefAction(refAction);
		return h;
	}

	public int getDemiNombre() {
		return Math.round(this.getNombre()*2);
	}

	public void setDemiNombre(int demiNiv) {
		this.setNombre( ((float) demiNiv)/2 );
	}

}
