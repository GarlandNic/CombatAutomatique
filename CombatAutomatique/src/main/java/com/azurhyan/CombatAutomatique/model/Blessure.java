package com.azurhyan.CombatAutomatique.model;

import java.util.List;

import lombok.Data;

@Data
public class Blessure {
	
	int demiNiveaux;
	
	int pointDeChoc;
	
	public String gravite(int CON) {
		int demiHandicap = Math.max(0, Math.round(this.handicap(CON)*2));
		switch(demiHandicap) {
			case 0: return "égratignure";
			case 1: return "mineure";
			case 2: return "légère";
			case 4: return "moyenne";
			case 6: return "grave";
			case 9: return "mortelle";
			case 20: return "mort";
		}
		return "";
	}
	
	public float handicap(int CON) {
		if(this.getNiveau()+0.5 > 0.7*CON) {
			return 10;
		}
		if(this.getNiveau()+0.5 > 0.5*CON) {
			return (float) 4.5;
		}
		if(this.getNiveau()+0.5 > 0.4*CON) {
			return 3;
		}
		if(this.getNiveau()+0.5 > 0.3*CON) {
			return 2;
		}
		if(this.getNiveau()+0.5 > 0.2*CON) {
			return 1;
		}
		if(this.getNiveau()+0.5 > 0.1*CON) {
			return (float) 0.5;
		}
		return 0;
	}
	
	public float getNiveau() {
		return ((float) this.demiNiveaux)/2;
	}
	
	public void setNiveau(float niv) {
		this.demiNiveaux = Math.max(0, Math.round(niv*2));
	}
	
	Blessure(float niv, int pdc) {
		this.setNiveau(niv);
		this.setPointDeChoc(pdc);
	}

}
