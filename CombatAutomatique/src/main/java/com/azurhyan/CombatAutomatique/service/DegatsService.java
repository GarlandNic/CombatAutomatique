package com.azurhyan.CombatAutomatique.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.azurhyan.CombatAutomatique.dto.BlessureDto;
import com.azurhyan.CombatAutomatique.dto.Degat;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.model.ComboDB.Dgts;
import com.azurhyan.CombatAutomatique.model.ComboDB.Element;

@Service
public class DegatsService {

	public int calculDegreChoc(int margeBless, Dgts dgts, Element element, boolean isObjet) {
		int degreBrut = calculDegre(margeBless);
		if(dgts.equals(Dgts.PRF) && isObjet) degreBrut -= 2;
		int modifElement = 0;
		switch (element) {
			case FEU: 
				modifElement=0; break;
			case ACIDE:
				modifElement=(isObjet ? +1 : 0); break;
			case FROID:
				modifElement=(isObjet ? +1 : 0); break;
			case ELECTRICITE:
				modifElement=(isObjet ? -2 : 0); break;
			case NECROTIQUE:
				modifElement=(isObjet ? +1 : 0); break;
			case BLANCHE:
				modifElement = +1; break;
			case NORMAL:
			default:
				modifElement=0; break;
		}
		degreBrut += modifElement;
		return (degreBrut < 0 ? 0 : degreBrut);
	}
	public int calculDegreBl(int margeBless, Dgts dgts, Element element, boolean isObjet) {
		int degreBrut = calculDegre(margeBless);
		if(dgts.equals(Dgts.PRF) && isObjet) degreBrut -= 2;
		int modifElement = 0;
		switch (element) {
			case FEU: 
				modifElement=0; break;
			case ACIDE:
				modifElement=(isObjet ? +1 : 0); break;
			case FROID:
				modifElement=(isObjet ? +1 : 0); break;
			case ELECTRICITE:
				modifElement=(isObjet ? -2 : 0); break;
			case NECROTIQUE:
				modifElement=(isObjet ? +1 : 0); break;
			case BLANCHE:
				modifElement = -1; break;
			case NORMAL:
			default:
				modifElement=0; break;
		}
		degreBrut += modifElement;
		return (degreBrut < 0 ? 0 : degreBrut);
	}
	private int calculDegre(int margeBless) {
		if(margeBless < -9) return -10;
		switch(margeBless) {
		case -9 : case -8 : case -7 : case -6 :
			return -2;
		case -5 : case -4 : case -3 :
			return -1;
		case -2 : case -1 :
			return 0;
		case 0 : case 1 :
			return 1;
		case 2 : case 3 :
			return 2;
		default :
			return 2+(margeBless-1)/3;
		}
	}

	public int calculPtChoc(int degreDgts, Dgts dgts) {
		if(degreDgts <= 0) return 0;
		switch(dgts) {
		case PRF : 
			switch(degreDgts) {
			case 1 : return 0;
			case 2 : return 1;
			case 3 : return 2;
			case 4 : return 3;
			case 5 : return 4;
			case 6 : return 7;
			case 7 : return 10;
			default : return (degreDgts-5)*5;
			}
		case CTD :
			switch(degreDgts) {
			case 1 : return 2;
			case 2 : return 4;
			case 3 : return 8;
			case 4 : return 12;
			case 5 : return 18;
			case 6 : return 24;
			case 7 : return 40;
			default : return (degreDgts-5)*20;
			}
		case NOR :
		default :
			switch(degreDgts) {
			case 1 : return 1;
			case 2 : return 2;
			case 3 : return 4;
			case 4 : return 6;
			case 5 : return 9;
			case 6 : return 14;
			case 7 : return 20;
			default : return (degreDgts-5)*10;
			}
		}
	}

	public float calculNvBl(int degreDgts, Dgts dgts) {
		if(degreDgts <= 0) return 0;
		switch(dgts) {
		case CTD :
			switch(degreDgts) {
			case 1 : return 0;
			case 2 : return (float) 0.5;
			case 3 : return 1;
			case 4 : return 2;
			case 5 : return 3;
			case 6 : return (float) 4.5;
			case 7 : return 7;
			default : return (degreDgts-6)*5;
			}
		case PRF : 
		case NOR :
		default :
			switch(degreDgts) {
			case 1 : return (float) 0.5;
			case 2 : return 1;
			case 3 : return 2;
			case 4 : return 3;
			case 5 : return (float) 4.5;
			case 6 : return 7;
			case 7 : return 10;
			default : return (degreDgts-5)*5;
			}
		}
	}

	public int modifPtChocGlb(int ptChoc) {
		// TODO Auto-generated method stub
		return ptChoc;
	}

	public float modifNvBlGlb(float nvBl) {
		// TODO Auto-generated method stub
		return nvBl;
	}

	public int modifPtChoc(Element element, int ptChoc, int degreDgts) {
		switch (element) {
		case NORMAL:
			return ptChoc;
		case FEU:
		case ACIDE:
			return ptChoc + degreDgts;
		case FROID:
			return ptChoc;
		case ELECTRICITE:
			return ptChoc;
		case NECROTIQUE:
			return ptChoc + degreDgts;
		case BLANCHE:
    	default:
    		return ptChoc;
		}
	}

