package com.azurhyan.CombatAutomatique.model;

import java.util.ArrayList;
import java.util.List;

import com.azurhyan.CombatAutomatique.model.ComboDB.Bouclier;
import com.azurhyan.CombatAutomatique.model.HandicapDB.TypeHand;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
	boolean visible;
	
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
	
	@OneToMany(cascade = CascadeType.ALL, 
			   orphanRemoval = true, 
			   fetch = FetchType.EAGER, 
			   mappedBy = "perso")
	List<HandicapDB> handicapList = new ArrayList<>();
	
	@OneToOne(mappedBy = "perso", 
			  cascade = CascadeType.ALL, 
			  orphanRemoval = true, 
			  fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
	EtatDB etat;
	
//	@OneToMany(mappedBy = "persoId")
//	List<ActionDB> actionList = new ArrayList<>();
	
	@Column(name="CON")
	int CON=10;
	
	@Column(name="PDCCOMBAT")
	int pdcCombat=3;
	
	@Column(name="CCINFATIGABLE")
	int CCinfatigable=0;
	
	@Column(name="CCINCOERCIBLE")
	int CCincoercible=0;
	
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
		return this.blessureList.stream().mapToInt(bl -> 
			( !(bl.partieTouchee.equals("Bouclier") || bl.partieTouchee.equals("Armure")) ? 
					bl.getDemiNiveau() : 0) ).sum();
	}

	public int totalPtsDeChoc() {
		return this.blessureList.stream().mapToInt(bl -> 
			( !(bl.partieTouchee.equals("Bouclier") || bl.partieTouchee.equals("Armure")) ? 
					bl.getPtDeChoc() : 0) ).sum();
	}
	
	public int totalHandicaps() {
		int Hfat = this.getHfatigue2() - this.CCinfatigable;
		Hfat = (Hfat > 0 ? Hfat : 0);
		int Hmob = this.getHmobilite2() - this.CCincoercible;
		Hmob = (Hmob > 0 ? Hmob : 0);
		int Hsen = this.getHsens2() - this.CCtoujoursPret;
		Hsen = (Hsen > 0 ? Hsen : 0);
		return (Hfat+Hmob+Hsen)/2;
	}
	
	public boolean hasBouclier() {
		boolean res = false;
		for(ComboDB comb : this.comboList) {
			if(comb.isActif() && comb.getNom().contains("Base"))
				res = (comb.getBouclier() != Bouclier.Pas_de_bouclier); 
		}
		return res;
	}
	
	public int getHfatigue2() {
		return this.handicapList.stream().mapToInt(h -> (h.getTypeHand() == TypeHand.FATIGUE ? h.getDemiNombre() : 0) ).sum();
	}

	public int getHmobilite2() {
		return this.handicapList.stream().mapToInt(h -> (h.getTypeHand() == TypeHand.MOBILITE ? h.getDemiNombre() : 0) ).sum();
	}

	public int getHsens2() {
		return this.handicapList.stream().mapToInt(h -> (h.getTypeHand() == TypeHand.SENS ? h.getDemiNombre() : 0) ).sum();
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
	
	public float getMinPourMineure() {
		double res = 0.2*this.CON - 1 + 0.01;
		res = Math.ceil(res)/2;
		return (float) res;
	}
	
	public PersonnageDB copy() {
		PersonnageDB newPerso = new PersonnageDB();
	    newPerso.setNom(this.getNom());
	    newPerso.setJoueur(this.getJoueur());
	    newPerso.setPartie(this.getPartie());
	    newPerso.setVisible(this.isVisible());
	    newPerso.setCON(this.getCON());
	    newPerso.setPdcCombat(this.getPdcCombat());
	    newPerso.setCCinfatigable(this.getCCinfatigable());
	    newPerso.setCCincoercible(this.getCCincoercible());
	    newPerso.setCCtoujoursPret(this.getCCtoujoursPret());
	    newPerso.setCCcombatPlusieurs(this.getCCcombatPlusieurs());
		
		List<BlessureDB> blList = newPerso.getBlessureList();
		blList.clear();
		this.getBlessureList().forEach(bl -> blList.add(bl.copy(newPerso)));
		
		List<HandicapDB> hList = newPerso.getHandicapList();
		hList.clear();
		this.getHandicapList().forEach(h -> hList.add(h.copy(newPerso)));
		
		List<ComboDB> combList = newPerso.getComboList();
		combList.clear();
		this.getComboList().forEach(comb -> combList.add(comb.copy(newPerso)));
		
		return newPerso;
	}

	public void addHandicap(int refAction, float nombreH, TypeHand typ, String nom) {
//		for(HandicapDB h : this.getHandicapList()) {
//			if(h.getTypeHand() == typ && h.getNomHand().equals(nom)) {
//				nombreH -= h.getNombre();
//			}
//		}
		HandicapDB newHand = new HandicapDB(this, nombreH, typ, nom);
		newHand.setRefAction(refAction);
		this.getHandicapList().add(newHand);
	}
	public void replaceHandicap(int refAction, float nombreH, TypeHand typ, String nom) {
		for(HandicapDB h : this.getHandicapList()) {
			if(h.getTypeHand() == typ && h.getNomHand().equals(nom)) {
				nombreH -= h.getNombre();
			}
		}
		HandicapDB newHand = new HandicapDB(this, nombreH, typ, nom);
		newHand.setRefAction(refAction);
		this.getHandicapList().add(newHand);
	}

}
