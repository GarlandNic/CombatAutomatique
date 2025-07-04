package com.azurhyan.CombatAutomatique.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ComboMltDto {
	
	List<CibleDto> cibleList = new ArrayList<>();
	
	List<CibleDto> autreList = new ArrayList<>();

	ComboDto combo = new ComboDto("nom", true);
	
	String partie="";
	
	public ComboMltDto() {	
	}

}
