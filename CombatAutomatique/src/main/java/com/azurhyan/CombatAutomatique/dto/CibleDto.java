package com.azurhyan.CombatAutomatique.dto;

import lombok.Data;

@Data
public class CibleDto {
	
	int cibleId=0;
	
	String cibleNom="";
	
	public CibleDto() {
	}
	
	public CibleDto(int i, String n) {
		this.cibleId = i;
		this.cibleNom = n;
	}

}
