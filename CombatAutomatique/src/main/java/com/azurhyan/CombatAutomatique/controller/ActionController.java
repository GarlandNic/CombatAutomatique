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
//		PersoCompletDto perso = persoServ.findPersoCompletDto(persoId);
//		attaque.setAttaquantId(perso.getPersoId());
//		attaque.setAttaquantNom(perso.getNom());
//		attaque.setAttaquantCombo(perso.getComboTotal());
//		attaque.setAttaquantPdc(perso.getPdcCombat());
		attaque.setPartie(partie);
		persoServ.listForPartie(partie).forEach(pp -> {
			if(pp.getId() == persoId) attaque.getAttaquantList().add(new CibleDto(pp.getId(), pp.getNom()));
			else attaque.getAutreList().add(new CibleDto(pp.getId(), pp.getNom()));
		});
		return filledPage_Attaque(model, partie, attaque, persoId);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}/attaque", params={"removeTarget"})
	public String removeTarget(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("attaque") AttaqueDto attaque, final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("removeTarget"));
	    attaque.getAutreList().add(attaque.getCibleList().get(rowId.intValue()));
	    attaque.getCibleList().remove(rowId.intValue());
	    return filledPage_Attaque(model, partie, attaque, persoId);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}/attaque", params={"addTarget"})
	public String addTarget(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("attaque") AttaqueDto attaque, final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("addTarget"));
	    CibleDto newTarget = attaque.getAutreList().get(rowId.intValue());
	    newTarget.setDe(persoServ.findByPersonnage(newTarget.getCibleId()).getDeDef());
	    attaque.getCibleList().add(newTarget);
	    attaque.getAutreList().remove(rowId.intValue());
	    return filledPage_Attaque(model, partie, attaque, persoId);
	}
	
	@PostMapping(value="/azurhyan/{game}/{persoId}/attaque", params={"removeAttacker"})
	public String removeAttacker(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("attaque") AttaqueDto attaque, final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("removeAttacker"));
	    CibleDto newTarget = attaque.getAttaquantList().get(rowId.intValue());
	    newTarget.setDe(persoServ.findByPersonnage(newTarget.getCibleId()).getDeDef());
	    attaque.getCibleList().add(newTarget);
	    attaque.getAttaquantList().remove(rowId.intValue());
	    return filledPage_Attaque(model, partie, attaque, persoId);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}/attaque", params={"addAttacker"})
	public String addAttacker(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("attaque") AttaqueDto attaque, final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("addAttacker"));
	    attaque.getCibleList().get(rowId.intValue()).setDe(0);
	    attaque.getAttaquantList().add(attaque.getCibleList().get(rowId.intValue()));
	    attaque.getCibleList().remove(rowId.intValue());
	    return filledPage_Attaque(model, partie, attaque, persoId);
	}
	
	@PostMapping(value="/azurhyan/{game}/{persoId}/attaque", params={"actu"})
	public String actu(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("attaque") AttaqueDto attaque, final HttpServletRequest req) {
	    return filledPage_Attaque(model, partie, attaque, persoId);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}/attaque")
	public String attaqueLancee(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("attaque") AttaqueDto attaque) {
		actionServ.lancerAttaque(attaque);
	    return "redirect:/azurhyan/"+partie;
	}
	
	@PostMapping(value="/azurhyan/{game}/annulerAttaque", params={"lastAttaque"})
	public String annulerAttaque(Model model, @PathVariable("game") final String partie, final HttpServletRequest req) {
	    final Integer refA = Integer.valueOf(req.getParameter("lastAttaque"));
	    actionServ.annulerAttaque(refA.intValue());
	    return "redirect:/azurhyan/"+partie;
	}
	
	@PostMapping(value="/azurhyan/{game}/modifierAttaque", params={"lastAttaque"})
	public String modifierAttaque(Model model, @PathVariable("game") final String partie, final HttpServletRequest req) {
	    final Integer refA = Integer.valueOf(req.getParameter("lastAttaque"));
	    AttaqueDto attaque = actionServ.fetchAttaque(refA.intValue(), partie);
	    actionServ.annulerAttaque(refA.intValue());
		return filledPage_Attaque(model, partie, attaque, 0);
	}
	
	private String filledPage_Attaque(Model model, String partie, AttaqueDto attaque, int numPerso) {
		model.addAttribute("partie", partie);
		model.addAttribute("attaque", attaque);
		model.addAttribute("numPerso", numPerso);
		return "attaque";
	}

}
