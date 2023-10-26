package com.azurhyan.CombatAutomatique.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "caracs")
public class Carac {
	
	String id;
	
	int AGI=9;
	int CHA=9;
	int CON=9;
	int ESP=9;
	int HAB=9;
	int INT=9;
	int PER=9;

}
