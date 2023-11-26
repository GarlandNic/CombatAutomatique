package com.azurhyan.CombatAutomatique.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AttaqueDto {
	
	int attaquantId=0;
	
	String attaquantNom="";
	
	int attaquantPdc=0;
	
	ComboDto attaquantCombo=null;
	
	List<CibleDto> cibleList = new ArrayList<>();
	
	List<CibleDto> pasCibleList = new ArrayList<>();
	
	boolean lePlusPossible = false;
	
	String partie="";
	
	public AttaqueDto() {	
	}

}
