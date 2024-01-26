package com.azurhyan.CombatAutomatique.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Degat {
	
	List<BlessureDto> blessList = new ArrayList<>();
	
	float bousculadeH = 0;
	
	boolean toucher = false;

}