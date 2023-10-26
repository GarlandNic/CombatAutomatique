package com.azurhyan.CombatAutomatique.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class Personnage {
	
	String id;
	
	String nom;
	
	String joueur;
	
	String partie;
	
	boolean visible=true;
	
	Carac carac;
	
	

}