	public float modifNvBl(Element element, float nvBl) {
		return nvBl;
	}
	
	public Dgts checkTypeElement(Dgts dgts, Element element) {
		switch (element) {
		case FEU:
		case ACIDE:
			return Dgts.NOR;
		case FROID:
			return Dgts.CTD;
		case ELECTRICITE:
			return Dgts.NOR;
		case NECROTIQUE:
			return Dgts.NOR;
		case BLANCHE:
			return Dgts.CTD;
		case NORMAL:
    	default:
			return dgts;
		}
	}

	
	public Degat diviseBlGlobaux(Degat degats, PersonnageDB defenseur) {
		if(degats.getBlessList().isEmpty()) return degats;
		int nbMin = 0;
		int nbLeg = 0;
		for(BlessureDto bl : degats.getBlessList()) {
			if(!bl.getPartieTouchee().equals("Bouclier") && !bl.getPartieTouchee().equals("Armure")) {
				nbMin += minToRep(bl.getNiveau());
				nbLeg += LegToRep(bl.getNiveau());
				bl.setNiveau(0);
				bl.setPtDeChoc(bl.getPtDeChoc() + PdcToRep(bl.getNiveau()));
				bl.setPartieTouchee("points de choc");
			}
		}
		
		int[] repartition = new int[] {0, 0, 0, 0, 0, 0};
		String[] parties = new String[] {"Tête", "Tronc", "Bras droit", "Bras gauche", "Jambe droite", "Jambe gauche"};
		
		repartition = repartitionScores(repartition, nbMin, nbLeg);
		
		for(int i=0; i<repartition.length; i++) {
			if(repartition[i] > 0) {
				BlessureDto bl = new BlessureDto(bl_scToNiv(repartition[i]), 0);
				bl.setPartieTouchee(parties[i]);
				degats.getBlessList().add(bl);
			}
		}
		return degats;
	}

	private int PdcToRep(float niveau) {
		if(niveau <= 0) return 0;
		if(niveau <= 0.5) return 0; // mineure
		if(niveau <= 1) return 2; // légère
		if(niveau <= 2) return 4; // moyenne
		if(niveau <= 3) return 6; // grave
		if(niveau <= 4.5) return 8; // mortelle
		if(niveau <= 7) return 10; // presque mort
		if(niveau <= 10) return 12; // mort 10
		return (int) (niveau/5*2+8);
	}
	private int LegToRep(float niveau) {
		if(niveau <= 0) return 0;
		if(niveau <= 0.5) return 0; // mineure
		if(niveau <= 1) return 0; // légère
		if(niveau <= 2) return 2; // moyenne
		if(niveau <= 3) return 3; // grave
		if(niveau <= 4.5) return 4; // mortelle
		if(niveau <= 7) return 7; // presque mort
		if(niveau <= 10) return 10; // mort 10
		return (int) (niveau);
	}
	private int minToRep(float niveau) {
		if(niveau <= 0) return 0;
		if(niveau <= 0.5) return 1; // mineure
		if(niveau <= 1) return 3; // légère
		if(niveau <= 2) return 2; // moyenne
		if(niveau <= 3) return 3; // grave
		if(niveau <= 4.5) return 4; // mortelle
		if(niveau <= 7) return 5; // presque mort
		if(niveau <= 10) return 6; // mort 10
		return (int) (niveau/5+4);
	}
	
	private float bl_scToNiv(int sc) {
		if(sc <= 0) return 0;
		if(sc <= 1) return (float) 0.5; // mineure
		if(sc <= 2) return 1; // légère
		if(sc <= 5) return 2; // moyenne
		if(sc <= 9) return 3; // grave
		if(sc <= 14) return (float) 4.5; // mortelle
		if(sc <= 20) return 7; // presque mort
		if(sc <= 29) return 10; // mort 10
		return (int) (5*(sc/10));
	}

	private int bl_nivToSc(float niveau) {
		if(niveau <= 0) return 0;
		if(niveau <= 0.5) return 1; // mineure
		if(niveau <= 1) return 2; // légère
		if(niveau <= 2) return 5; // moyenne
		if(niveau <= 3) return 8; // grave
		if(niveau <= 4.5) return 12; // mortelle
		if(niveau <= 7) return 15; // presque mort
		if(niveau <= 10) return 21; // mort 10
		return (int) (niveau*2);
	}
	
	private int[] repartitionScores(int[] repartition, int nbMin, int nbLeg) {
		Random random = new Random();
		int N = repartition.length;
		
		for(int a=0; a<nbMin; a++) {
			repartition[random.nextInt(N)] += 1;
		}
		for(int b=0; b<nbLeg; b++) {
			repartition[random.nextInt(N)] += 2;
		}

		return repartition;
	}
	
	public int modifDegreBousc(Dgts typeDgts) {
		switch(typeDgts) {
			case PRF: return -1;
			case CTD: return +1;
			case NOR: 
			default:  return 0;
		}
	}

}
