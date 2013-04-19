package pals.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController 
{
	public static final String HOME_VIEW = "home";

	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView login()
	{
        ModelAndView mav = new ModelAndView();
		mav.setViewName(HOME_VIEW);
		return mav;
    }
}
