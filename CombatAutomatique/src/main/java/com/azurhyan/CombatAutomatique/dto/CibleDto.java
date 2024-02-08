package com.azurhyan.CombatAutomatique.dto;

import lombok.Data;

@Data
public class CibleDto {
	
	int cibleId=0;
	
	String cibleNom="";
	
	int de = 0;
	
	public CibleDto() {
	}
	
	public CibleDto(int i, String n) {
		this.cibleId = i;
		this.cibleNom = n;
	}

	public CibleDto(int i, String n, int d) {
		this.cibleId = i;
		this.cibleNom = n;
		this.de = d;
	}

}
