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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "personnages")
public class PersonnageDB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	int persoId;
	
	String nom="Perso";
	
	String joueur="PNJ";
	
	String partie;
	
	boolean visible=true;
	
	@OneToMany(cascade = CascadeType.ALL, 
			   orphanRemoval = true, 
			   fetch = FetchType.EAGER)
	@JoinColumn(name = "persoId")
	List<ComboDB> comboList = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, 
			   orphanRemoval = true, 
			   fetch = FetchType.EAGER)
	@JoinColumn(name = "persoId")
	List<BlessureDB> blessureList = new ArrayList<>();
	
	@OneToMany
	@JoinColumn(name = "persoId")
	List<ActionDB> actionList = new ArrayList<>();
	
	int CON=10;
	
	int Hfatigue=0;
	
	int CCinfatigable=0;
	
	int Hmobilite=0;
	
	int CCincoercible=0;
	
	int Hsens=0;
	
	int CCtoujoursPret=0;
	
	int CCcombatPlusieurs=0;
	
	public PersonnageDB(String partie) {
		this.partie = partie;
	}

}
