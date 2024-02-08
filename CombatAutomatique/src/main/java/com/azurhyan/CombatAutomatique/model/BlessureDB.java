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
@Table(name = "blessures")
public class BlessureDB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="BLESSID")
	int blessureId;
	
//	add FOREIGN KEY (REFACTION) REFERENCES actions (ACTIOID) ON DELETE SET NULL ON UPDATE SET NULL;
	@Column(name="REFACTION")
	int refAction=0;
	
//	@Column(name="PERSONNAGE")
//	int persoId;
	
	@ManyToOne
	@JoinColumn(name="PERSONNAGE")
	PersonnageDB perso;
	
	@Column(name="DEMINIVEAU")
	int demiNiveau;
	
	@Column(name="PTDECHOC")
	int ptDeChoc;
	
	@Column(name="PARTIETOUCHEE")
	String partieTouchee;
	
//	create table blessures(
//			 FOREIGN KEY (PERSONNAGE) REFERENCES personnages (ID) ON DELETE CASCADE ON UPDATE CASCADE
//			);
	
	public String getGravite() {
		int CON = this.perso.getCON();
		return this.getGravite(CON);
	}
	public String getGravite(int CON) {
		if(this.partieTouchee.equals("Bouclier")) return "";
		if(this.partieTouchee.equals("Armure")) return "";
		int demiHandicap = Math.max(0, Math.round(this.getHandicap(CON)*2));
		switch(demiHandicap) {
			case 0: return "égratignure";
			case 1: return "mineure (0,5 H-)";
			case 2: return "légère (1 H-)";
			case 4: return "moyenne (2 H-)";
			case 6: return "grave (3 H-)";
			case 9: return "mortelle (4,5 H-)";
			case 20: return "mort";
		}
		return "";
	}
	
	public float getHandicap() {
		int CON = this.perso.getCON();
		return this.getHandicap(CON);
	}
	public float getHandicap(int CON) {
		if(this.partieTouchee.equals("Bouclier")) return 0;
		if(this.partieTouchee.equals("Armure")) return 0;
		if(this.getNiveau()+0.5 > 0.7*CON) {
			return 10;
		}
		if(this.getNiveau()+0.5 > 0.55*CON) {
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
		return ((float) this.demiNiveau)/2;
	}
	
	public void setNiveau(float niv) {
		this.demiNiveau = Math.max(0, Math.round(niv*2));
//		this.perso.setHfatigue(this.perso.getHfatigue() + (int) (this.getHandicap()*2));
	}
	
	public void setDemiNiveau(int demniv) {
		this.demiNiveau = demniv;
//		this.perso.setHfatigue(this.perso.getHfatigue() + (int) (this.getHandicap()*2));
	}
	
	public BlessureDB(PersonnageDB perso, float niv, int pdc) {
		this.perso = perso;
		this.setNiveau(niv);
		this.setPtDeChoc(pdc);
		this.partieTouchee = "";
	}
	
	public BlessureDB() {
	}
	
	public BlessureDB copy(PersonnageDB newPerso) {
		BlessureDB bl = new BlessureDB();
		bl.setDemiNiveau(this.getDemiNiveau());
		bl.setPartieTouchee(this.getPartieTouchee());
		bl.setPtDeChoc(this.getPtDeChoc());
		bl.setPerso(newPerso);
		return bl;
	}
	
	
	public String blessureToString() {
		String result = partieTouchee;
		result += " (";
		if(demiNiveau > 0) result += getNiveau()+" nv";
		if(demiNiveau > 0 && ptDeChoc > 0) result += " & ";
		if(ptDeChoc > 0) result += ptDeChoc+"#";
		if( !(partieTouchee.equals("Bouclier") || partieTouchee.equals("Armure")) ) 
			result += " ("+getGravite()+")"; 
		result += ")";
		return result;
	}

}
