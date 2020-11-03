package com.dermacon.securewebapp.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class ErrorService {

    public enum ERROR_CODE {
        INVALID_ROLE("no access - invalid permissions"),
        INVALID_COURSE("invalid course id");

        private final String message;

        ERROR_CODE(String message) {
            this.message = message;
        }
    }

    public String displayErrorPage(Model model, ERROR_CODE error_code) {
        model.addAttribute("error_message", error_code.message);
        return "error";
    }

}
