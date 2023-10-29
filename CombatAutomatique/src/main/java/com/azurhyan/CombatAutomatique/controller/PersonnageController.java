package com.azurhyan.CombatAutomatique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.service.ActionService;
import com.azurhyan.CombatAutomatique.service.PersonnageService;

@Controller
public class PersonnageController {
	
	@Autowired
	PersonnageService persoServ;
	
	@Autowired
	ActionService actionServ;
	
	@GetMapping("/azurhyan/{game}/{persoId}")
	public String partie(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final String persoId) {
		return filledPage_Personnage(model, partie, persoId);
	}
	
	@GetMapping("/azurhyan/{game}/nouveauPerso")
	public String formNouvPerso(Model model, @PathVariable("game") final String partie) {
		return filledPage_Personnage(model, partie, null);
	}


	
	private String filledPage_Personnage(Model model, String partie, String persoId) {
		model.addAttribute("partie", partie);
		// créer un perso vide
		PersonnageDB perso = new PersonnageDB();
		if(!persoId.isEmpty()) {
			// récupérer les infos du perso depuis la BD grace à persoId
		}
//		model.addAttribute(...perso...);
		return "personnage";
	}
	
}
