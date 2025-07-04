package com.azurhyan.CombatAutomatique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.azurhyan.CombatAutomatique.dto.AttaqueDto;
import com.azurhyan.CombatAutomatique.dto.CibleDto;
import com.azurhyan.CombatAutomatique.dto.ComboMltDto;
import com.azurhyan.CombatAutomatique.service.ActionService;
import com.azurhyan.CombatAutomatique.service.ComboService;
import com.azurhyan.CombatAutomatique.service.PersonnageService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ComboMultipleController {
	
	@Autowired
	PersonnageService persoServ;
	
	@Autowired
	ComboService comboServ;
	
	@GetMapping("/azurhyan/{game}/comboMultiple")
	public String formComboMlt(Model model, @PathVariable("game") final String partie) {
		ComboMltDto cmbMlt = new ComboMltDto();
//		PersoCompletDto perso = persoServ.findPersoCompletDto(persoId);
//		attaque.setAttaquantId(perso.getPersoId());
//		attaque.setAttaquantNom(perso.getNom());
//		attaque.setAttaquantCombo(perso.getComboTotal());
//		attaque.setAttaquantPdc(perso.getPdcCombat());
		cmbMlt.setPartie(partie);
		persoServ.listForPartie(partie).forEach(pp -> {
			cmbMlt.getAutreList().add(new CibleDto(pp.getId(), pp.getNom()));
		});
		return filledPage_ComboMlt(model, partie, cmbMlt);
	}

	@PostMapping(value="/azurhyan/{game}/comboMultiple", params={"removeTarget"})
	public String removeTarget(Model model, @PathVariable("game") final String partie, @ModelAttribute("cmbMlt") ComboMltDto cmbMlt, 
			final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("removeTarget"));
	    cmbMlt.getAutreList().add(cmbMlt.getCibleList().get(rowId.intValue()));
	    cmbMlt.getCibleList().remove(rowId.intValue());
	    return filledPage_ComboMlt(model, partie, cmbMlt);
	}

	@PostMapping(value="/azurhyan/{game}/comboMultiple", params={"addTarget"})
	public String addTarget(Model model, @PathVariable("game") final String partie, @ModelAttribute("cmbMlt") ComboMltDto cmbMlt, 
			final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("addTarget"));
	    CibleDto newTarget = cmbMlt.getAutreList().get(rowId.intValue());
	    cmbMlt.getCibleList().add(newTarget);
	    cmbMlt.getAutreList().remove(rowId.intValue());
	    return filledPage_ComboMlt(model, partie, cmbMlt);
	}
	
	@PostMapping(value="/azurhyan/{game}/comboMultiple", params={"actu"})
	public String actu(Model model, @PathVariable("game") final String partie, @ModelAttribute("cmbMlt") ComboMltDto cmbMlt) {
	    return filledPage_ComboMlt(model, partie, cmbMlt);
	}

	@PostMapping(value="/azurhyan/{game}/comboMultiple")
	public String affecterCombo(Model model, @PathVariable("game") final String partie, @ModelAttribute("cmbMlt") ComboMltDto cmbMlt) {
		comboServ.affecterCombo(cmbMlt);
	    return "redirect:/azurhyan/"+partie;
	}
	
	
	private String filledPage_ComboMlt(Model model, String partie, ComboMltDto cmbMlt) {
		model.addAttribute("partie", partie);
		model.addAttribute("persos", persoServ.listForPartie(partie));
		model.addAttribute("cmbMlt", cmbMlt);
		return "comboMultiple";
	}

}
