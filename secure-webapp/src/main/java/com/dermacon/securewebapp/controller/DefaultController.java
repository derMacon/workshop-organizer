package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.PersonRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class DefaultController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CourseRepository courseRepository;


    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("selectedDomain", "home");

        return "index";
    }



    @RequestMapping("/admin")
    public String adminEntry(Model model) {
        System.out.println("admin entry");
        model.addAttribute("selectedDomain", "home");
        return "main";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        List<User> users = (List<User>) userRepository.findAll();

        model.addAttribute("users", users);
    }


}
