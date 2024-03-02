package com.azurhyan.CombatAutomatique.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "handicaps")
public class HandicapDB {
	
	public static enum TypeHand {
		FATIGUE,
		MOBILITE,
		SENS;
		
		@Override
		public String toString() {
			switch (this) {
	        	case FATIGUE:
	        		return "Fatigue";
	        	case MOBILITE:
	        		return "Mobilité";
	        	case SENS:
	        		return "Sens";
	        	default:
	        		return null;
			}
		}
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="HANDID")
	int handicapId;
	
//	add FOREIGN KEY (REFACTION) REFERENCES actions (ACTIOID) ON DELETE SET NULL ON UPDATE SET NULL;
	@Column(name="REFACTION")
	int refAction=0;
	
//	@Column(name="PERSONNAGE")
//	int persoId;
	
	@ManyToOne
	@JoinColumn(name="PERSONNAGE")
	PersonnageDB perso;
	
	@Column(name="DEMINOMBRE")
	int demiNombre;
	
	@Column(name="TYPEHAND")
	@Enumerated(EnumType.STRING)
	TypeHand typeHand;
	
	@Column(name="NOMHAND")
	String nomHand;
	
	public float getNombre() {
		return ((float) this.demiNombre)/2;
	}
	
	public void setNombre(float h) {
		this.demiNombre = Math.max(0, Math.round(h*2));
	}
	
	public HandicapDB(PersonnageDB perso, float nb, TypeHand typ) {
		this.perso = perso;
		this.setNombre(nb);
		this.typeHand = typ;
		this.nomHand = "";
	}
	
	public HandicapDB(PersonnageDB perso, float nb, TypeHand typ, String nom) {
		this.perso = perso;
		this.setNombre(nb);
		this.typeHand = typ;
		this.nomHand = nom;
	}
	
	public HandicapDB() {
	}
	
	public HandicapDB copy(PersonnageDB newPerso) {
		HandicapDB hand = new HandicapDB();
		hand.setDemiNombre(this.getDemiNombre());
		hand.setNomHand(this.getNomHand());
		hand.setTypeHand(this.getTypeHand());
		hand.setRefAction(this.getRefAction());
		hand.setPerso(newPerso);
		return hand;
	}

}
