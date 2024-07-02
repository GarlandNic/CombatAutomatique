package com.azurhyan.CombatAutomatique.model;

import java.time.LocalDateTime;
import java.util.Random;

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
	
	@Column(name="REFATTAQUE")
	int refAttaque=0;

	@Column(name="PARTIE")
	String partie;
	
//	 FOREIGN KEY (ACTEURID) REFERENCES personnages (ID) ON DELETE CASCADE ON UPDATE CASCADE
	@Column(name="ACTEURID")
	int persoId;
	
	@Column(name="ACTEURNOM")
	String acteurNom;
	
	@Column(name="ACTEURDE")
	int acteurDe=0;
	
	@Column(name="CIBLEID")
	int cibleId;
	
	@Column(name="CIBLENOM")
	String cibleNom;
	
	@Column(name="CIBLEDE")
	int cibleDe;

//	@ManyToOne
//	@JoinColumn(name="ACTEURID")
//	PersonnageDB acteur;
	
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

	public ActionDB(String partie, String descr) {
		this.setPartie(partie);
		this.setActionTime(LocalDateTime.now());
		this.setDescription(descr);
	}

	public ActionDB() {
	}
	
	public static String getCouleur(String acteurNom) {
		if(null == acteurNom || acteurNom.isBlank()) {
			return "#ffff00";
		} else {
//			int max = 46656; // 6^6
			Long res = 12345L;
			for(char a : acteurNom.toCharArray()) {
				res *= (int) a;
				res += 1;
//				res = res % max;
			}
			Random alea = new Random();
			alea.setSeed(res);
			char[] result = {'#', 'a', 'a', 'a', 'a', 'a', 'a'};
			for(int i = 1 ; i<result.length ; i++) {
				int num = alea.nextInt(6);
				switch(num) {
				case 0 : result[i] = 'a'; break;
				case 1 : result[i] = 'b'; break;
				case 2 : result[i] = 'c'; break;
				case 3 : result[i] = 'd'; break;
				case 4 : result[i] = 'e'; break;
				case 5 : result[i] = 'f'; break;
				}
			}
			return String.valueOf(result);
		}
	}
	
	public String getCouleur() {
		return getCouleur(this.acteurNom);
	}
}
