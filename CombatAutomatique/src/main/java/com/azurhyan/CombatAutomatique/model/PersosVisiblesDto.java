package com.azurhyan.CombatAutomatique.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class PersosVisiblesDto {
	
	List<PersoVisible> persoList;

}
