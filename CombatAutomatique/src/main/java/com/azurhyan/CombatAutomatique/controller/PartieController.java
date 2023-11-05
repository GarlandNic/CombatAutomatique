package com.azurhyan.CombatAutomatique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.azurhyan.CombatAutomatique.model.PersosVisiblesDto;
import com.azurhyan.CombatAutomatique.service.ActionService;
import com.azurhyan.CombatAutomatique.service.PersonnageService;

@Controller
public class PartieController {
	
	@Autowired
	ActionService actionServ;
	
	@Autowired
	PersonnageService persoServ;
	
	@GetMapping("/azurhyan/{game}")
	public String partie(Model model, @PathVariable("game") final String partie) {
		return filledPage_Partie(model, partie);
	}
	
//	@GetMapping("/azurhyan/{game}/annulerAction") // pas encore implémenté TODO
//	public String annulerAction(Model model, @PathVariable("game") final String partie) {
//		actionServ.annulerDerniereAction();
//		return filledPage_Partie(model, partie);
//	}
	
	@GetMapping("/azurhyan/{game}/visibilitePerso")
	public String switchVisibilitePersos(Model model, @PathVariable("game") final String partie) {
		model.addAttribute("partie", partie);
		// ajouter au model la liste des perso // perso.id, perso.nom, perso.visibilite
		PersosVisiblesDto persosVisibles = persoServ.withVisibilite(partie);
		// persosVisibles.persoList
		model.addAttribute("persosVisibles", persosVisibles);
		return "visibilite";
	}
	
	@PostMapping("/azurhyan/{game}/visibilitePerso")
	public String changeVisibilitePersos(Model model, @PathVariable("game") final String partie, @ModelAttribute("persosVisibles") PersosVisiblesDto persosVisibles) {
		persoServ.setAllVisibilite(persosVisibles);
		return filledPage_Partie(model, partie);
	}
	
	
	private String filledPage_Partie(Model model, String partie) {
		model.addAttribute("partie", partie);
		// liste des perso visibles avec init/nom/id à mettre 
		model.addAttribute("persos", persoServ.listForPartie(partie));
		// liste des actions à afficher / action.description
		model.addAttribute("actions", actionServ.listForPartie(partie));
		return "partie";
	}
	
}
