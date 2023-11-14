package com.azurhyan.CombatAutomatique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azurhyan.CombatAutomatique.indextesting.Row;
import com.azurhyan.CombatAutomatique.indextesting.SeedStarter;
import com.azurhyan.CombatAutomatique.model.BlessureDB;
import com.azurhyan.CombatAutomatique.model.ComboDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.model.PersosVisiblesDto;
import com.azurhyan.CombatAutomatique.service.ActionService;
import com.azurhyan.CombatAutomatique.service.PersonnageService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PersonnageController {
	
	@Autowired
	PersonnageService persoServ;
	
	@Autowired
	ActionService actionServ;
	
	@GetMapping("/azurhyan/{game}/nouveauPerso")
	public String formNouvPerso(Model model, @PathVariable("game") final String partie) {
		return filledPage_Personnage(model, partie, null);
	}

	@GetMapping("/azurhyan/{game}/{persoId}")
	public String fichePersonnage(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId) {
		return filledPage_Personnage(model, partie, persoServ.findById(persoId));
	}
	
	@PostMapping("/azurhyan/{game}/{persoId}")
	public String savePersonnage(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersonnageDB perso) {
		PersonnageDB pp = persoServ.save(perso);
		return filledPage_Personnage(model, partie, pp);
	}
	
	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"actu"})
	public String actu(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersonnageDB perso) {
	    return filledPage_Personnage(model, partie, perso);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"addRow"})
	public String addRow(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersonnageDB perso) {
	    perso.getBlessureList().add(new BlessureDB(perso, 0, 0));
	    return filledPage_Personnage(model, partie, perso);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"removeRow"})
	public String removeRow(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersonnageDB perso, final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
	    perso.getBlessureList().remove(rowId.intValue());
	    return filledPage_Personnage(model, partie, perso);
	}
//	@RequestMapping(value="/azurhyan/{game}/{persoId}", params={"addRow"})
//	public String addRow(Model model) {
//		PersonnageDB perso = (PersonnageDB) model.getAttribute("perso");
//		perso.getBlessureList().add(new BlessureDB(perso, 0, 0));
//	    return "personnage";
//	}
//	@RequestMapping(value="/azurhyan/{game}/{persoId}", params={"removeRow"})
//	public String removeRow(Model model, final HttpServletRequest req) {
//		PersonnageDB perso = (PersonnageDB) model.getAttribute("perso");
//	    final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
//	    perso.getBlessureList().remove(rowId.intValue());
//	    return "personnage";
//	}

	
	private String filledPage_Personnage(Model model, String partie, PersonnageDB perso) {
		model.addAttribute("partie", partie);
		if(perso==null) {
			perso = new PersonnageDB(partie);
		}
		model.addAttribute("perso", perso);
		model.addAttribute("combohandicap", persoServ.comboHandicap(perso));
		model.addAttribute("combototal", persoServ.comboTotal(perso));
		return "personnage";
	}
	
}
