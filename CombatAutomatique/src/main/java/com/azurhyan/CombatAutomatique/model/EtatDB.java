package com.azurhyan.CombatAutomatique.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="etat")
public class EtatDB {
	
	@Id
	@Column(name="PERSONNAGE")
	int personnage;
	
	@Column(name="DEDEF")
	int deDef;
	
	@Column(name="TURNORDER")
	int turnOrder;
	
	@Column(name="INCAPACITE")
	int incapacite;
	
    @OneToOne
    @MapsId
    @JoinColumn(name = "PERSONNAGE")
    private PersonnageDB perso;

	public EtatDB(PersonnageDB p) {
		this.perso = p;
		this.turnOrder = p.getPersoId();
	}
	
	public EtatDB() {
	}
}
