package com.dermacon.securewebapp.controller.admin;

import com.dermacon.securewebapp.controller.services.FlatmateService;
import com.dermacon.securewebapp.data.Flatmate;
import com.dermacon.securewebapp.data.FlatmateRepository;
import com.dermacon.securewebapp.data.InputPerson;
import com.dermacon.securewebapp.data.ItemPresetRepository;
import com.dermacon.securewebapp.data.LivingSpace;
import com.dermacon.securewebapp.data.LivingSpaceRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import com.dermacon.securewebapp.data.UserRole;
import com.dermacon.securewebapp.exception.FlatmateExistsException;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Controller
@Transactional
@RequestMapping("groceryList/admin")
public class AdminController {

    @Autowired
    private FlatmateService flatmateService;

    @Autowired
    private ItemPresetRepository itemPresetRepository;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String displayAdmin(Model model) {
        model.addAttribute("allFlatmates", flatmateService.getAllFlatmates());
        model.addAttribute("emptyLivingSpaces", flatmateService.findEmptyLivingSpaces());
        model.addAttribute("allItemPresets", itemPresetRepository.findAll());

        // empty flatmate object -> filled in input box
        model.addAttribute("inputPerson", new InputPerson());

        // empty flatmate list wrapper -> filled in selection box
        model.addAttribute("selectedFlatmates", new SelectedElements());

        // empty flatmate list wrapper -> filled in selection box
        model.addAttribute("selectedItemPresets", new SelectedElements());

        return "admin_main";
    }

    @RequestMapping(value = "/createFlatmate", method = RequestMethod.POST)
    public String createNewFlatmate_post(@ModelAttribute(value = "inputPerson") InputPerson person) {
        if (!flatmateService.createAndSafeFlatmate(person)) {
            // todo handling error
            System.out.println("todo handle error - AdminController, createNewFlatmate_post");
        }
        return "redirect:/groceryList/admin/";
    }


    @RequestMapping(value = "/removeFlatmate", method = RequestMethod.POST)
    public String removeFlatmate_post(@ModelAttribute(value = "selectedFlatmates") SelectedElements selectedFlatmateIds) {
        // foreach flatmate first remove user and then the entity itself
        selectedFlatmateIds.getCheckedElements().stream()
                .forEach(flatmateService::saveDeleteFlatmate);
        return "redirect:/groceryList/admin/";
    }


}
