package com.azurhyan.CombatAutomatique.dto;

import java.time.LocalDateTime;

import com.azurhyan.CombatAutomatique.model.ActionDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class ActionDto {
	
	public ActionDto(ActionDB action) {
		this.acteurNom = action.getActeurNom();
		this.actionTime = action.getActionTime();
		this.description = action.getDescription();
	}
	
	public ActionDto() {
	}

	public ActionDB actionToDB(PersonnageDB perso) {
		ActionDB act = new ActionDB();
		act.setActionId(perso.getPersoId());
		act.setActeurNom(this.acteurNom);
		act.setActionTime(this.actionTime);
		act.setDescription(this.description);
		return act;
	}

	String acteurNom;
	
	LocalDateTime actionTime;

	String description;
	
	int de20;

}
