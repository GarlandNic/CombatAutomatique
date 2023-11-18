package com.azurhyan.CombatAutomatique.dto;

import java.util.ArrayList;
import java.util.List;

import com.azurhyan.CombatAutomatique.model.PersonnageDB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class PersosVisiblesDto {
	
	List<PersonnageDB> persoList = new ArrayList<>();

}
