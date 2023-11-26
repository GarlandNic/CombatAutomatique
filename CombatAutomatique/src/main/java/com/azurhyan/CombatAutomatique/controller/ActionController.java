package com.azurhyan.CombatAutomatique.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.azurhyan.CombatAutomatique.dto.AttaqueDto;
import com.azurhyan.CombatAutomatique.dto.CibleDto;
import com.azurhyan.CombatAutomatique.dto.ComboDto;
import com.azurhyan.CombatAutomatique.dto.PersoCompletDto;
import com.azurhyan.CombatAutomatique.service.ActionService;
import com.azurhyan.CombatAutomatique.service.PersonnageService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ActionController {
	
	@Autowired
	PersonnageService persoServ;
	
	@Autowired
	ActionService actionServ;
	
	@GetMapping("/azurhyan/{game}/{persoId}/attaque")
	public String formAttaque(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId) {
		AttaqueDto attaque = new AttaqueDto();
		PersoCompletDto perso = persoServ.findPersoCompletDto(persoId);
		attaque.setAttaquantId(perso.getPersoId());
		attaque.setAttaquantNom(perso.getNom());
		attaque.setAttaquantCombo(perso.getComboTotal());
		attaque.setAttaquantPdc(perso.getPdcCombat());
		attaque.setPartie(partie);
		persoServ.listForPartie(partie).forEach(pp -> {
			attaque.getPasCibleList().add(new CibleDto(pp.getId(), pp.getNom()));
		});
		return filledPage_Attaque(model, partie, attaque);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}/attaque", params={"removeTarget"})
	public String removeTarget(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("attaque") AttaqueDto attaque, final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("removeTarget"));
	    attaque.getPasCibleList().add(attaque.getCibleList().get(rowId.intValue()));
	    attaque.getCibleList().remove(rowId.intValue());
	    return filledPage_Attaque(model, partie, attaque);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}/attaque", params={"addTarget"})
	public String addTarget(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("attaque") AttaqueDto attaque, final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("addTarget"));
	    attaque.getCibleList().add(attaque.getPasCibleList().get(rowId.intValue()));
	    attaque.getPasCibleList().remove(rowId.intValue());
	    return filledPage_Attaque(model, partie, attaque);
	}
	
	@PostMapping(value="/azurhyan/{game}/{persoId}/attaque", params={"actu"})
	public String actu(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("attaque") AttaqueDto attaque, final HttpServletRequest req) {
	    return filledPage_Attaque(model, partie, attaque);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}/attaque")
	public String attaqueLancee(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("attaque") AttaqueDto attaque) {
		actionServ.lancerAttaque(attaque);		
	    return "redirect:/azurhyan/"+partie;
	}
	
	
	
	private String filledPage_Attaque(Model model, String partie, AttaqueDto attaque) {
		model.addAttribute("partie", partie);
		model.addAttribute("attaque", attaque);
		return "attaque";
	}

}
