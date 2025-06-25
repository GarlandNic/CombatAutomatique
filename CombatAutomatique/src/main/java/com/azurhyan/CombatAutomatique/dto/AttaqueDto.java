package com.azurhyan.CombatAutomatique.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AttaqueDto {
	
	List<CibleDto> attaquantList = new ArrayList<>();
	
	List<CibleDto> cibleList = new ArrayList<>();
	
	List<CibleDto> autreList = new ArrayList<>();

	boolean tirDeMasse = false; // = tirDeMasse = lePlusPossible
	boolean bousculade = false;
	boolean coupDansLeBouclier = false;
	boolean capture = false;
	
	String partie="";
	
	public AttaqueDto() {	
	}

}
