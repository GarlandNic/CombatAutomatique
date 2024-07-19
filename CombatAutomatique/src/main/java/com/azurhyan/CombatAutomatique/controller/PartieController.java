package com.azurhyan.CombatAutomatique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.azurhyan.CombatAutomatique.dto.PersoCompletDto;
import com.azurhyan.CombatAutomatique.dto.PersoPartieDto;
import com.azurhyan.CombatAutomatique.dto.PersosVisiblesDto;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.service.ActionService;
import com.azurhyan.CombatAutomatique.service.ActionService_v6;
import com.azurhyan.CombatAutomatique.service.PersonnageService;

import jakarta.servlet.http.HttpServletRequest;

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
	
	@GetMapping("/azurhyan/{game}/effacerActions")
	public String effacerLesActions(Model model, @PathVariable("game") final String partie) {
		actionServ.effacerLesActions(partie);
		return filledPage_Partie(model, partie);
	}
	
	@GetMapping("/azurhyan/{game}/visibilitePerso")
	public String visibilitePersos(Model model, @PathVariable("game") final String partie) {
		model.addAttribute("partie", partie);
		PersosVisiblesDto persosVisibles = persoServ.withVisibilite(partie);
		model.addAttribute("persosVisibles", persosVisibles);
		return "visibilite";
	}
	
	@PostMapping("/azurhyan/{game}/visibilitePerso")
	public String changeVisibilitePersos(Model model, @PathVariable("game") final String partie, @ModelAttribute("persosVisibles") PersosVisiblesDto persosVisibles) {
		persoServ.setAllEtat(persosVisibles);
		return filledPage_Partie(model, partie);
	}
	
	@PostMapping(value="/azurhyan/{game}/visibilitePerso", params={"archiver"})
	public String archiverPerso(Model model, @PathVariable("game") final String partie, @ModelAttribute("persosVisibles") PersosVisiblesDto persosVisibles, 
			final HttpServletRequest req) {
	    final Integer id = Integer.valueOf(req.getParameter("archiver"));
	    persoServ.switchArchivage(id);
	    return "redirect:/azurhyan/"+partie+"/visibilitePerso";
	}
	
	@PostMapping(value="/azurhyan/{game}/visibilitePerso", params={"dupliquer"})
	public String duppliquerPerso(Model model, @PathVariable("game") final String partie, @ModelAttribute("persosVisibles") PersosVisiblesDto persosVisibles, 
			final HttpServletRequest req) {
	    final Integer id = Integer.valueOf(req.getParameter("dupliquer"));
	    PersonnageDB perso = persoServ.findById(id);
	    persoServ.saveCopy(perso);
	    return "redirect:/azurhyan/"+partie+"/visibilitePerso";
	}
	
	@PostMapping(value="/azurhyan/{game}/visibilitePerso", params={"supprimer"})
	public String supprimerPerso(Model model, @PathVariable("game") final String partie, @ModelAttribute("persosVisibles") PersosVisiblesDto persosVisibles, 
			final HttpServletRequest req) {
	    final Integer id = Integer.valueOf(req.getParameter("supprimer"));
	    model.addAttribute("confirme", id);
	    return visibilitePersos(model, partie);
	}
	
	@PostMapping(value="/azurhyan/{game}/visibilitePerso", params={"confirmeSuppr"})
	public String confirmeSupprPerso(Model model, @PathVariable("game") final String partie, @ModelAttribute("persosVisibles") PersosVisiblesDto persosVisibles, 
			final HttpServletRequest req) {
	    final Integer id = Integer.valueOf(req.getParameter("confirmeSuppr"));
	    persoServ.deleteById(id);
	    return "redirect:/azurhyan/"+partie+"/visibilitePerso";
	}
	
	@GetMapping("/azurhyan/{game}/defenseReset")
	public String nouveauRound(Model model, @PathVariable("game") final String partie) {
		int newRd = actionServ.nouveauRound(partie);
		persoServ.resetDefense(partie, newRd);
		return "redirect:/azurhyan/"+partie;
	}
	
	@PostMapping(value="/azurhyan/{game}/supprimerHandicaps", params={"refAction"})
	public String modifierAttaque(Model model, @PathVariable("game") final String partie, final HttpServletRequest req) {
	    final Integer refAction = Integer.valueOf(req.getParameter("refAction"));
	    actionServ.supprimerHandicap(refAction);
		return filledPage_Partie(model, partie);
	}
	

	
	
	
	private String filledPage_Partie(Model model, String partie) {
		model.addAttribute("partie", partie);
		model.addAttribute("persos", persoServ.listForPartie(partie));
		model.addAttribute("actions", actionServ.listForPartie(partie));
		model.addAttribute("refactionhandicap", persoServ.listRefActionFromHandi(partie));
		return "partie";
	}
	
}
