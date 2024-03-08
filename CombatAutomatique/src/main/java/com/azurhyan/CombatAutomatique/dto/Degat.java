package com.azurhyan.CombatAutomatique.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Degat {
	
	List<BlessureDto> blessList = new ArrayList<>();

	List<HandicapDto> handList = new ArrayList<>();
	
	int margeBlesser = 0;
	
	int margeToucher = 0;
	
	boolean isPare = false;

}
