package com.azurhyan.CombatAutomatique.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class PersosVisiblesDto {
	
	List<PersoVisible> persoList;

}
