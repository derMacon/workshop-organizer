package com.dermacon.securewebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CourseController {

    @RequestMapping("/courses")
    public String index(Model model) {
        return "courses";
    }


}
