package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.service.CourseService;
import com.dermacon.securewebapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private PersonService personService;

    @RequestMapping(path = "/allCourses")
    public String showAllCourses(Model model) {
        model.addAttribute("loggedInPerson", personService.getLoggedInPerson());
        model.addAttribute("allCourses", courseService.getAllCourses());
        return "/fragments/courses/overview/allCourses";
    }



}
