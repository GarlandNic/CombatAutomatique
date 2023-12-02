package com.azurhyan.CombatAutomatique.model;

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
@Table(name="etat")
public class EtatDB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ETATID")
	int etatId;
	
//	@Column(name="PERSONNAGE")
//	int persoId;
	
	@ManyToOne
	@JoinColumn(name="PERSONNAGE")
	PersonnageDB perso;
	
	@Column(name="DEDEF")
	int deDef;
	
	@Column(name="TURNORDER")
	int turnOrder;
	
	@Column(name="INCAPACITE")
	int incapacite;

	public EtatDB(PersonnageDB p) {
		this.perso = p;
		this.turnOrder = p.getPersoId();
	}
}
