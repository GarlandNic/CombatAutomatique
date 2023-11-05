package com.azurhyan.CombatAutomatique.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "combos")
public class ComboDB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	int comboId;
	
//	 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (ID) ON DELETE CASCADE ON UPDATE CASCADE
	@Column(name="PERSONNAGE")
	int persoId;
	
	String nom;
	
	int init=15;

	boolean CaC=true;

	int toucher=15;

	int prdEnnemie=0;
	
	@Column(name="FORCEATTAQUE")
	int force=15;

	int IBatt=15;
	
	@Column(name="DEFENSE")
	int defense=15;
	
	int esquive=15;
	
	int parade=0;
	
	int endBouclier=15;
	
	int endPerso=15;
	
	int IBdef=15;

}
