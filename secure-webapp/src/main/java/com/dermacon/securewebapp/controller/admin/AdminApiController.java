package com.dermacon.securewebapp.controller.admin;

import com.dermacon.securewebapp.controller.services.FlatmateService;
import com.dermacon.securewebapp.data.Flatmate;
import com.dermacon.securewebapp.data.InputPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("api/admin")
public class AdminApiController {

    @Autowired
    private FlatmateService flatmateService;

    /**
     * Returns all flatmate instances living in the flat
     * @return all flatmate instances living in the flat
     */
    @RequestMapping(path = "/info")
    public Iterable<Flatmate> getInfo() {
        return flatmateService.getAllFlatmates();
    }

    /**
     * Adds a new flatmate to the application
     * @param firstname firstname of the flatmate
     * @param surname surname of the flatmate
     * @param birthday birthday of the flatmate
     * @param livingSpaceId id of the living space
     * @return true if the new flatmate was created successfully, false if not
     */
    @RequestMapping(path = "/addFlatmate_param")
    public boolean addFlatmate(@RequestParam String firstname,
                               @RequestParam String surname,
                               @RequestParam Date birthday,
                               @RequestParam long livingSpaceId
    ) {
        InputPerson person = new InputPerson(firstname, surname, birthday, livingSpaceId);
        return flatmateService.createAndSafeFlatmate(person);
    }


    /**
     * Adds a new flatmate to the application
     * @param person Input Person instance that should be parsed to a flatmate
     *               should be saved to the database
     * @return true if the new flatmate was created successfully, false if not
     */
    @RequestMapping(path = "/addFlatmate")
    public boolean addFlatmate(@RequestParam InputPerson person) {
        return flatmateService.createAndSafeFlatmate(person);
    }

    /**
     * Removes the flatmate with a given id
     * @param id id of flatmate
     * @return true if the system was able to remove the specified flatmate
     */
    @PostMapping(value = "/removeFlatmate")
    public boolean removeFlatmate(@RequestParam long id) {
        return flatmateService.saveDeleteFlatmate(id);
    }

}
