package com.azurhyan.CombatAutomatique.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.azurhyan.CombatAutomatique.dto.BlessureDto;
import com.azurhyan.CombatAutomatique.dto.ComboDto;
import com.azurhyan.CombatAutomatique.dto.HandicapDto;
import com.azurhyan.CombatAutomatique.dto.PersoCompletDto;
import com.azurhyan.CombatAutomatique.model.HandicapDB.TypeHand;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;
import com.azurhyan.CombatAutomatique.service.ActionService_v6;
import com.azurhyan.CombatAutomatique.service.PersonnageService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PersonnageController {
	
	@Autowired
	PersonnageService persoServ;
	
	@Autowired
	ActionService_v6 actionServ;
	
	@GetMapping("/azurhyan/{game}/nouveauPerso")
	public String formNouvPerso(Model model, @PathVariable("game") final String partie) {
		return filledPage_Personnage(model, partie, null);
	}

	@GetMapping("/azurhyan/{game}/{persoId}")
	public String fichePersonnage(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId) {
		return filledPage_Personnage(model, partie, persoServ.findPersoCompletDto(persoId));
	}
	
	@PostMapping("/azurhyan/{game}/{persoId}")
	public String savePersonnage(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersoCompletDto perso) {
		PersoCompletDto pp = persoServ.saveDto(perso);
		return filledPage_Personnage(model, partie, pp);
	}
	
	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"retour"})
	public String savePersoAndReturn(Model model, @PathVariable("game") final String partie, 
			@ModelAttribute("perso") PersoCompletDto perso) {
		persoServ.saveDto(perso);
		return "redirect:/azurhyan/"+partie;
	}
	
	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"attaque"})
	public String savePersoAndAttaque(Model model, @PathVariable("game") final String partie, 
			@ModelAttribute("perso") PersoCompletDto perso) {
		PersoCompletDto pp = persoServ.saveDto(perso);
		return "redirect:/azurhyan/"+partie+"/"+pp.getPersoId()+"/attaque";
	}
	
	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"actu"})
	public String actu(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersoCompletDto perso) {
	    return filledPage_Personnage(model, partie, perso);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"save"})
	public String save(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersoCompletDto perso) {
		PersoCompletDto persoSaved = persoServ.saveDto(perso);
		if(perso.getPersoId()==0) return "redirect:/azurhyan/"+partie+"/"+persoSaved.getPersoId();
	    return filledPage_Personnage(model, partie, persoSaved);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"addBless"})
	public String addBless(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersoCompletDto perso, final HttpServletRequest req) {
	    perso.getBlessureList().add(new BlessureDto(0, 0, req.getParameter("addBless")));
	    return filledPage_Personnage(model, partie, perso);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"removeBless"})
	public String removeBless(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersoCompletDto perso, final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("removeBless"));
	    perso.getBlessureList().remove(rowId.intValue());
	    return filledPage_Personnage(model, partie, perso);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"addHand"})
	public String addHand(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersoCompletDto perso, final HttpServletRequest req) {
	    perso.getHandicapList().add(new HandicapDto(0, TypeHand.valueOf(req.getParameter("addHand")), ""));
	    return filledPage_Personnage(model, partie, perso);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"removeHand"})
	public String removeHand(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersoCompletDto perso, final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("removeHand"));
	    perso.getHandicapList().remove(rowId.intValue());
	    return filledPage_Personnage(model, partie, perso);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"addCombo"})
	public String addCombo(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersoCompletDto perso) {
	    perso.getComboList().add(new ComboDto("nouveau combo", true));
	    return filledPage_Personnage(model, partie, perso);
	}

	@PostMapping(value="/azurhyan/{game}/{persoId}", params={"removeCombo"})
	public String removeCombo(Model model, @PathVariable("game") final String partie, @PathVariable("persoId") final int persoId, 
			@ModelAttribute("perso") PersoCompletDto perso, final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("removeCombo"));
	    perso.getComboList().remove(rowId.intValue());
	    return filledPage_Personnage(model, partie, perso);
	}

	
	private String filledPage_Personnage(Model model, String partie, PersoCompletDto perso) {
		model.addAttribute("partie", partie);
		if(perso==null) {
			perso = new PersoCompletDto(partie);
		}
		model.addAttribute("perso", perso);
//		model.addAttribute("combohandicap", perso.getComboHandicap());
//		model.addAttribute("combototal", perso.getComboTotal());
		return "personnage";
	}
	
}
