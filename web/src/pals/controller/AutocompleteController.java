package pals.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pals.database.SuburbDAO;

@Controller
public class AutocompleteController 
{
	@Autowired SuburbDAO suburbDAO;
	
	@RequestMapping(value="/autocomplete/suburb", method = RequestMethod.GET)
	public @ResponseBody List<String> suburbs(@RequestParam("query") String query) 
	{
		return suburbDAO.searchByName(query);
	}
}
