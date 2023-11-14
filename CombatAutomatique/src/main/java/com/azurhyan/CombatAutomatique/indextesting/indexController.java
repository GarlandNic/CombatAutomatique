package com.azurhyan.CombatAutomatique.indextesting;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.azurhyan.CombatAutomatique.model.BlessureDB;
import com.azurhyan.CombatAutomatique.model.PersonnageDB;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class indexController {
	
	@RequestMapping({"/","/seedstartermng"})
	public String showSeedstarters(final PersonnageDB perso, final SeedStarter seedStarter) {
	    return "index";
	}
	
	@RequestMapping(value="/", params={"addRow"})
	public String addRow(final SeedStarter seedStarter, final BindingResult bindingResult) {
	    seedStarter.getRows().add(new Row());
	    return "index";
	}

	@RequestMapping(value="/", params={"removeRow"})
	public String removeRow(
	        final SeedStarter seedStarter, final BindingResult bindingResult, 
	        final HttpServletRequest req) {
	    final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
	    seedStarter.getRows().remove(rowId.intValue());
	    return "index";
	}
	
//	@RequestMapping(value="/bloub", params={"addRow"})
//	public String addRowBloub(final PersonnageDB perso, final BindingResult bindingResult) {
//	    perso.getBlessureList().add(new BlessureDB(perso, 0, 0));
//	    return "index";
//	}
//
//	@RequestMapping(value="/bloub", params={"removeRow"})
//	public String removeRowBloub(
//	        final PersonnageDB perso, final BindingResult bindingResult, 
//	        final HttpServletRequest req) {
//	    final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
//	    perso.getBlessureList().remove(rowId.intValue());
//	    return "index";
//	}

}
