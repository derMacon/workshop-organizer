package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Announcement;
import com.dermacon.securewebapp.data.AnnouncementRepository;
import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class DefaultController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    PersonService personService;

    @Autowired
    AnnouncementRepository announcementRepository;


    @ModelAttribute
    public void displayLoggedInUser(Model model) {
//         todo why is this not working... not able to display in header template
        model.addAttribute("loggedInPerson", personService.getLoggedInPerson());
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("selectedDomain", "home");

        return "index";
    }

    /**
     * https://stackoverflow.com/questions/20848312/how-to-correctly-logout-user-in-spring-security
     */
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout"; //You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }



    @RequestMapping("/admin")
    public String adminEntry(Model model) {
        System.out.println("admin entry");
        model.addAttribute("selectedDomain", "home");
        return "main";
    }

}
