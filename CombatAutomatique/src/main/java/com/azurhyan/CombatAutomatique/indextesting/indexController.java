package com.azurhyan.CombatAutomatique.indextesting;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class indexController {
	
	@RequestMapping({"/","/seedstartermng"})
	public String showSeedstarters(final SeedStarter seedStarter) {
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

}
