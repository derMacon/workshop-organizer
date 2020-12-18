package com.dermacon.securewebapp.controller.services;

import com.dermacon.securewebapp.data.Flatmate;
import com.dermacon.securewebapp.data.FlatmateRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.TimeZone;

@Service
public class UserService {

    @Autowired
    private FlatmateRepository flatmateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * generate user (username: <firstname>; pw: <lastname><birthday>)
     *
     * @param flatmate
     * @return
     */
    public User generateUser(final Flatmate flatmate) {
        assert (flatmate.getUser() == null);

        User newUser = new User();
        newUser.setUsername(generateUsername(flatmate));
        String password = generatePassword(flatmate);
        String hash = passwordEncoder.encode(password);
        newUser.setPassword(hash);
        newUser.setRole(UserRole.ROLE_USER);

        return newUser;
    }

    /**
     * Creates a Username from a given String. If there is already a flatmate with the same first
     * name the system will append as many letters from the last name as needed to make the
     * username unique
     * @param flatmate Flatmate instance
     * @return username
     */
    private String generateUsername(Flatmate flatmate) {
        String username = flatmate.getFirstname().toLowerCase();
        // flatmate with same firstname already exists
        // -> append as much letters from surname as needed to make it unique
        int i = 0;
        String surname = capitalizeWord(flatmate.getSurname());
        while (flatmateRepository.findByFirstname(username) != null && i < surname.length()) {
            username += flatmate.getSurname().substring(i, i + 1);
            i++;
        }
        return username;
    }

    /**
     * Capitalizes word
     * @param word word
     * @return capitalized word
     */
    private String capitalizeWord(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    /**
     * Generates a password from the given flatmate instance
     * Password: [surname][birthday][monthOfBirth]
     * e.g. surname0101 for user a user with the lastname 'surname' who's birthday is on the
     * 01.01.XXXX
     * @param flatmate Flatmate instance
     * @return password string
     */
    private String generatePassword(Flatmate flatmate) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(flatmate.getBirthday());

        int day = cal.get(Calendar.DAY_OF_MONTH);
        // for some reason the month of january will evaluate to month == 0
        int month = cal.get(Calendar.MONTH) + 1;

        String month_str = month < 10
                ? "0" + month
                : "" + month;

        String day_str = day < 10
                ? "0" + day
                : "" + day;

        return flatmate.getSurname().toLowerCase() + day_str + month_str;
    }

}
