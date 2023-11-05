package com.azurhyan.CombatAutomatique.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "actions")
public class ActionDB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	int actionId;
	
//	 FOREIGN KEY (ACTEURID) REFERENCES personnages (ID) ON DELETE CASCADE ON UPDATE CASCADE
	@Column(name="ACTEURID")
	int persoId;
	
	@ManyToOne
	@JoinColumn(name="persoId")
	PersonnageDB acteur;
	
	String acteurNom;
	
	LocalDate actionTime;

	String description;
	
	public String getPartie() {
		return acteur.getPartie();
	}

}
