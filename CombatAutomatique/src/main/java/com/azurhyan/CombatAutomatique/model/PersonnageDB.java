package com.azurhyan.CombatAutomatique.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "personnages")
public class PersonnageDB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	String nom;
	
	String joueur;
	
	String partie;
	
	boolean visible;
	
	@OneToMany(cascade = CascadeType.ALL, 
			   orphanRemoval = true, 
			   fetch = FetchType.EAGER)
	@JoinColumn(name = "personnage")
	List<ComboDB> comboList = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, 
			   orphanRemoval = true, 
			   fetch = FetchType.EAGER)
	@JoinColumn(name = "personnage")
	List<BlessureDB> blessureList;
	
	int CON;
	
	int Hfatigue;
	
	int CCinfatigable;
	
	int Hmobilite;
	
	int CCincoercible;
	
	int Hsens;
	
	int CCtoujoursPret;
	
	int CCcombatPlusieurs;

}
