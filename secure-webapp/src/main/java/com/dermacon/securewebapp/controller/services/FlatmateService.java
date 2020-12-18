package com.dermacon.securewebapp.controller.services;

import com.dermacon.securewebapp.data.Flatmate;
import com.dermacon.securewebapp.data.FlatmateRepository;
import com.dermacon.securewebapp.data.InputPerson;
import com.dermacon.securewebapp.data.LivingSpace;
import com.dermacon.securewebapp.data.LivingSpaceRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import com.dermacon.securewebapp.data.UserRole;
import com.dermacon.securewebapp.exception.FlatmateExistsException;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class FlatmateService {

    @Autowired
    private FlatmateRepository flatmateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LivingSpaceRepository livingSpaceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * first remove user and then the entity itself
     * @param flatmateId flatmate id that will be removed
     */
    public boolean saveDeleteFlatmate(long flatmateId) {
        Optional<Flatmate> flatmate_opt = flatmateRepository.findById(flatmateId);
        if (!flatmate_opt.isPresent()) {
            return false;
        }

        Flatmate flatmate = flatmate_opt.get();
        User user = flatmate.getUser();
        LoggerSingleton.getInstance().info("delete user: " + user);
        userRepository.delete(user);
        flatmate.setUser(null);
        flatmate.setLivingSpace(null);
        flatmateRepository.delete(flatmate);

        return true; // successful deletion
    }

    public Iterable<Flatmate> getAllFlatmates() {
        return flatmateRepository.findAll();
    }

    public Set<LivingSpace> findEmptyLivingSpaces() {
        return StreamSupport.stream(livingSpaceRepository.findAll().spliterator(), false)
                .filter(e -> flatmateRepository.findByLivingSpace(e) == null)
                .collect(Collectors.toSet());
    }

    public boolean createAndSafeFlatmate(InputPerson person) {
        String firstname = person.getFirstname();
        String surname = person.getSurname();
        Flatmate fm = flatmateRepository.findByFirstnameAndSurname(firstname, surname);
        if (fm != null) {
            LoggerSingleton.getInstance().warning("flatmate already exists");
            return false; // unsuccessful return
        }

        // form only sets living space id -> necessary to load whole entity
        Long formInput_id = person.getLivingSpaceId();
        LivingSpace livingSpace = livingSpaceRepository.findById(formInput_id).get();

        // generate user (username: <firstname>; pw: <lastname><birthday-day><birthday-month>)
        User newUser = generateUser(person);
        newUser = userRepository.save(newUser);

        Flatmate flatmate = new Flatmate(
                newUser,
                person.getFirstname(),
                person.getSurname(),
                person.getBirthday(),
                livingSpace);

        // save in database
        LoggerSingleton.getInstance().info("save flatmate: " + person);
        flatmateRepository.save(flatmate);
        return true; // successful return
    }

    /**
     * generate user (username: <firstname>; pw: <lastname><birthday>)
     *
     * @param person
     * @return
     */
    private User generateUser(InputPerson person) {
        User newUser = new User();

        newUser.setUsername(generateUsername(person));
        String hash = passwordEncoder.encode(generatePassword(person));
        newUser.setPassword(hash);
        newUser.setRole(UserRole.ROLE_USER);

        return newUser;
    }

    private String generateUsername(InputPerson person) {
        String username = person.getFirstname().toLowerCase();
        // flatmate with same firstname already exists
        // -> append as much letters from surname as needed to make it unique
        int i = 0;
        String surname = capitalizeWord(person.getSurname());
        while (flatmateRepository.findByFirstname(username) != null && i < surname.length()) {
            username += person.getSurname().substring(i, i + 1);
            i++;
        }
        return username;
    }

    private String capitalizeWord(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    private String generatePassword(InputPerson person) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(person.getBirthday());

        int day = cal.get(Calendar.DAY_OF_MONTH);
        // for some reason the month of january will evaluate to month == 0
        int month = cal.get(Calendar.MONTH) + 1;

        String month_str = month < 10
                ? "0" + month
                : "" + month;

        String day_str = day < 10
                ? "0" + day
                : "" + day;

        return person.getSurname().toLowerCase() + day_str + month_str;
    }


    /**
     * Returns the Flatmate entity of the currently logged in user.
     * @return the Flatmate entity of the currently logged in user.
     */
    public Flatmate getLoggedInFlatmate() {
        User currUser = getLoggedInUser();
        // todo use flatmateRepository for this
        Flatmate loggedInFlatmate = null;
        for (Flatmate fm : flatmateRepository.findAll()) {
            if (fm.getUser().getUserId() == currUser.getUserId()) {
                loggedInFlatmate = fm;
            }
        }
        return loggedInFlatmate;
    }

    /**
     * Determines the currently logged in user
     * @return the currently logged in user
     */
    private User getLoggedInUser() {
        // for some reason the id is always 0
        String user_name = ((UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getUsername();

        return userRepository.findByUsername(user_name);
    }
}
