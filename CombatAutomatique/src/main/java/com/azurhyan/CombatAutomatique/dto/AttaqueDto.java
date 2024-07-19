package com.azurhyan.CombatAutomatique.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AttaqueDto {
	
	List<CibleDto> attaquantList = new ArrayList<>();
	
	List<CibleDto> cibleList = new ArrayList<>();
	
	List<CibleDto> autreList = new ArrayList<>();

	boolean lePlusPossible = false;
	boolean bousculade = false;
	boolean coupDansLeBouclier = false;
	
	String partie="";
	
	public AttaqueDto() {	
	}

}
