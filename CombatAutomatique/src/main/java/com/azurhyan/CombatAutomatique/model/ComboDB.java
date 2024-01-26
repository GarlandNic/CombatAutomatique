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
@Table(name = "combos")
public class ComboDB {
	
	public static enum Dgts {
		NOR,
		CTD,
		PRF,
		GLB,
		BOUSCUL,
		CAPTURE
	}

	public ComboDB() {
	}
	
	public ComboDB(String nom, PersonnageDB perso, boolean mod) {
		this.nom = nom;
		this.perso = perso;
		if(mod) {
			this.init = 0;
			this.toucher = 0;
			this.prdEnnemie = 0;
			this.force = 0;
			this.IBatt = 0;
			this.defense = 0;
			this.esquive = 0;
			this.parade = 0;
			this.endBouclier = 0;
			this.endPerso = 0;
			this.IBdef = 0;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="COMBOID")
	int comboId;
	
//	 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (ID) ON DELETE CASCADE ON UPDATE CASCADE
//	@Column(name="PERSONNAGE")
//	int persoId;
	
	@ManyToOne
	@JoinColumn(name="PERSONNAGE")
	PersonnageDB perso;
	
	@Column(name="NOM")
	String nom;
	
	@Column(name="INIT")
	int init=15;

	@Column(name="TYPEDGTS")
	@Enumerated(EnumType.STRING)
	Dgts typeDgts=Dgts.NOR;

	@Column(name="CAC")
	boolean CaC=true;

	@Column(name="TOUCHER")
	int toucher=15;

	@Column(name="PRDENNEMIE")
	int prdEnnemie=0;
	
	@Column(name="FORCEATTAQUE")
	int force=15;

	@Column(name="IBATT")
	int IBatt=15;
	
	@Column(name="DEFENSE")
	int defense=15;
	
	@Column(name="ESQUIVE")
	int esquive=15;
	
	@Column(name="PARADE")
	int parade=0;
	
	@Column(name="ENDBOUCLIER")
	int endBouclier=15;
	
	@Column(name="ENDPERSO")
	int endPerso=15;
	
	@Column(name="IBDEF")
	int IBdef=15;
	
	@Column(name="ACTIF")
	boolean actif=true;
	
	public void addHandicap(int demiH) {
		int h = demiH/2;
		this.init = this.init - h;
		this.toucher = this.toucher - h;
		this.force = this.force - h;
		this.IBatt = this.IBatt - h;
		this.defense = this.defense - h;
		this.esquive = this.esquive - h;
		this.endBouclier = this.endBouclier - h;
		this.endPerso = this.endPerso - h;
		this.IBdef = this.IBdef - h;
	}

	public ComboDB copy(PersonnageDB newPerso) {
		ComboDB combo = new ComboDB();
		combo.setCaC(CaC);
		combo.setDefense(defense);
		combo.setEndBouclier(endBouclier);
		combo.setEndPerso(endPerso);
		combo.setEsquive(esquive);
		combo.setForce(force);
		combo.setIBatt(IBatt);
		combo.setIBdef(IBdef);
		combo.setInit(init);
		combo.setNom(nom);
		combo.setParade(parade);
		combo.setPrdEnnemie(prdEnnemie);
		combo.setToucher(toucher);
		combo.setTypeDgts(typeDgts);
		combo.setActif(actif);
		combo.setPerso(newPerso);
		return combo;
	}

}
