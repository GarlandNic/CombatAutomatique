package com.azurhyan.CombatAutomatique.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "actions")
public class ActionDB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ACTIOID")
	int actionId;
	
//	 FOREIGN KEY (ACTEURID) REFERENCES personnages (ID) ON DELETE CASCADE ON UPDATE CASCADE
	@Column(name="ACTEURID")
	int persoId;
	
	@Column(name="PARTIE")
	String partie;
	
//	@ManyToOne
//	@JoinColumn(name="ACTEURID")
//	PersonnageDB acteur;
	
	@Column(name="ACTEURNOM")
	String acteurNom;
	
	@Column(name="ACTIONTIME")
	LocalDateTime actionTime;

	@Column(name="DESCRIPTION")
	String description;
	
//	public String getPartie() {
//		return acteur.getPartie();
//	}
	
	public void addDescription(String txt) {
		this.description += txt;
	}

}
