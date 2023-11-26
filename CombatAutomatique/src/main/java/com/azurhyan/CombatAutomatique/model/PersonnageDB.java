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
	
	public PersonnageDB() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="PERSOID")
	int persoId;
	
	@Column(name="NOM")
	String nom="Perso";
	
	@Column(name="JOUEUR")
	String joueur="PNJ";
	
	@Column(name="PARTIE")
	String partie;
	
	@Column(name="VISIBLE")
	boolean visible=true;
	
	@OneToMany(cascade = CascadeType.ALL, 
			   orphanRemoval = true, 
			   fetch = FetchType.EAGER, 
			   mappedBy = "perso")
	List<ComboDB> comboList = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, 
			   orphanRemoval = true, 
			   fetch = FetchType.EAGER, 
			   mappedBy = "perso")
	List<BlessureDB> blessureList = new ArrayList<>();
	
//	@OneToMany(mappedBy = "persoId")
//	List<ActionDB> actionList = new ArrayList<>();
	
	@Column(name="CON")
	int CON=10;
	
	@Column(name="PDCCOMBAT")
	int pdcCombat=3;
	
	@Column(name="HFATIGUE")
	int Hfatigue=0; // nb de demi-Handicap
	
	@Column(name="CCINFATIGABLE")
	int CCinfatigable=0;
	
	@Column(name="HMOBILITE")
	int Hmobilite=0;
	
	@Column(name="CCINCOERCIBLE")
	int CCincoercible=0;
	
	@Column(name="HSENS")
	int Hsens=0;
	
	@Column(name="CCTOUJOURSPRET")
	int CCtoujoursPret=0;
	
	@Column(name="CCCOMBATPLUSIEURS")
	int CCcombatPlusieurs=0;
	
	public PersonnageDB(String partie) {
		this.partie = partie;
		this.comboList = new ArrayList<>();
		this.comboList.add(new ComboDB("Base", this, false));
		this.comboList.add(new ComboDB("Bonus permanent", this, true));
		this.comboList.add(new ComboDB("Bonus 1 round", this, true));
	}
	
	public int totalDemiNiveauxBlessures() {
		return this.blessureList.stream().mapToInt(bl -> bl.getDemiNiveau()).sum();
	}

	public int totalPtsDeChoc() {
		return this.blessureList.stream().mapToInt(bl -> bl.getPtDeChoc()).sum();
	}
	
	public float getHfatigue2() {
		return (float) ((float) this.Hfatigue/2.0);
	}
	public void setHfatigue2(float hd) {
		this.Hfatigue = (int) Math.round(hd*2);
	}
	public float getHmobilite2() {
		return (float) ((float) this.Hmobilite/2.0);
	}
	public void setHmobilite2(float hd) {
		this.Hmobilite = (int) Math.round(hd*2);
	}
	public float getHsens2() {
		return (float) ((float) this.Hsens/2.0);
	}
	public void setHsens2(float hd) {
		this.Hsens = (int) Math.round(hd*2);
	}
	
	public float getCCinfatigable2() {
		return (float) ((float) this.CCinfatigable/2.0);
	}
	public void setCCinfatigable2(float hd) {
		this.CCinfatigable = (int) Math.round(hd*2);
	}	
	public float getCCincoercible2() {
		return (float) ((float) this.CCincoercible/2.0);
	}
	public void setCCincoercible2(float hd) {
		this.CCincoercible = (int) Math.round(hd*2);
	}	
	public float getCCtoujoursPret2() {
		return (float) ((float) this.CCtoujoursPret/2.0);
	}
	public void setCCtoujoursPret2(float hd) {
		this.CCtoujoursPret = (int) Math.round(hd*2);
	}
}
